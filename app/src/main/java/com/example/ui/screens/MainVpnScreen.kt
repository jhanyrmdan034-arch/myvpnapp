package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppStrings
import com.example.data.BottomTab
import com.example.data.DefaultData
import com.example.data.Language
import com.example.data.VpnServer
import com.example.data.VpnStats
import com.example.data.VpnStatus
import com.example.ui.components.CountryFlag
import com.example.ui.components.GlassTrafficCard
import com.example.ui.components.GlowConnectionButton
import com.example.ui.components.JumpJumpBottomBar
import com.example.ui.components.JumpJumpHeader
import com.example.ui.components.LocationSelectionCard
import com.example.ui.components.SelectedServerPill
import com.example.ui.theme.VpnCardBorder
import com.example.ui.theme.VpnDarkBg
import com.example.ui.theme.VpnNeonGreen
import com.example.ui.theme.VpnSurface
import com.example.ui.theme.VpnSurfaceElevated
import com.example.ui.theme.VpnTextMuted
import com.example.ui.theme.VpnTextPrimary
import com.example.ui.theme.VpnTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainVpnScreen(
    currentTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    vpnStatus: VpnStatus,
    selectedServer: VpnServer,
    servers: List<VpnServer>,
    stats: VpnStats,
    langCode: String,
    killSwitchEnabled: Boolean,
    dnsProtectionEnabled: Boolean,
    selectedProtocol: String,
    onToggleConnect: () -> Unit,
    onSelectServer: (VpnServer) -> Unit,
    onKillSwitchToggle: (Boolean) -> Unit,
    onDnsToggle: (Boolean) -> Unit,
    onProtocolSelected: (String) -> Unit,
    onLanguageSelected: (Language) -> Unit,
    onResetOnboarding: () -> Unit,
    sessionExpiredNotice: Boolean = false,
    onDismissSessionExpiredNotice: () -> Unit = {},
    errorMessage: String? = null,
    onClearError: () -> Unit = {},
    onConnectOffline: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("main_vpn_screen"),
        bottomBar = {
            JumpJumpBottomBar(
                currentTab = currentTab,
                onTabSelected = onTabSelected,
                langCode = langCode
            )
        },
        containerColor = VpnDarkBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            // Top Header: Shield Logo + "JumpJump VPN" + Settings icon
            JumpJumpHeader(
                onSettingsClick = { onTabSelected(BottomTab.SETTINGS) }
            )

            when (currentTab) {
                BottomTab.HOME -> {
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 1. Big Digital Timer Section
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 12.dp)
                        ) {
                            val timeStr = if (vpnStatus == VpnStatus.CONNECTED) {
                                val remHours = stats.remainingLimitSeconds / 3600
                                val remMinutes = (stats.remainingLimitSeconds % 3600) / 60
                                val remSeconds = stats.remainingLimitSeconds % 60
                                "%02d:%02d:%02d".format(remHours, remMinutes, remSeconds)
                            } else {
                                "00:00:00"
                            }

                            Text(
                                text = timeStr,
                                color = if (vpnStatus == VpnStatus.CONNECTED) Color.White else VpnTextSecondary,
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            val statusLabel = when (vpnStatus) {
                                VpnStatus.CONNECTED -> if (langCode == "fa") "متصل شد" else "CONNECTED"
                                VpnStatus.CONNECTING -> if (langCode == "fa") "در حال اتصال..." else "CONNECTING..."
                                VpnStatus.DISCONNECTED -> if (langCode == "fa") "متصل نیست" else "DISCONNECTED"
                                VpnStatus.ERROR -> if (langCode == "fa") {
                                    "خطا در اتصال. لطفاً اینترنت خود را بررسی کنید یا سرور دیگری انتخاب نمایید"
                                } else {
                                    "Connection failed. Please check your internet connection."
                                }
                            }
                            Text(
                                text = statusLabel,
                                color = when (vpnStatus) {
                                    VpnStatus.CONNECTED -> Color(0xFF3A82F7)
                                    VpnStatus.ERROR -> Color(0xFFFF5252)
                                    else -> VpnTextMuted
                                },
                                fontSize = if (vpnStatus == VpnStatus.ERROR) 11.sp else 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = if (vpnStatus == VpnStatus.ERROR) 0.5.sp else 1.5.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }

                        // 2. Center Big Glowing Button (Our exact JumpJump capsule switch button - colors strictly kept)
                        GlowConnectionButton(
                            status = vpnStatus,
                            pingMs = selectedServer.pingMs,
                            langCode = langCode,
                            onToggleConnect = onToggleConnect
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // 3. Glassmorphic Upload & Download Card (directly under the connect button)
                        GlassTrafficCard(
                            stats = stats,
                            status = vpnStatus,
                            langCode = langCode
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // 4. Server Location Selection Card (at the bottom of the page)
                        LocationSelectionCard(
                            servers = servers,
                            selectedServerId = selectedServer.id,
                            langCode = langCode,
                            onSelectServer = onSelectServer
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                BottomTab.SETTINGS -> {
                    SettingsTabContent(
                        langCode = langCode,
                        killSwitchEnabled = killSwitchEnabled,
                        onKillSwitchToggle = onKillSwitchToggle,
                        dnsProtectionEnabled = dnsProtectionEnabled,
                        onDnsToggle = onDnsToggle,
                        selectedProtocol = selectedProtocol,
                        onProtocolSelected = onProtocolSelected,
                        onChangeLanguageClick = { showLanguageDialog = true },
                        onViewPrivacyClick = { showPrivacyDialog = true },
                        onResetOnboardingClick = onResetOnboarding
                    )
                }
            }
        }
    }

    // Language Selection Modal Dialog
    if (showLanguageDialog) {
        BasicAlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            modifier = Modifier.padding(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = VpnSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VpnCardBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppStrings.get("select_language_title", langCode),
                            color = VpnTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { showLanguageDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = VpnTextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    DefaultData.languages.forEach { lang ->
                        val isSelected = lang.code == langCode
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) VpnSurfaceElevated else Color.Transparent)
                                .clickable {
                                    onLanguageSelected(lang)
                                    showLanguageDialog = false
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CountryFlag(countryCode = lang.countryCode, size = 26.dp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "${lang.nativeName} (${lang.name})",
                                color = if (isSelected) VpnNeonGreen else VpnTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }

    // Privacy Policy Modal Dialog
    if (showPrivacyDialog) {
        BasicAlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            modifier = Modifier.padding(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = VpnSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VpnCardBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppStrings.get("privacy_title", langCode),
                            color = VpnTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { showPrivacyDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = VpnTextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .height(300.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = if (langCode == "fa") AppStrings.samplePrivacyPolicyPersian else AppStrings.samplePrivacyPolicyEnglish,
                            color = VpnTextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }

    // Settings Quick Menu Dialog
    if (showSettingsDialog) {
        BasicAlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            modifier = Modifier.padding(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = VpnSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, VpnCardBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppStrings.get("settings", langCode),
                            color = VpnTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { showSettingsDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = VpnTextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                showSettingsDialog = false
                                showLanguageDialog = true
                            }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppStrings.get("change_language", langCode),
                            color = VpnTextPrimary,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

        // 1.5-Hour Session Timeout Dialog
        if (sessionExpiredNotice) {
            val isFa = langCode == "fa"
            BasicAlertDialog(
                onDismissRequest = onDismissSessionExpiredNotice,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF1E2638))
                    .border(1.dp, Color(0xFF3A82F7).copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = null,
                        tint = Color(0xFF3A82F7),
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = if (isFa) "اتمام زمان اتصال (۱:۳۰ ساعت)" else "Session Limit Reached (1h 30m)",
                        color = VpnTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isFa)
                            "مدت زمان مجاز اتصال پیوسته (۱ ساعت و نیم) به پایان رسید و اتصال قطع شد. برای اتصال مجدد ضربه بزنید."
                        else
                            "Your 1.5-hour connection time limit was reached and the session ended. Tap reconnect to connect again.",
                        color = VpnTextSecondary,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF2B3448))
                                .clickable(onClick = onDismissSessionExpiredNotice)
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isFa) "بستن" else "Close",
                                color = VpnTextPrimary,
                                fontSize = 14.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF3A82F7))
                                .clickable {
                                    onDismissSessionExpiredNotice()
                                    onToggleConnect()
                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isFa) "اتصال مجدد" else "Reconnect",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
