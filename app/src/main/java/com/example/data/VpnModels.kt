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

data class ServerModel(
    val server_name: String,
    val country: String,
    val country_code: String,
    val flag: String = "",
    val config: String,
    val pingMs: Int = 45,
    val isFastest: Boolean = false,
    val isFree: Boolean = true
) {
    val id: String get() = server_name
    val name: String get() = server_name.ifEmpty { country }
    val countryCode: String get() = country_code
    val nameFa: String get() = country
    val protocol: String get() = "VLESS"
    val icon: String get() = flag.ifBlank { countryCodeToEmoji(country_code) }
    val port: Int get() = extractPortFromVless(config)
    val configProfile: String get() = config
    val ipAddress: String get() = extractHostFromVless(config)
    val endpointHost: String get() = ipAddress

    companion object {
        fun countryCodeToEmoji(countryCode: String): String {
            val clean = countryCode.trim().uppercase()
            if (clean.length != 2) return "🌐"
            return try {
                val first = Character.codePointAt(clean, 0) - 0x41 + 0x1F1E6
                val second = Character.codePointAt(clean, 1) - 0x41 + 0x1F1E6
                String(Character.toChars(first)) + String(Character.toChars(second))
            } catch (_: Exception) {
                "🌐"
            }
        }

        fun extractHostFromVless(vlessUri: String): String {
            return try {
                if (vlessUri.startsWith("vless://")) {
                    val afterAt = vlessUri.substringAfter("@")
                    val hostPort = afterAt.substringBefore("?")
                    hostPort.substringBefore(":")
                } else {
                    "127.0.0.1"
                }
            } catch (_: Exception) {
                "127.0.0.1"
            }
        }

        fun extractPortFromVless(vlessUri: String): String {
            return try {
                if (vlessUri.startsWith("vless://")) {
                    val afterAt = vlessUri.substringAfter("@")
                    val hostPort = afterAt.substringBefore("?")
                    if (hostPort.contains(":")) hostPort.substringAfter(":").toInt() else 443
                } else {
                    443
                }
            } catch (_: Exception) {
                443
            }
        }
    }
}

typealias VpnServer = ServerModel

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
        Language("ar", "Arabic", "العربية", "SA", "SZ", isRtl = true),
        Language("tr", "Turkish", "Türkçe", "TR", "🇹🇷"),
        Language("fr", "French", "Français", "FR", "🇫🇷"),
        Language("de", "German", "Deutsch", "DE", "🇩🇪"),
        Language("it", "Italian", "Italiano", "IT", "🇮🇹"),
        Language("ru", "Russian", "Русский", "RU", "🇷🇺"),
        Language("ja", "Japanese", "日本語", "JP", "🇯🇵")
    )

    val servers: List<ServerModel> = emptyList()
}
