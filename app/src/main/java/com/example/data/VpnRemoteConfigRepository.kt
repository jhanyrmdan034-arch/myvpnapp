package com.example.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.util.concurrent.TimeUnit

class RemoteConfigException(message: String, cause: Throwable? = null) : Exception(message, cause)

class VpnRemoteConfigRepository(
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
) {
    companion object {
        private const val TAG = "VpnRemoteConfig"

        // Official GitHub raw endpoint for servers.json
        const val GITHUB_SERVERS_URL = "https://raw.githubusercontent.com/jhanyrmdan034-arch/myvpn/refs/heads/main/servers.json"
        const val GITHUB_BASE_URL = "https://githubusercontent.com"
    }

    /**
     * Executes HTTP GET request to GitHub to download and parse servers.json.
     * Extracts dynamic list of countries and WireGuard configurations.
     */
    suspend fun fetchServers(targetUrl: String = GITHUB_SERVERS_URL): List<ServerModel> = withContext(Dispatchers.IO) {
        Log.i(TAG, "Fetching servers from GitHub URL: $targetUrl")
        val request = Request.Builder()
            .url(targetUrl)
            .header("User-Agent", "JumpJumpVPN-Android/1.0")
            .header("Accept", "application/json, text/plain, */*")
            .header("Cache-Control", "no-cache")
            .build()

        try {
            val response = client.newCall(request).execute()
            response.use { resp ->
                if (!resp.isSuccessful) {
                    Log.e(TAG, "GitHub servers endpoint returned HTTP ${resp.code}")
                    throw RemoteConfigException("خطا در دریافت سرورها (کد ${resp.code})")
                }
                val body = resp.body?.string()?.trim()
                if (body.isNullOrBlank()) {
                    throw RemoteConfigException("فایل سرورها از گیت‌هاب خالی است.")
                }
                val parsed = parseServersJson(body)
                if (parsed.isEmpty()) {
                    throw RemoteConfigException("هیچ سرور فعالی در فایل سرورها یافت نشد.")
                }
                Log.i(TAG, "Successfully parsed ${parsed.size} servers from GitHub.")
                return@withContext parsed
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to download servers from GitHub: ${e.message}", e)
            throw RemoteConfigException("اتصال به سرور برقرار نشد. لطفاً اینترنت خود را بررسی کنید.", e)
        }
    }

    /**
     * Parses the JSON array from GitHub:
     * [
     *   {
     *     "server_name": "United States - Server 1",
     *     "country": "United States",
     *     "country_code": "US",
     *     "config": "[Interface]\n..."
     *   }
     * ]
     */
    fun parseServersJson(jsonString: String): List<ServerModel> {
        val trimmed = jsonString.trim()
        val jsonArray = if (trimmed.startsWith("[")) {
            JSONArray(trimmed)
        } else if (trimmed.startsWith("{")) {
            // In case encapsulated in { "servers": [...] }
            val root = org.json.JSONObject(trimmed)
            root.optJSONArray("servers") ?: root.optJSONArray("data") ?: JSONArray()
        } else {
            throw RemoteConfigException("فرمت فایل سرورها نامعتبر است.")
        }

        val result = mutableListOf<ServerModel>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.optJSONObject(i) ?: continue
            val serverName = obj.optString("server_name", obj.optString("name", "Server ${i + 1}"))
            val country = obj.optString("country", "Unknown")
            val countryCode = obj.optString("country_code", obj.optString("code", "US")).trim().uppercase()
            val config = obj.optString("config", "")
            val icon = obj.optString("icon", obj.optString("flag", ServerModel.countryCodeToEmoji(countryCode)))

            if (config.isNotBlank()) {
                result.add(
                    ServerModel(
                        server_name = serverName,
                        country = country,
                        country_code = countryCode,
                        config = config,
                        icon = icon,
                        pingMs = 40 + (i * 12) % 70,
                        isFastest = (i == 0)
                    )
                )
            }
        }
        return result
    }

    // Compatibility method for any legacy callers
    suspend fun fetchSubscriptionServers(): List<ServerModel> = fetchServers()
    suspend fun fetchLatestServers(customUrl: String? = null): List<ServerModel> = fetchServers(customUrl ?: GITHUB_SERVERS_URL)
}
