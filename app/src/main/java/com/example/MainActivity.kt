package com.example

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ads.AdNetworkManager
import com.example.data.VpnStatus
import com.example.ui.VpnViewModel
import com.example.ui.components.ConnectingModalDialog
import com.example.ui.components.DisconnectConfirmDialog
import com.example.ui.components.DisconnectingModalDialog
import com.example.ui.screens.MainVpnScreen
import com.example.ui.screens.OnboardingLanguageScreen
import com.example.ui.screens.OnboardingPrivacyScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.VpnDarkBg

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AdNetworkManager.initialize(applicationContext)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = VpnDarkBg
                ) {
                    VpnAppRoot()
                }
            }
        }
    }
}

@Composable
fun VpnAppRoot(
    viewModel: VpnViewModel = viewModel()
) {
    val context = LocalContext.current

    val isSplashScreenCompleted by viewModel.isSplashScreenCompleted.collectAsState()
    val isServerListReady by viewModel.isServerListReady.collectAsState()
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsState()
    val onboardingStep by viewModel.onboardingStep.collectAsState()
    val selectedLanguageCode by viewModel.selectedLanguageCode.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val vpnStatus by viewModel.vpnStatus.collectAsState()
    val selectedServer by viewModel.selectedServer.collectAsState()
    val servers by viewModel.servers.collectAsState()
    val isFetchingServers by viewModel.isFetchingServers.collectAsState()
    val serversFetchError by viewModel.serversFetchError.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val killSwitchEnabled by viewModel.killSwitchEnabled.collectAsState()
    val dnsProtectionEnabled by viewModel.dnsProtectionEnabled.collectAsState()
    val selectedProtocol by viewModel.selectedProtocol.collectAsState()
    val connectingProgress by viewModel.connectingProgress.collectAsState()
    val sessionExpiredNotice by viewModel.sessionExpiredNotice.collectAsState()
    val showDisconnectConfirm by viewModel.showDisconnectConfirm.collectAsState()
    val isDisconnecting by viewModel.isDisconnecting.collectAsState()
    val isRocketLoading by viewModel.isRocketLoading.collectAsState()
    val connectingLogMessage by viewModel.connectingLogMessage.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    // Show Toast messages (e.g. for connection errors or permission denied)
    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            viewModel.clearToast()
        }
    }

    // 1. Android Notification Permission Launcher (API 33+)
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* Handled gracefully */ }

    // 2. Official Android VpnService.prepare() System Dialog Launcher
    val vpnPrepareLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Permission granted by user via official Android system dialog!
            viewModel.onVpnPermissionGranted(context)
        } else {
            // User cancelled or denied VPN permission dialog
            viewModel.onVpnPermissionDenied()
        }
    }

    // Connect / Disconnect Handler invoking native VpnService & Interstitial Ads
    val handleConnectToggle: () -> Unit = {
        when (vpnStatus) {
            VpnStatus.DISCONNECTED, VpnStatus.ERROR -> {
                viewModel.clearError()

                // Check notification permission for foreground persistent notification on Android 13+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                // Step 1: Check VpnService.prepare and trigger 20-second loading & Ad
                viewModel.startConnectionFlow(context) { vpnIntent ->
                    vpnPrepareLauncher.launch(vpnIntent)
                }
            }
            VpnStatus.CONNECTED -> {
                // Step 1 in Disconnection: Show confirmation dialog ("آیا از قطع ارتباط اطمینان دارید؟")
                viewModel.promptDisconnect()
            }
            VpnStatus.CONNECTING -> {
                viewModel.cancelConnecting()
            }
        }
    }

    // Determine layout direction (RTL for Persian and Arabic)
    val layoutDirection = if (selectedLanguageCode == "fa" || selectedLanguageCode == "ar") {
        LayoutDirection.Rtl
    } else {
        LayoutDirection.Ltr
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        AnimatedContent(
            targetState = isSplashScreenCompleted,
            transitionSpec = {
                fadeIn(animationSpec = tween(500)).togetherWith(fadeOut(animationSpec = tween(500)))
            },
            label = "splash_transition"
        ) { splashDone ->
            if (!splashDone) {
                SplashScreen(
                    isServerListReady = isServerListReady,
                    onSplashFinished = {
                        viewModel.completeSplashScreen()
                    }
                )
            } else {
                if (!isOnboardingCompleted) {
                    // Onboarding Flow: 2 sequential screens
                    AnimatedContent(
                        targetState = onboardingStep,
                        transitionSpec = {
                            if (targetState > initialState) {
                                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                                    slideOutHorizontally { width -> -width } + fadeOut()
                                )
                            } else {
                                (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                                    slideOutHorizontally { width -> width } + fadeOut()
                                )
                            }
                        },
                        label = "onboarding_flow"
                    ) { step ->
                        when (step) {
                            1 -> {
                                OnboardingLanguageScreen(
                                    selectedLanguageCode = selectedLanguageCode,
                                    onLanguageSelected = { lang ->
                                        viewModel.selectLanguage(lang)
                                    },
                                    onContinueClick = {
                                        viewModel.goToPrivacyScreen()
                                    }
                                )
                            }
                            2 -> {
                                BackHandler {
                                    viewModel.backToLanguageScreen()
                                }
                                OnboardingPrivacyScreen(
                                    langCode = selectedLanguageCode,
                                    onBackClick = {
                                        viewModel.backToLanguageScreen()
                                    },
                                    onAcceptAndContinue = {
                                        viewModel.completeOnboarding()
                                    }
                                )
                            }
                        }
                    }
                } else {
                    // Main VPN Screen with modal ConnectingModalDialog overlay
                    Box(modifier = Modifier.fillMaxSize()) {
                        MainVpnScreen(
                            currentTab = currentTab,
                            onTabSelected = { viewModel.setTab(it) },
                            vpnStatus = vpnStatus,
                            selectedServer = selectedServer,
                            servers = servers,
                            stats = stats,
                            langCode = selectedLanguageCode,
                            killSwitchEnabled = killSwitchEnabled,
                            dnsProtectionEnabled = dnsProtectionEnabled,
                            selectedProtocol = selectedProtocol,
                            isFetchingServers = isFetchingServers,
                            serversFetchError = serversFetchError,
                            onRetryFetchServers = { viewModel.retryFetchServers() },
                            onToggleConnect = handleConnectToggle,
                            onSelectServer = { viewModel.selectServer(it, context) },
                            onKillSwitchToggle = { viewModel.setKillSwitch(it) },
                            onDnsToggle = { viewModel.setDnsProtection(it) },
                            onProtocolSelected = { viewModel.setProtocol(it) },
                            onLanguageSelected = { viewModel.selectLanguage(it) },
                            onResetOnboarding = { viewModel.resetOnboarding() },
                            sessionExpiredNotice = sessionExpiredNotice,
                            onDismissSessionExpiredNotice = { viewModel.dismissSessionExpiredNotice() },
                            errorMessage = errorMessage,
                            onClearError = { viewModel.clearError() },
                            onConnectOffline = {
                                viewModel.startConnectionFlow(context) { vpnIntent ->
                                    vpnPrepareLauncher.launch(vpnIntent)
                                }
                            }
                        )

                        AnimatedVisibility(
                            visible = isRocketLoading,
                            enter = fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.95f),
                            exit = fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 1.05f)
                        ) {
                            ConnectingModalDialog(
                                server = selectedServer,
                                progress = connectingProgress,
                                logMessage = connectingLogMessage,
                                langCode = selectedLanguageCode,
                                onCancel = { viewModel.cancelConnecting() }
                            )
                        }

                        // Disconnecting Modal Loader (Original circular spinner with power icon)
                        AnimatedVisibility(
                            visible = isDisconnecting,
                            enter = fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.95f),
                            exit = fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 1.05f)
                        ) {
                            DisconnectingModalDialog(
                                server = selectedServer,
                                langCode = selectedLanguageCode,
                                onCancel = { viewModel.cancelDisconnecting() }
                            )
                        }

                        // Disconnect Confirmation Dialog ("مطمئنی میخوای اتصال قطع بشه؟")
                        if (showDisconnectConfirm) {
                            DisconnectConfirmDialog(
                                langCode = selectedLanguageCode,
                                onConfirm = { viewModel.confirmDisconnect(context) },
                                onDismiss = { viewModel.dismissDisconnectConfirm() }
                            )
                        }
                    }
                }
            }
        }
    }
}
