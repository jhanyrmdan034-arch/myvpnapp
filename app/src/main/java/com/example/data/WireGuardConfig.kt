package com.example.data

import android.util.Log

/**
 * Robust WireGuard configuration parser and data model.
 * Parses standard WireGuard [Interface] and [Peer] configuration files.
 */
data class WireGuardConfig(
    val privateKey: String,
    val address: String,          // e.g. "10.7.0.2"
    val prefix: Int = 24,         // CIDR prefix, e.g. 24 or 32
    val dnsList: List<String> = listOf("1.1.1.1", "8.8.8.8"),
    val mtu: Int = 1420,
    val publicKey: String,
    val endpointHost: String,     // Server IP or Hostname, e.g. "198.51.100.1"
    val endpointPort: Int = 51820,
    val allowedIps: List<String> = listOf("0.0.0.0/0"),
    val persistentKeepalive: Int = 25
) {
    val endpoint: String get() = "$endpointHost:$endpointPort"

    val isValid: Boolean
        get() = endpointHost.isNotBlank() &&
                endpointPort in 1..65535 &&
                privateKey.isNotBlank() &&
                publicKey.isNotBlank()

    companion object {
        private const val TAG = "WireGuardConfig"

        /**
         * Parses standard WireGuard configuration text format.
         */
        fun parse(rawConfig: String): WireGuardConfig? {
            if (rawConfig.isBlank()) return null

            var privateKey = ""
            var address = "10.7.0.2"
            var prefix = 24
            val dnsList = mutableListOf<String>()
            var mtu = 1420
            var publicKey = ""
            var endpointHost = ""
            var endpointPort = 51820
            val allowedIps = mutableListOf<String>()
            var persistentKeepalive = 25

            try {
                for (line in rawConfig.lines()) {
                    val trimmed = line.trim()
                    if (trimmed.isEmpty() || trimmed.startsWith("#") || trimmed.startsWith(";")) {
                        continue
                    }

                    if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
                        continue
                    }

                    val equalsIdx = trimmed.indexOf('=')
                    if (equalsIdx == -1) continue

                    val key = trimmed.substring(0, equalsIdx).trim().lowercase()
                    val value = trimmed.substring(equalsIdx + 1).trim()

                    when (key) {
                        "privatekey" -> {
                            privateKey = value
                        }
                        "address" -> {
                            // Can be "10.7.0.2/24" or comma-separated list
                            val firstAddr = value.split(",").firstOrNull()?.trim() ?: value
                            if (firstAddr.contains("/")) {
                                address = firstAddr.substringBefore("/").trim()
                                prefix = firstAddr.substringAfter("/").trim().toIntOrNull() ?: 24
                            } else {
                                address = firstAddr
                                prefix = 32
                            }
                        }
                        "dns" -> {
                            val items = value.split(",").map { it.trim() }.filter { it.isNotBlank() }
                            dnsList.addAll(items)
                        }
                        "mtu" -> {
                            mtu = value.toIntOrNull() ?: 1420
                        }
                        "publickey" -> {
                            publicKey = value
                        }
                        "endpoint" -> {
                            // Can be "host:port", "1.2.3.4:51820", or "[2001:db8::1]:51820"
                            if (value.startsWith("[") && value.contains("]:")) {
                                endpointHost = value.substringAfter("[").substringBefore("]")
                                endpointPort = value.substringAfter("]:").toIntOrNull() ?: 51820
                            } else if (value.contains(":")) {
                                endpointHost = value.substringBeforeLast(":").trim()
                                endpointPort = value.substringAfterLast(":").trim().toIntOrNull() ?: 51820
                            } else {
                                endpointHost = value
                                endpointPort = 51820
                            }
                        }
                        "allowedips" -> {
                            val items = value.split(",").map { it.trim() }.filter { it.isNotBlank() }
                            allowedIps.addAll(items)
                        }
                        "persistentkeepalive" -> {
                            persistentKeepalive = value.toIntOrNull() ?: 25
                        }
                    }
                }

                if (dnsList.isEmpty()) {
                    dnsList.add("1.1.1.1")
                    dnsList.add("8.8.8.8")
                }
                if (allowedIps.isEmpty()) {
                    allowedIps.add("0.0.0.0/0")
                }

                return WireGuardConfig(
                    privateKey = privateKey,
                    address = address,
                    prefix = prefix,
                    dnsList = dnsList,
                    mtu = mtu,
                    publicKey = publicKey,
                    endpointHost = endpointHost,
                    endpointPort = endpointPort,
                    allowedIps = allowedIps,
                    persistentKeepalive = persistentKeepalive
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing WireGuard config", e)
                return null
            }
        }
    }
}
