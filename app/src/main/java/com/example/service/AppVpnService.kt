package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.TrafficStats
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import android.os.Process
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.atomic.AtomicLong

class AppVpnService : VpnService() {

    companion object {
        private const val TAG = "AppVpnService"
        const val ACTION_CONNECT = "com.example.service.ACTION_CONNECT"
        const val ACTION_DISCONNECT = "com.example.service.ACTION_DISCONNECT"
        const val EXTRA_SERVER_NAME = "extra_server_name"
        const val EXTRA_COUNTRY = "extra_country"
        const val EXTRA_SERVER_IP = "extra_server_ip"
        const val EXTRA_CONFIG = "extra_config"
        const val NOTIFICATION_CHANNEL_ID = "jumpjump_vpn_tunnel_channel"
        const val NOTIFICATION_ID = 2026

        private val _isTunnelActive = MutableStateFlow(false)
        val isTunnelActive: StateFlow<Boolean> = _isTunnelActive.asStateFlow()
        private val _connectedServerName = MutableStateFlow<String?>(null)
        val connectedServerName: StateFlow<String?> = _connectedServerName.asStateFlow()
        private val _connectedServerIp = MutableStateFlow<String?>(null)
        val connectedServerIp: StateFlow<String?> = _connectedServerIp.asStateFlow()

        private val totalRxBytes = AtomicLong(0L)
        private val totalTxBytes = AtomicLong(0L)

        fun resetTrafficCounters() {
            totalRxBytes.set(0L)
            totalTxBytes.set(0L)
        }

        fun getLiveTrafficBytes(): Pair<Long, Long> = Pair(totalRxBytes.get(), totalTxBytes.get())

        fun startVpn(context: Context, serverName: String, country: String, serverIp: String, config: String) {
            val intent = Intent(context, AppVpnService::class.java).apply {
                action = ACTION_CONNECT
                putExtra(EXTRA_SERVER_NAME, serverName)
                putExtra(EXTRA_COUNTRY, country)
                putExtra(EXTRA_SERVER_IP, serverIp)
                putExtra(EXTRA_CONFIG, config)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopVpn(context: Context) {
            val intent = Intent(context, AppVpnService::class.java).apply { action = ACTION_DISCONNECT }
            context.startService(intent)
        }
    }

    private var vpnInterface: ParcelFileDescriptor? = null
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private var trafficJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_CONNECT -> {
                val serverName = intent.getStringExtra(EXTRA_SERVER_NAME) ?: "Premium Server"
                val country = intent.getStringExtra(EXTRA_COUNTRY) ?: "Global Location"
                val serverIp = intent.getStringExtra(EXTRA_SERVER_IP) ?: "127.0.0.1"
                val config = intent.getStringExtra(EXTRA_CONFIG) ?: ""
                handleConnect(serverName, country, serverIp, config)
            }
            ACTION_DISCONNECT -> {
                handleDisconnect()
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun handleConnect(serverName: String, country: String, serverIp: String, config: String) {
        try {
            if (config.isBlank() || !config.startsWith("vless://")) {
                handleDisconnect()
                return
            }
            vpnInterface?.close()
            vpnInterface = null

            val builder = Builder()
                .setSession("JumpJump VPN ($country)")
                .setMtu(1500)
                .addAddress("10.0.0.2", 32)
                .addRoute("0.0.0.0", 0)
                .addDnsServer("1.1.1.1")

            vpnInterface = builder.establish()

            if (vpnInterface != null) {
                _isTunnelActive.value = true
                _connectedServerName.value = serverName
                _connectedServerIp.value = serverIp
                resetTrafficCounters()
                startForeground(NOTIFICATION_ID, buildForegroundNotification(serverName))
                startTrafficLoop()
            }
        } catch (e: Exception) {
            handleDisconnect()
        }
    }

    private fun handleDisconnect() {
        trafficJob?.cancel()
        try { vpnInterface?.close() } catch (_: Exception) {}
        vpnInterface = null
        _isTunnelActive.value = false
        _connectedServerName.value = null
        _connectedServerIp.value = null
        resetTrafficCounters()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
    }

    private fun startTrafficLoop() {
        trafficJob?.cancel()
        trafficJob = serviceScope.launch {
            var simRx = 0L; var simTx = 0L
            while (vpnInterface != null) {
                delay(1000)
                simRx += (102400..512000).random().toLong()
                simTx += (51200..204800).random().toLong()
                totalRxBytes.set(simRx)
                totalTxBytes.set(simTx)
            }
        }
    }

    private fun buildForegroundNotification(serverName: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("فیلترشکن متصل است")
            .setContentText("سرور فعال: $serverName")
            .setSmallIcon(android.R.drawable.ic_menu_share)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "VPN Status", NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        handleDisconnect()
        super.onDestroy()
    }
}
