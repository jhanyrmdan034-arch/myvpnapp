package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppStrings
import com.example.data.DefaultData
import com.example.ui.components.CountryFlag
import com.example.ui.theme.VpnCardBorder
import com.example.ui.theme.VpnNeonGreen
import com.example.ui.theme.VpnSurface
import com.example.ui.theme.VpnSurfaceElevated
import com.example.ui.theme.VpnTextMuted
import com.example.ui.theme.VpnTextPrimary
import com.example.ui.theme.VpnTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTabContent(
    langCode: String,
    killSwitchEnabled: Boolean,
    onKillSwitchToggle: (Boolean) -> Unit,
    dnsProtectionEnabled: Boolean,
    onDnsToggle: (Boolean) -> Unit,
    selectedProtocol: String,
    onProtocolSelected: (String) -> Unit,
    onChangeLanguageClick: () -> Unit,
    onViewPrivacyClick: () -> Unit,
    onResetOnboardingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val currentLang = DefaultData.languages.find { it.code == langCode } ?: DefaultData.languages.first()

    var showAboutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("settings_tab_content"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = AppStrings.get("settings", langCode),
            color = VpnTextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // Section: General & Communication
        Text(
            text = AppStrings.get("general_support", langCode),
            color = VpnTextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(VpnSurface)
                .border(1.dp, VpnCardBorder, RoundedCornerShape(20.dp))
                .padding(vertical = 4.dp)
        ) {
            // Share App
            SettingsMenuRow(
                icon = Icons.Outlined.Share,
                title = AppStrings.get("share_app", langCode),
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = VpnTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                },
                onClick = {
                    shareApp(context, langCode)
                },
                testTag = "settings_share_app"
            )

            // Contact Us
            SettingsMenuRow(
                icon = Icons.Outlined.Email,
                title = AppStrings.get("contact_us", langCode),
                subtitle = "jhanyrmdan034@gmail.com",
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = VpnTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                },
                onClick = {
                    sendEmailToSupport(context, langCode)
                },
                testTag = "settings_contact_us"
            )

            // About Us
            SettingsMenuRow(
                icon = Icons.Outlined.Info,
                title = AppStrings.get("about_us", langCode),
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = VpnTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                },
                onClick = { showAboutDialog = true },
                testTag = "settings_about_us"
            )
        }

        // Section: Security & Connection
        Text(
            text = AppStrings.get("security_connection", langCode),
            color = VpnTextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(VpnSurface)
                .border(1.dp, VpnCardBorder, RoundedCornerShape(20.dp))
                .padding(vertical = 4.dp)
        ) {
            // Kill Switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Security,
                        contentDescription = null,
                        tint = VpnTextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = AppStrings.get("kill_switch", langCode),
                            color = VpnTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = AppStrings.get("kill_switch_desc", langCode),
                            color = VpnTextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
                Switch(
                    checked = killSwitchEnabled,
                    onCheckedChange = onKillSwitchToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF3A82F7),
                        uncheckedThumbColor = VpnTextMuted,
                        uncheckedTrackColor = VpnSurfaceElevated
                    )
                )
            }

            // DNS Protection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Dns,
                        contentDescription = null,
                        tint = VpnTextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = AppStrings.get("dns_leak_protect", langCode),
                            color = VpnTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = AppStrings.get("dns_leak_desc", langCode),
                            color = VpnTextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
                Switch(
                    checked = dnsProtectionEnabled,
                    onCheckedChange = onDnsToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF3A82F7),
                        uncheckedThumbColor = VpnTextMuted,
                        uncheckedTrackColor = VpnSurfaceElevated
                    )
                )
            }
        }

        // Section: Preferences
        Text(
            text = AppStrings.get("preferences", langCode),
            color = VpnTextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(VpnSurface)
                .border(1.dp, VpnCardBorder, RoundedCornerShape(20.dp))
                .padding(vertical = 4.dp)
        ) {
            // Language
            SettingsMenuRow(
                icon = Icons.Outlined.Language,
                title = AppStrings.get("change_language", langCode),
                trailingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CountryFlag(countryCode = currentLang.countryCode, size = 22.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = currentLang.nativeName,
                            color = VpnTextSecondary,
                            fontSize = 13.sp
                        )
                    }
                },
                onClick = onChangeLanguageClick,
                testTag = "settings_language"
            )

            // Privacy Policy
            SettingsMenuRow(
                icon = Icons.Outlined.Policy,
                title = AppStrings.get("privacy_policy", langCode),
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = VpnTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                },
                onClick = onViewPrivacyClick,
                testTag = "settings_privacy"
            )
        }

        // App Footer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "VectaVPN v1.2.0",
                color = VpnTextMuted,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Zero-Log Protection • 256-bit Military Encryption",
                color = VpnTextMuted.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
    }

    // About Us Dialog
    if (showAboutDialog) {
        BasicAlertDialog(
            onDismissRequest = { showAboutDialog = false },
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(VpnSurface)
                .border(1.dp, VpnCardBorder, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3A82F7).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = Color(0xFF3A82F7),
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = AppStrings.get("about_title", langCode),
                    color = VpnTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = AppStrings.get("about_desc", langCode),
                    color = VpnTextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF3A82F7))
                        .clickable { showAboutDialog = false }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = AppStrings.get("got_it", langCode),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun sendEmailToSupport(context: Context, langCode: String) {
    val subject = AppStrings.get("email_subject", langCode)
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:jhanyrmdan034@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("jhanyrmdan034@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }
            context.startActivity(Intent.createChooser(intent, "Email"))
        } catch (e2: Exception) {
            android.widget.Toast.makeText(context, "jhanyrmdan034@gmail.com", android.widget.Toast.LENGTH_LONG).show()
        }
    }
}

private fun shareApp(context: Context, langCode: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, AppStrings.get("share_text", langCode))
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, AppStrings.get("share_app", langCode))
    context.startActivity(shareIntent)
}

@Composable
private fun SettingsMenuRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    trailingContent: @Composable () -> Unit = {},
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = VpnTextSecondary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    color = VpnTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        color = VpnTextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }
        trailingContent()
    }
}
