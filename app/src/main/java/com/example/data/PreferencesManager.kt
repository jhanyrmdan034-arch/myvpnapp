package com.example.data

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("jumpjump_vpn_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "key_onboarding_completed"
        private const val KEY_LANGUAGE = "key_language"
        private const val KEY_SERVER_ID = "key_server_id"
        private const val KEY_KILL_SWITCH = "key_kill_switch"
        private const val KEY_DNS_PROTECTION = "key_dns_protection"
        private const val KEY_PROTOCOL = "key_protocol"
    }

    var isOnboardingCompleted: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        set(value) = prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, value).apply()

    var selectedLanguageCode: String
        get() = prefs.getString(KEY_LANGUAGE, "en") ?: "en"
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()

    var selectedServerId: String
        get() = prefs.getString(KEY_SERVER_ID, "usa_1") ?: "usa_1"
        set(value) = prefs.edit().putString(KEY_SERVER_ID, value).apply()

    var killSwitchEnabled: Boolean
        get() = prefs.getBoolean(KEY_KILL_SWITCH, true)
        set(value) = prefs.edit().putBoolean(KEY_KILL_SWITCH, value).apply()

    var dnsProtectionEnabled: Boolean
        get() = prefs.getBoolean(KEY_DNS_PROTECTION, true)
        set(value) = prefs.edit().putBoolean(KEY_DNS_PROTECTION, value).apply()

    var vpnProtocol: String
        get() = prefs.getString(KEY_PROTOCOL, "WireGuard (Fastest)") ?: "WireGuard (Fastest)"
        set(value) = prefs.edit().putString(KEY_PROTOCOL, value).apply()

    fun resetOnboarding() {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, false).apply()
    }
}
