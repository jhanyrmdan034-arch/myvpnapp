package com.example.data

enum class VpnStatus {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR
}

enum class BottomTab {
    HOME,
    SETTINGS
}

data class Language(
    val code: String,
    val name: String,
    val nativeName: String,
    val countryCode: String,
    val flagEmoji: String,
    val isRtl: Boolean = false
)

data class VpnServer(
    val id: String,
    val name: String,
    val countryCode: String,
    val pingMs: Int,
    val ipAddress: String,
    val isFastest: Boolean = false,
    val isFree: Boolean = true,
    val nameFa: String = name,
    val protocol: String = "WireGuard",
    val port: Int = 51820,
    val configProfile: String? = null
)

data class VpnStats(
    val durationSeconds: Long = 0L,
    val remainingLimitSeconds: Long = 5400L, // 1 hour 30 mins session limit
    val downloadSpeedMbps: Float = 0f,
    val uploadSpeedMbps: Float = 0f,
    val bytesReceivedMb: Float = 0f,
    val bytesSentMb: Float = 0f,
    val virtualIp: String = ""
)

object DefaultData {
    val languages = listOf(
        Language("en", "English", "English (US)", "US", "🇺🇸"),
        Language("fa", "Persian", "فارسی (Iran)", "IR", "🇮🇷", isRtl = true),
        Language("es", "Spanish", "Español", "ES", "🇪🇸"),
        Language("ar", "Arabic", "العربية", "SA", "🇸🇦", isRtl = true),
        Language("tr", "Turkish", "Türkçe", "TR", "🇹🇷"),
        Language("fr", "French", "Français", "FR", "🇫🇷"),
        Language("de", "German", "Deutsch", "DE", "🇩🇪"),
        Language("it", "Italian", "Italiano", "IT", "🇮🇹"),
        Language("ru", "Russian", "Русский", "RU", "🇷🇺"),
        Language("ja", "Japanese", "日本語", "JP", "🇯🇵")
    )

    val servers = listOf(
        VpnServer("usa_1", "United States", "US", 120, "104.28.19.42", isFastest = true, nameFa = "ایالات متحده"),
        VpnServer("de_1", "Germany", "DE", 85, "185.220.101.5", nameFa = "آلمان"),
        VpnServer("uk_1", "United Kingdom", "GB", 95, "194.187.249.20", nameFa = "انگلستان"),
        VpnServer("nl_1", "Netherlands", "NL", 110, "185.107.56.12", nameFa = "هلند"),
        VpnServer("fr_1", "France", "FR", 115, "51.15.23.11", nameFa = "فرانسه"),
        VpnServer("ca_1", "Canada", "CA", 145, "192.99.148.10", nameFa = "کانادا"),
        VpnServer("jp_1", "Japan", "JP", 210, "133.242.18.9", nameFa = "ژاپن"),
        VpnServer("sg_1", "Singapore", "SG", 180, "139.59.245.1", nameFa = "سنگاپور")
    )
}
