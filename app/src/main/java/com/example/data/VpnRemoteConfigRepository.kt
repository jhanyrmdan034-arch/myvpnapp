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
        const val GITHUB_SERVERS_URL = "https://githubusercontent.com"
    }

    suspend fun fetchServers(targetUrl: String = GITHUB_SERVERS_URL): List<ServerModel> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(targetUrl)
            .header("User-Agent", "Mozilla/5.0")
            .header("Cache-Control", "no-cache")
            .build()

        try {
            val response = client.newCall(request).execute()
            response.use { resp ->
                if (!resp.isSuccessful) {
                    throw RemoteConfigException("اتصال برقرار نشد.")
                }
                val body = resp.body?.string()?.trim()
                if (body.isNullOrBlank()) {
                    throw RemoteConfigException("اتصال برقرار نشد.")
                }
                return@withContext parseServersJson(body)
            }
        } catch (e: Exception) {
            throw RemoteConfigException("اتصال برقرار نشد.", e)
        }
    }

    fun parseServersJson(jsonString: String): List<ServerModel> {
        return try {
            val jsonArray = JSONArray(jsonString.trim())
            val result = mutableListOf<ServerModel>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.optJSONObject(i) ?: continue
                val serverName = obj.optString("server_name", "Premium Server")
                val country = obj.optString("country", "Global")
                val countryCode = obj.optString("country_code", "US").trim().uppercase()
                val config = obj.optString("config", "")
                val flag = obj.optString("flag", "🌐")

                if (config.isNotBlank()) {
                    result.add(
                        ServerModel(
                            server_name = serverName,
                            country = country,
                            country_code = countryCode,
                            flag = flag,
                            config = config,
                            pingMs = 45,
                            isFastest = (i == 0)
                        )
                    )
                }
            }
            result
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun fetchSubscriptionServers(): List<ServerModel> = fetchServers()
    suspend fun fetchLatestServers(customUrl: String? = null): List<ServerModel> = fetchServers(customUrl ?: GITHUB_SERVERS_URL)
}
