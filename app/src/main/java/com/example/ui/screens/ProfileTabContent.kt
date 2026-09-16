package com.example.ui.screens

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
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppStrings
import com.example.data.DefaultData
import com.example.data.VpnStats
import com.example.ui.components.CountryFlag
import com.example.ui.components.JumpJumpShieldIcon
import com.example.ui.theme.VpnCardBorder
import com.example.ui.theme.VpnNeonGreen
import com.example.ui.theme.VpnSurface
import com.example.ui.theme.VpnSurfaceElevated
import com.example.ui.theme.VpnTextMuted
import com.example.ui.theme.VpnTextPrimary
import com.example.ui.theme.VpnTextSecondary

@Composable
fun ProfileTabContent(
    stats: VpnStats,
    langCode: String,
    onChangeLanguageClick: () -> Unit,
    onViewPrivacyClick: () -> Unit,
    onResetOnboardingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val currentLang = DefaultData.languages.find { it.code == langCode } ?: DefaultData.languages.first()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("profile_tab_content"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = AppStrings.get("tab_profile", langCode),
            color = VpnTextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // User Account Header Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(VpnSurface)
                .border(1.dp, VpnCardBorder, RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(VpnSurfaceElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "User",
                        tint = VpnNeonGreen,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "VectaVPN Member",
                        color = VpnTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Pro Protection • Unlimited Bandwidth",
                        color = VpnTextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Live Traffic Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(VpnSurface)
                    .border(1.dp, VpnCardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = AppStrings.get("download_speed", langCode),
                        color = VpnTextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "%.1f MB".format(stats.bytesReceivedMb),
                        color = VpnNeonGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(VpnSurface)
                    .border(1.dp, VpnCardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = AppStrings.get("upload_speed", langCode),
                        color = VpnTextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "%.1f MB".format(stats.bytesSentMb),
                        color = VpnTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Options List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(VpnSurface)
                .border(1.dp, VpnCardBorder, RoundedCornerShape(20.dp))
                .padding(vertical = 6.dp)
        ) {
            ProfileMenuRow(
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
                testTag = "profile_change_language"
            )

            ProfileMenuRow(
                icon = Icons.Outlined.Policy,
                title = AppStrings.get("privacy_title", langCode),
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = VpnTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                },
                onClick = onViewPrivacyClick,
                testTag = "profile_view_privacy"
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Version & Attribution
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            JumpJumpShieldIcon(size = 42)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "VectaVPN v1.2.0",
                color = VpnTextMuted,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "ChaCha20-Poly1305 • Zero-Log Protected",
                color = VpnTextMuted.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun ProfileMenuRow(
    icon: ImageVector,
    title: String,
    trailingContent: @Composable () -> Unit,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = VpnNeonGreen,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = title,
                color = VpnTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }

        trailingContent()
    }
}
