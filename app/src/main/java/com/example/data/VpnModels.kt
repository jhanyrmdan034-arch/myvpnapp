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
    val config: String,
    val icon: String = countryCodeToEmoji(country_code),
    val pingMs: Int = 45,
    val isFastest: Boolean = false,
    val isFree: Boolean = true
) {
    val id: String get() = server_name
    val name: String get() = country.ifEmpty { server_name }
    val countryCode: String get() = country_code
    val nameFa: String get() = country
    val protocol: String get() = "WireGuard"
    val wireGuardConfig: WireGuardConfig? get() = WireGuardConfig.parse(config)
    val port: Int get() = wireGuardConfig?.endpointPort ?: 51820
    val configProfile: String get() = config
    val ipAddress: String get() = wireGuardConfig?.endpointHost?.ifBlank { null } ?: extractEndpointIp(config)
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

        fun extractEndpointIp(config: String): String {
            return try {
                val lines = config.lines()
                for (line in lines) {
                    val trimmed = line.trim()
                    if (trimmed.startsWith("Endpoint", ignoreCase = true) && trimmed.contains("=")) {
                        val endpoint = trimmed.substringAfter("=").trim()
                        return endpoint.substringBefore(":")
                    }
                }
                "10.7.0.2"
            } catch (_: Exception) {
                "10.7.0.2"
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
        Language("ar", "Arabic", "العربية", "SA", "🇸🇦", isRtl = true),
        Language("tr", "Turkish", "Türkçe", "TR", "🇹🇷"),
        Language("fr", "French", "Français", "FR", "🇫🇷"),
        Language("de", "German", "Deutsch", "DE", "🇩🇪"),
        Language("it", "Italian", "Italiano", "IT", "🇮🇹"),
        Language("ru", "Russian", "Русский", "RU", "🇷🇺"),
        Language("ja", "Japanese", "日本語", "JP", "🇯🇵")
    )

    // Strictly empty list - NO fake or mock servers
    val servers: List<ServerModel> = emptyList()
}
