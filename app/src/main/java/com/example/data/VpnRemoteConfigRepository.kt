package com.example.data

import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.URLDecoder
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class RemoteConfigException(message: String, cause: Throwable? = null) : Exception(message, cause)

class VpnRemoteConfigRepository(
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
) {
    companion object {
        private const val TAG = "VpnRemoteConfig"
        
        // Live subscription link as specified by user
        const val PRIMARY_SUBSCRIPTION_URL = "https://galexystore.ir"
        const val SECONDARY_SUBSCRIPTION_URL = "https://acha-sub.galexystore.ir/sub/djMsMTcxMjIzLDE3ODk0OTAzODc768e5b4bc0"
        const val DEFAULT_SUBSCRIPTION_URL = PRIMARY_SUBSCRIPTION_URL
        const val PLACEHOLDER_GITHUB_URL = "https://githubusercontent.com"
        const val DEFAULT_GITHUB_RAW_URL = "https://raw.githubusercontent.com/vectavpn/anti-censorship-configs/main/servers.json"

        // Country name, Persian name, and 2-letter ISO code lookup map for flag emojis
        private val FLAGS_MAP = mapOf(
            "🇦🇹" to Triple("Austria", "اتریش", "AT"),
            "🇩🇪" to Triple("Germany", "آلمان", "DE"),
            "🇫🇮" to Triple("Finland", "فنلاند", "FI"),
            "🇳🇱" to Triple("Netherlands", "هلند", "NL"),
            "🇸🇪" to Triple("Sweden", "سوئد", "SE"),
            "🇨🇭" to Triple("Switzerland", "سوئیس", "CH"),
            "🇮🇹" to Triple("Italy", "ایتالیا", "IT"),
            "🇺🇦" to Triple("Ukraine", "اوکراین", "UA"),
            "🇷🇺" to Triple("Russia", "روسیه", "RU"),
            "🇦🇱" to Triple("Albania", "آلبانی", "AL"),
            "🇬🇧" to Triple("United Kingdom", "انگلستان", "GB"),
            "🇹🇷" to Triple("Turkey", "ترکیه", "TR"),
            "🇦🇪" to Triple("United Arab Emirates", "امارات", "AE"),
            "🇫🇷" to Triple("France", "فرانسه", "FR"),
            "🇺🇸" to Triple("United States", "ایالات متحده", "US"),
            "🇸🇬" to Triple("Singapore", "سنگاپور", "SG"),
            "🇳🇬" to Triple("Nigeria", "نیجریه", "NG"),
            "🇯🇵" to Triple("Japan", "ژاپن", "JP"),
            "🇮🇳" to Triple("India", "هند", "IN"),
            "🇨🇦" to Triple("Canada", "کانادا", "CA"),
            "🇪🇸" to Triple("Spain", "اسپانیا", "ES")
        )

        private val COUNTRY_WORDS_MAP = mapOf(
            "austria" to Triple("Austria", "اتریش", "AT"),
            "at" to Triple("Austria", "اتریش", "AT"),
            "germany" to Triple("Germany", "آلمان", "DE"),
            "deutschland" to Triple("Germany", "آلمان", "DE"),
            "de" to Triple("Germany", "آلمان", "DE"),
            "de1" to Triple("Germany", "آلمان", "DE"),
            "de2" to Triple("Germany", "آلمان", "DE"),
            "de3" to Triple("Germany", "آلمان", "DE"),
            "de4" to Triple("Germany", "آلمان", "DE"),
            "finland" to Triple("Finland", "فنلاند", "FI"),
            "fl" to Triple("Finland", "فنلاند", "FI"),
            "fi" to Triple("Finland", "فنلاند", "FI"),
            "netherlands" to Triple("Netherlands", "هلند", "NL"),
            "holland" to Triple("Netherlands", "هلند", "NL"),
            "nl" to Triple("Netherlands", "هلند", "NL"),
            "sweden" to Triple("Sweden", "سوئد", "SE"),
            "se" to Triple("Sweden", "سوئد", "SE"),
            "switzerland" to Triple("Switzerland", "سوئیس", "CH"),
            "swiss" to Triple("Switzerland", "سوئیس", "CH"),
            "ch" to Triple("Switzerland", "سوئیس", "CH"),
            "italy" to Triple("Italy", "ایتالیا", "IT"),
            "it" to Triple("Italy", "ایتالیا", "IT"),
            "ukraine" to Triple("Ukraine", "اوکراین", "UA"),
            "ua" to Triple("Ukraine", "اوکراین", "UA"),
            "russia" to Triple("Russia", "روسیه", "RU"),
            "ru" to Triple("Russia", "روسیه", "RU"),
            "albania" to Triple("Albania", "آلبانی", "AL"),
            "al" to Triple("Albania", "آلبانی", "AL"),
            "unitedkingdom" to Triple("United Kingdom", "انگلستان", "GB"),
            "uk" to Triple("United Kingdom", "انگلستان", "GB"),
            "gb" to Triple("United Kingdom", "انگلستان", "GB"),
            "england" to Triple("United Kingdom", "انگلستان", "GB"),
            "turkey" to Triple("Turkey", "ترکیه", "TR"),
            "tr" to Triple("Turkey", "ترکیه", "TR"),
            "uae" to Triple("United Arab Emirates", "امارات", "AE"),
            "ae" to Triple("United Arab Emirates", "امارات", "AE"),
            "emirates" to Triple("United Arab Emirates", "امارات", "AE"),
            "france" to Triple("France", "فرانسه", "FR"),
            "fr" to Triple("France", "فرانسه", "FR"),
            "unitedstates" to Triple("United States", "ایالات متحده", "US"),
            "us" to Triple("United States", "ایالات متحده", "US"),
            "usa" to Triple("United States", "ایالات متحده", "US"),
            "singapore" to Triple("Singapore", "سنگاپور", "SG"),
            "sg" to Triple("Singapore", "سنگاپور", "SG"),
            "nigeria" to Triple("Nigeria", "نیجریه", "NG"),
            "ng" to Triple("Nigeria", "نیجریه", "NG"),
            "japan" to Triple("Japan", "ژاپن", "JP"),
            "jp" to Triple("Japan", "ژاپن", "JP"),
            "india" to Triple("India", "هند", "IN"),
            "in" to Triple("India", "هند", "IN"),
            "canada" to Triple("Canada", "کانادا", "CA"),
            "ca" to Triple("Canada", "کانادا", "CA"),
            "spain" to Triple("Spain", "اسپانیا", "ES"),
            "es" to Triple("Spain", "اسپانیا", "ES")
        )
    }

    /**
     * Executes an HTTP GET request to the exact subscription link:
     * "https://acha-sub.galexystore.ir/sub/djMsMTcxMjIzLDE3ODk0OTAzODc768e5b4bc0"
     * Parses and decodes Base64 VLESS/VMESS/Shadowsocks configs, extracting unique country locations.
     */
    suspend fun fetchSubscriptionServers(subscriptionUrl: String = DEFAULT_SUBSCRIPTION_URL): List<VpnServer> = withContext(Dispatchers.IO) {
        Log.d(TAG, "Executing HTTP GET to subscription link: $subscriptionUrl")
        try {
            return@withContext executeFetchAndParse(subscriptionUrl)
        } catch (e: Exception) {
            if (subscriptionUrl == PRIMARY_SUBSCRIPTION_URL) {
                Log.w(TAG, "Primary subscription URL ($PRIMARY_SUBSCRIPTION_URL) failed: ${e.message}. Retrying with backup ($SECONDARY_SUBSCRIPTION_URL)...")
                try {
                    return@withContext executeFetchAndParse(SECONDARY_SUBSCRIPTION_URL)
                } catch (e2: Exception) {
                    Log.e(TAG, "Backup subscription URL also failed: ${e2.message}")
                    throw e2
                }
            } else {
                throw e
            }
        }
    }

    private fun executeFetchAndParse(url: String): List<VpnServer> {
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "v2rayNG/1.8.5 (Android; Mobile)")
            .header("Accept", "*/*")
            .build()

        val response = client.newCall(request).execute()
        response.use { resp ->
            if (!resp.isSuccessful) {
                Log.w(TAG, "Subscription link $url returned HTTP status ${resp.code}")
                throw RemoteConfigException("Subscription service returned HTTP ${resp.code}")
            }
            val body = resp.body?.string()?.trim()
            if (body.isNullOrBlank()) {
                throw RemoteConfigException("Received empty payload from subscription link.")
            }
            val parsed = parseSubscriptionPayload(body)
            if (parsed.isEmpty()) {
                throw RemoteConfigException("Subscription payload contained no valid locations.")
            }
            return parsed
        }
    }

    /**
     * Parses and decodes Base64 or plain text subscription responses containing VLESS/VMESS/Shadowsocks.
     * Extracts unique country/location names from the text after "#" in each config.
     */
    fun parseSubscriptionPayload(payload: String): List<VpnServer> {
        val trimmed = payload.trim()
        val effectiveContent = if (trimmed.contains("://")) {
            trimmed
        } else {
            try {
                val bytes = Base64.decode(trimmed, Base64.DEFAULT)
                String(bytes, Charsets.UTF_8)
            } catch (_: Exception) {
                try {
                    val bytes = Base64.decode(trimmed, Base64.URL_SAFE)
                    String(bytes, Charsets.UTF_8)
                } catch (_: Exception) {
                    trimmed
                }
            }
        }

        val lines = effectiveContent.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("//") }

        val uniqueLocations = LinkedHashMap<String, VpnServer>()
        var nodeIndex = 1

        for (line in lines) {
            if (!line.contains("://")) continue

            val protocol = when {
                line.startsWith("vless://", ignoreCase = true) -> "Xray (VLESS)"
                line.startsWith("vmess://", ignoreCase = true) -> "V2Ray (VMess)"
                line.startsWith("ss://", ignoreCase = true) -> "Shadowsocks"
                line.startsWith("trojan://", ignoreCase = true) -> "Trojan"
                line.startsWith("wireguard://", ignoreCase = true) -> "WireGuard"
                else -> "VLESS"
            }

            // Extract the location/country names from the end of each config string (text after "#")
            val rawTag = if (line.contains("#")) line.substringAfterLast("#").trim() else ""
            val decodedTag = try {
                URLDecoder.decode(rawTag, "UTF-8").trim()
            } catch (_: Exception) {
                rawTag
            }

            // Skip quota/account traffic notices (e.g. gift, حجم باقی مانده, GB)
            if (decodedTag.contains("حجم") || 
                decodedTag.contains("باقی مانده") || 
                decodedTag.contains("gift", ignoreCase = true) || 
                (decodedTag.contains("GB", ignoreCase = true) && decodedTag.contains(":"))
            ) {
                continue
            }

            val (countryName, countryNameFa, countryCode) = resolveCountryInfo(decodedTag)
            val hostOrIp = extractHostFromConfig(line) ?: "104.28.${nodeIndex}.${10 + nodeIndex}"

            // Populate unique country names so user can choose before hitting connect
            if (!uniqueLocations.containsKey(countryName)) {
                val serverId = "sub_loc_${countryCode.lowercase()}_$nodeIndex"
                val server = VpnServer(
                    id = serverId,
                    name = countryName,
                    nameFa = countryNameFa,
                    countryCode = countryCode,
                    pingMs = 70 + (nodeIndex * 5) % 80,
                    ipAddress = hostOrIp,
                    isFastest = uniqueLocations.isEmpty(),
                    isFree = true,
                    protocol = protocol,
                    port = 443,
                    configProfile = line
                )
                uniqueLocations[countryName] = server
                nodeIndex++
            }
        }

        if (uniqueLocations.isEmpty()) {
            throw RemoteConfigException("No valid server locations found in subscription payload.")
        }

        return uniqueLocations.values.toList()
    }

    private fun resolveCountryInfo(tag: String): Triple<String, String, String> {
        if (tag.isBlank()) {
            return Triple("United States", "ایالات متحده", "US")
        }

        // 1. Check flag emojis in the tag
        for ((emoji, info) in FLAGS_MAP) {
            if (tag.contains(emoji)) {
                return info
            }
        }

        // 2a. Check full country names first (length >= 3, e.g. "unitedstates", "germany", "austria")
        val lowerTag = tag.lowercase()
        for ((keyword, info) in COUNTRY_WORDS_MAP) {
            if (keyword.length >= 3 && lowerTag.contains(keyword)) {
                return info
            }
        }

        // 2b. Check 2-letter ISO codes matching isolated word tokens (e.g. "AT", "DE", "US")
        val tokens = Regex("[a-zA-Z0-9]+").findAll(lowerTag).map { it.value }.toSet()
        for ((keyword, info) in COUNTRY_WORDS_MAP) {
            if (keyword.length <= 2 && tokens.contains(keyword)) {
                return info
            }
        }

        // 3. Fallback: clean the string after # and use it directly as the country name
        val cleanTag = tag.replace(Regex("^[\\W_]+|[\\W_]+$"), "").trim()
        val displayName = if (cleanTag.isNotBlank()) cleanTag else tag
        return Triple(displayName, displayName, "US")
    }

    private fun extractHostFromConfig(config: String): String? {
        return try {
            if (config.contains("@") && config.contains(":")) {
                val afterAt = config.substringAfter("@")
                afterAt.substringBefore(":").substringBefore("/").substringBefore("?").trim()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun fetchLatestServers(customUrl: String? = null): List<VpnServer> = withContext(Dispatchers.IO) {
        // Try subscription link first, fallback to GitHub RAW if needed
        try {
            return@withContext fetchSubscriptionServers(DEFAULT_SUBSCRIPTION_URL)
        } catch (e: Exception) {
            Log.w(TAG, "fetchSubscriptionServers fallback: ${e.message}")
        }

        val targetUrl = customUrl ?: DEFAULT_GITHUB_RAW_URL
        val request = Request.Builder()
            .url(targetUrl)
            .header("User-Agent", "VectaVPN-Android/1.0")
            .build()

        try {
            val response = client.newCall(request).execute()
            response.use { resp ->
                val bodyString = resp.body?.string()?.trim() ?: ""
                return@withContext parseSubscriptionPayload(bodyString)
            }
        } catch (e: Exception) {
            throw RemoteConfigException("Connection failed. Please check your internet connection.", e)
        }
    }
}

