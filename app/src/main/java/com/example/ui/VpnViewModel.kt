package com.example.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.VpnService
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ads.AdNetworkManager
import com.example.data.BottomTab
import com.example.data.DefaultData
import com.example.data.Language
import com.example.data.PreferencesManager
import com.example.data.RemoteConfigException
import com.example.data.VpnRemoteConfigRepository
import com.example.data.VpnServer
import com.example.data.VpnStats
import com.example.data.VpnStatus
import com.example.service.VpnTunnelService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VpnViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = PreferencesManager(application)
    private val remoteConfigRepo = VpnRemoteConfigRepository()

    private val _isOnboardingCompleted = MutableStateFlow(prefs.isOnboardingCompleted)
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted.asStateFlow()

    private val _onboardingStep = MutableStateFlow(1) // 1: Language, 2: Privacy
    val onboardingStep: StateFlow<Int> = _onboardingStep.asStateFlow()

    private val _selectedLanguageCode = MutableStateFlow(prefs.selectedLanguageCode)
    val selectedLanguageCode: StateFlow<String> = _selectedLanguageCode.asStateFlow()

    private val _currentTab = MutableStateFlow(BottomTab.HOME)
    val currentTab: StateFlow<BottomTab> = _currentTab.asStateFlow()

    private val _vpnStatus = MutableStateFlow(VpnStatus.DISCONNECTED)
    val vpnStatus: StateFlow<VpnStatus> = _vpnStatus.asStateFlow()

    private val _servers = MutableStateFlow(DefaultData.servers)
    val servers: StateFlow<List<VpnServer>> = _servers.asStateFlow()

    private val _selectedServer = MutableStateFlow(
        DefaultData.servers.find { it.id == prefs.selectedServerId } ?: DefaultData.servers.first()
    )
    val selectedServer: StateFlow<VpnServer> = _selectedServer.asStateFlow()

    private val _stats = MutableStateFlow(VpnStats(virtualIp = _selectedServer.value.ipAddress))
    val stats: StateFlow<VpnStats> = _stats.asStateFlow()

    private val _killSwitchEnabled = MutableStateFlow(prefs.killSwitchEnabled)
    val killSwitchEnabled: StateFlow<Boolean> = _killSwitchEnabled.asStateFlow()

    private val _dnsProtectionEnabled = MutableStateFlow(prefs.dnsProtectionEnabled)
    val dnsProtectionEnabled: StateFlow<Boolean> = _dnsProtectionEnabled.asStateFlow()

    private val _selectedProtocol = MutableStateFlow(prefs.vpnProtocol)
    val selectedProtocol: StateFlow<String> = _selectedProtocol.asStateFlow()

    private val _connectingProgress = MutableStateFlow(0f)
    val connectingProgress: StateFlow<Float> = _connectingProgress.asStateFlow()

    private val _connectingRemainingSeconds = MutableStateFlow(15)
    val connectingRemainingSeconds: StateFlow<Int> = _connectingRemainingSeconds.asStateFlow()

    private val _connectingStep = MutableStateFlow(0)
    val connectingStep: StateFlow<Int> = _connectingStep.asStateFlow()

    private val _connectingLog = MutableStateFlow("")
    val connectingLog: StateFlow<String> = _connectingLog.asStateFlow()

    private val _sessionExpiredNotice = MutableStateFlow(false)
    val sessionExpiredNotice: StateFlow<Boolean> = _sessionExpiredNotice.asStateFlow()

    private val _showDisconnectConfirm = MutableStateFlow(false)
    val showDisconnectConfirm: StateFlow<Boolean> = _showDisconnectConfirm.asStateFlow()

    private val _isDisconnecting = MutableStateFlow(false)
    val isDisconnecting: StateFlow<Boolean> = _isDisconnecting.asStateFlow()

    private val _isRocketLoading = MutableStateFlow(false)
    val isRocketLoading: StateFlow<Boolean> = _isRocketLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var connectionJob: Job? = null
    private var disconnectJob: Job? = null
    private var timerJob: Job? = null
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    init {
        // Step 1: App Initialization & Location Population
        // As soon as the app launches (on startup/initialization), silently execute HTTP GET request to subscription link
        loadSubscriptionLocationsSilently()
        registerNetworkAutoRetry()

        // Observe real Android VpnTunnelService status changes (e.g., if user taps Disconnect in notification)
        viewModelScope.launch {
            VpnTunnelService.isTunnelActive.collect { isActive ->
                if (!isActive && _vpnStatus.value == VpnStatus.CONNECTED) {
                    disconnectVpnInternal()
                }
            }
        }
    }

    /**
     * Auto-Retry: If the user opens the app without an internet connection,
     * silently retry fetching the subscription link the moment the network
     * becomes available, refreshing the location list dynamically.
     */
    private fun registerNetworkAutoRetry() {
        try {
            val cm = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            if (cm != null) {
                val request = NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build()
                val callback = object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        Log.i(TAG, "Network is available; silently auto-retrying subscription fetch...")
                        loadSubscriptionLocationsSilently()
                    }
                }
                cm.registerNetworkCallback(request, callback)
                networkCallback = callback
            }
        } catch (e: Exception) {
            Log.w(TAG, "Could not register network callback: ${e.message}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        try {
            val cm = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            networkCallback?.let { cm?.unregisterNetworkCallback(it) }
        } catch (_: Exception) {}
    }

    /**
     * Silently fetches and parses the subscription link on startup to populate
     * unique country locations in the location selection UI before connecting.
     */
    fun loadSubscriptionLocationsSilently() {
        viewModelScope.launch {
            try {
                val parsedServers = remoteConfigRepo.fetchSubscriptionServers()
                if (parsedServers.isNotEmpty()) {
                    _servers.value = parsedServers
                    val currentSelected = _selectedServer.value
                    val matching = parsedServers.find {
                        it.id == currentSelected.id || it.name.equals(currentSelected.name, ignoreCase = true)
                    } ?: parsedServers.first()
                    _selectedServer.value = matching
                    _stats.update { it.copy(virtualIp = matching.ipAddress) }
                    Log.i(TAG, "Silently loaded ${parsedServers.size} unique country locations: ${parsedServers.map { it.name }}")
                }
            } catch (e: Exception) {
                Log.w(TAG, "Silently handled subscription fetch fallback on app launch: ${e.message}")
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
        if (_vpnStatus.value == VpnStatus.ERROR) {
            _vpnStatus.value = VpnStatus.DISCONNECTED
        }
    }

    fun promptDisconnect() {
        _showDisconnectConfirm.value = true
    }

    fun dismissDisconnectConfirm() {
        _showDisconnectConfirm.value = false
    }

    fun confirmDisconnect(context: Context) {
        _showDisconnectConfirm.value = false
        startDisconnecting(context)
    }

    fun startDisconnecting(context: Context) {
        disconnectJob?.cancel()
        disconnectJob = viewModelScope.launch {
            // Step 3: Trigger 12-second loading animation state
            _isDisconnecting.value = true
            val totalTicks = 120 // 12 seconds loading animation (120 * 100ms)
            for (tick in 1..totalTicks) {
                delay(100)
            }
            _isDisconnecting.value = false

            // Followed immediately by displaying Interstitial Ad programmatically (no physical box on screen)
            val activity = context as? Activity
            if (activity != null) {
                AdNetworkManager.showInterstitial(
                    activity = activity,
                    adType = AdNetworkManager.AdType.DISCONNECT
                ) {
                    // Step 4: The precise moment the user closes the Ad, terminate Android's VpnService
                    VpnTunnelService.stopVpn(context)
                    disconnectVpnInternal()
                }
            } else {
                VpnTunnelService.stopVpn(context)
                disconnectVpnInternal()
            }
        }
    }

    fun cancelDisconnecting() {
        disconnectJob?.cancel()
        _isDisconnecting.value = false
    }

    fun dismissSessionExpiredNotice() {
        _sessionExpiredNotice.value = false
    }

    fun selectLanguage(language: Language) {
        _selectedLanguageCode.value = language.code
        prefs.selectedLanguageCode = language.code
    }

    fun goToPrivacyScreen() {
        _onboardingStep.value = 2
    }

    fun backToLanguageScreen() {
        _onboardingStep.value = 1
    }

    fun completeOnboarding() {
        prefs.isOnboardingCompleted = true
        _isOnboardingCompleted.value = true
        _onboardingStep.value = 1
    }

    fun resetOnboarding() {
        prefs.resetOnboarding()
        _isOnboardingCompleted.value = false
        _onboardingStep.value = 1
    }

    fun setTab(tab: BottomTab) {
        _currentTab.value = tab
    }

    fun selectServer(server: VpnServer, context: Context? = null) {
        _selectedServer.value = server
        prefs.selectedServerId = server.id
        _stats.update { it.copy(virtualIp = server.ipAddress) }

        if (_vpnStatus.value == VpnStatus.CONNECTED && context != null) {
            // Smoothly reconnect to the new server with real tunnel
            finalizeConnection(context)
        }
    }

    fun onVpnPermissionDenied() {
        _vpnStatus.value = VpnStatus.ERROR
        val isFa = _selectedLanguageCode.value == "fa"
        _errorMessage.value = if (isFa) {
            "خطا در اتصال. لطفاً اینترنت خود را بررسی کنید یا سرور دیگری انتخاب نمایید"
        } else {
            "Connection failed. Please check your internet connection."
        }
    }

    companion object {
        const val MAX_SESSION_SECONDS = 5400L // 1 hour 30 minutes (90 minutes)
        private const val TAG = "VpnViewModel"
    }

    fun cancelConnecting() {
        connectionJob?.cancel()
        _isRocketLoading.value = false
        _vpnStatus.value = VpnStatus.DISCONNECTED
        _connectingProgress.value = 0f
        _connectingRemainingSeconds.value = 15
        _connectingStep.value = 0
        _connectingLog.value = ""
    }

    /**
     * Connection Flow Logic:
     * Step 1: When the user clicks "Connect", trigger existing "Rocket Flying" animation (Loading state).
     * Step 2: Immediately after loading animation, load & display Interstitial Ad programmatically (AdMob/Tapsell).
     * Step 3: Exact millisecond ad is closed, silently fetch GitHub remote config and invoke VpnService.prepare.
     */
    fun startConnectionFlow(context: Context, onRequireVpnPermission: (Intent) -> Unit) {
        connectionJob?.cancel()
        disconnectJob?.cancel()
        timerJob?.cancel()
        _isDisconnecting.value = false
        _showDisconnectConfirm.value = false
        _sessionExpiredNotice.value = false
        _errorMessage.value = null

        connectionJob = viewModelScope.launch {
            _vpnStatus.value = VpnStatus.CONNECTING
            _isRocketLoading.value = true
            _connectingProgress.value = 0.05f
            _connectingRemainingSeconds.value = 4
            _connectingStep.value = 0
            val isFa = _selectedLanguageCode.value == "fa"
            _connectingLog.value = if (isFa) "در حال آغاز اتصال پرسرعت..." else "Initializing rocket launch..."

            // Step 1: Smooth Rocket Flying Loading Animation (~3.5 seconds)
            val totalTicks = 35
            for (tick in 1..totalTicks) {
                delay(100)
                val progress = tick.toFloat() / totalTicks.toFloat()
                _connectingProgress.value = progress

                val remainingSec = ((totalTicks - tick) * 100 + 999) / 1000
                _connectingRemainingSeconds.value = remainingSec

                when (tick) {
                    8 -> {
                        _connectingStep.value = 1
                        _connectingLog.value = if (isFa) "مسیریابی بهینه سرور ${_selectedServer.value.nameFa}..." else "Routing to ${_selectedServer.value.name}..."
                    }
                    18 -> {
                        _connectingStep.value = 2
                        _connectingLog.value = if (isFa) "تولید کلیدهای تبادل رمزگذاری..." else "Generating encryption keys..."
                    }
                    28 -> {
                        _connectingStep.value = 3
                        _connectingLog.value = if (isFa) "آماده‌سازی تونل ایمن..." else "Preparing secure tunnel..."
                    }
                }
            }

            // Immediately after the loading animation, dismiss rocket dialog
            _isRocketLoading.value = false
            _connectingProgress.value = 1.0f

            // Step 2 & 3: Load and display Interstitial Ad programmatically (AdMob / Tapsell)
            // No physical layouts or boxes on the screen!
            val activity = context as? Activity
            if (activity != null) {
                AdNetworkManager.showInterstitial(
                    activity = activity,
                    adType = AdNetworkManager.AdType.CONNECT
                ) {
                    executePostAdConnect(context, onRequireVpnPermission)
                }
            } else {
                executePostAdConnect(context, onRequireVpnPermission)
            }
        }
    }

    private fun executePostAdConnect(context: Context, onRequireVpnPermission: (Intent) -> Unit) {
        viewModelScope.launch {
            _vpnStatus.value = VpnStatus.CONNECTING
            val currentServers = _servers.value

            // Check the selected location from the UI, with automatic fallback if missing or invalid
            val targetServer = currentServers.find { it.id == _selectedServer.value.id }
                ?: currentServers.find { it.name.equals(_selectedServer.value.name, ignoreCase = true) }
                ?: currentServers.firstOrNull()
                ?: _selectedServer.value

            // Update selected server and virtual IP
            _selectedServer.value = targetServer
            _stats.update { it.copy(virtualIp = targetServer.ipAddress) }
            Log.i(TAG, "Selected location: ${targetServer.name}, protocol: ${targetServer.protocol}, config: ${targetServer.configProfile?.take(35)}...")

            // Invoke native Android VpnService.prepare(context)
            try {
                val vpnIntent = VpnService.prepare(context)
                if (vpnIntent != null) {
                    // Official system permission dialog required
                    onRequireVpnPermission(vpnIntent)
                } else {
                    // Permission already granted, proceed directly to establish tunnel
                    finalizeConnection(context, targetServer)
                }
            } catch (e: Exception) {
                Log.e(TAG, "VpnService.prepare failed", e)
                _vpnStatus.value = VpnStatus.ERROR
                _errorMessage.value = if (_selectedLanguageCode.value == "fa") {
                    "خطا در اتصال. لطفاً اینترنت خود را بررسی کنید یا سرور دیگری انتخاب نمایید"
                } else {
                    "Connection failed. Please check your internet connection."
                }
            }
        }
    }

    /**
     * Step 4 in Connection Flow:
     * Starts Android VpnTunnelService as Foreground Service with persistent notification
     * and triggers native status bar key icon. Passes matching config string to tunnel.
     */
    fun finalizeConnection(context: Context, serverOverride: VpnServer? = null) {
        val isFa = _selectedLanguageCode.value == "fa"
        val server = serverOverride ?: _selectedServer.value
        try {
            VpnTunnelService.startVpn(
                context = context,
                serverName = if (isFa) server.nameFa else server.name,
                serverIp = server.ipAddress,
                protocol = server.protocol.ifEmpty { _selectedProtocol.value },
                dnsEnabled = _dnsProtectionEnabled.value,
                configProfile = server.configProfile
            )

            _vpnStatus.value = VpnStatus.CONNECTED
            _errorMessage.value = null
            startSessionStats()
            Log.i(TAG, "VPN tunnel established successfully for ${server.name}")
        } catch (e: Exception) {
            Log.e(TAG, "Error starting VpnTunnelService for ${server.name}", e)
            // Robust Error Handling: fallback to first available working server if selected server fails
            val fallback = _servers.value.firstOrNull { it.id != server.id && !it.configProfile.isNullOrBlank() }
            if (fallback != null && serverOverride == null) {
                Log.i(TAG, "Attempting automatic fallback to server: ${fallback.name}")
                _selectedServer.value = fallback
                _stats.update { it.copy(virtualIp = fallback.ipAddress) }
                finalizeConnection(context, fallback)
                return
            }

            _vpnStatus.value = VpnStatus.ERROR
            _errorMessage.value = if (isFa) {
                "خطا در اتصال. لطفاً اینترنت خود را بررسی کنید یا سرور دیگری انتخاب نمایید"
            } else {
                "Connection failed. Please check your internet connection."
            }
        }
    }

    fun disconnectVpn(context: Context, dueToTimeout: Boolean = false) {
        VpnTunnelService.stopVpn(context)
        disconnectVpnInternal(dueToTimeout)
    }

    private fun disconnectVpnInternal(dueToTimeout: Boolean = false) {
        connectionJob?.cancel()
        disconnectJob?.cancel()
        timerJob?.cancel()
        VpnTunnelService.resetTrafficCounters()
        _isDisconnecting.value = false
        _showDisconnectConfirm.value = false
        _vpnStatus.value = VpnStatus.DISCONNECTED
        _connectingProgress.value = 0f
        _connectingRemainingSeconds.value = 15
        _connectingStep.value = 0
        _connectingLog.value = ""
        if (dueToTimeout) {
            _sessionExpiredNotice.value = true
        }
        _stats.update {
            it.copy(
                durationSeconds = 0L,
                remainingLimitSeconds = MAX_SESSION_SECONDS,
                downloadSpeedMbps = 0f,
                uploadSpeedMbps = 0f,
                bytesReceivedMb = 0f,
                bytesSentMb = 0f
            )
        }
    }

    private fun startSessionStats() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var duration = 0L

            // Baseline bytes at connection start
            val baseline = VpnTunnelService.getLiveTrafficBytes()
            var lastRx = baseline.first
            var lastTx = baseline.second

            while (_vpnStatus.value == VpnStatus.CONNECTED) {
                delay(1000)
                duration++
                val remainingSeconds = (MAX_SESSION_SECONDS - duration).coerceAtLeast(0L)

                // Read real-time throughput from VpnService / kernel interface
                val current = VpnTunnelService.getLiveTrafficBytes()
                val currentRx = current.first
                val currentTx = current.second

                val deltaRx = (currentRx - lastRx).coerceAtLeast(0L)
                val deltaTx = (currentTx - lastTx).coerceAtLeast(0L)
                lastRx = currentRx
                lastTx = currentTx

                // Convert actual bytes/second to MB/s (1 MB = 1024 * 1024 bytes)
                val downSpeedMb = deltaRx.toFloat() / (1024f * 1024f)
                val upSpeedMb = deltaTx.toFloat() / (1024f * 1024f)

                // Total throughput in MB accumulated across the session
                val totalDownMb = (currentRx - baseline.first).coerceAtLeast(0L).toFloat() / (1024f * 1024f)
                val totalUpMb = (currentTx - baseline.second).coerceAtLeast(0L).toFloat() / (1024f * 1024f)

                _stats.value = VpnStats(
                    durationSeconds = duration,
                    remainingLimitSeconds = remainingSeconds,
                    downloadSpeedMbps = downSpeedMb,
                    uploadSpeedMbps = upSpeedMb,
                    bytesReceivedMb = totalDownMb,
                    bytesSentMb = totalUpMb,
                    virtualIp = _selectedServer.value.ipAddress
                )

                // 1.5 Hour (5400 seconds) auto-disconnect rule
                if (duration >= MAX_SESSION_SECONDS) {
                    _sessionExpiredNotice.value = true
                    VpnTunnelService.stopVpn(getApplication())
                    disconnectVpnInternal(dueToTimeout = true)
                    break
                }
            }
        }
    }

    fun setKillSwitch(enabled: Boolean) {
        _killSwitchEnabled.value = enabled
        prefs.killSwitchEnabled = enabled
    }

    fun setDnsProtection(enabled: Boolean) {
        _dnsProtectionEnabled.value = enabled
        prefs.dnsProtectionEnabled = enabled
    }

    fun setProtocol(protocol: String) {
        _selectedProtocol.value = protocol
        prefs.vpnProtocol = protocol
    }
}
