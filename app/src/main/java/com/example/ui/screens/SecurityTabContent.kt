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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.VpnLock
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.ui.theme.VpnCardBorder
import com.example.ui.theme.VpnDarkBg
import com.example.ui.theme.VpnNeonGreen
import com.example.ui.theme.VpnSurface
import com.example.ui.theme.VpnSurfaceElevated
import com.example.ui.theme.VpnTextMuted
import com.example.ui.theme.VpnTextPrimary
import com.example.ui.theme.VpnTextSecondary

@Composable
fun SecurityTabContent(
    killSwitchEnabled: Boolean,
    onKillSwitchToggle: (Boolean) -> Unit,
    dnsProtectionEnabled: Boolean,
    onDnsToggle: (Boolean) -> Unit,
    selectedProtocol: String,
    onProtocolSelected: (String) -> Unit,
    virtualIp: String,
    langCode: String,
    modifier: Modifier = Modifier
) {
    val protocols = listOf("WireGuard (Fastest)", "OpenVPN (UDP)", "OpenVPN (TCP)", "Shadowsocks")
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("security_tab_content"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = AppStrings.get("tab_security", langCode),
            color = VpnTextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // Status Card: Protected Connection
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(VpnSurface)
                .border(1.dp, VpnNeonGreen.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(VpnNeonGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Shield,
                        contentDescription = null,
                        tint = VpnNeonGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Shield Active",
                        color = VpnNeonGreen,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "IP Masked: $virtualIp",
                        color = VpnTextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Toggles Section
        SecurityToggleCard(
            title = AppStrings.get("kill_switch", langCode),
            subtitle = AppStrings.get("kill_switch_desc", langCode),
            icon = Icons.Outlined.PowerSettingsNew,
            isChecked = killSwitchEnabled,
            onCheckedChange = onKillSwitchToggle,
            testTag = "kill_switch_toggle"
        )

        SecurityToggleCard(
            title = AppStrings.get("dns_leak_protect", langCode),
            subtitle = AppStrings.get("dns_leak_desc", langCode),
            icon = Icons.Outlined.Dns,
            isChecked = dnsProtectionEnabled,
            onCheckedChange = onDnsToggle,
            testTag = "dns_toggle"
        )

        // Protocol Selector Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(VpnSurface)
                .border(1.dp, VpnCardBorder, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.VpnLock,
                    contentDescription = null,
                    tint = VpnNeonGreen,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = AppStrings.get("vpn_protocol", langCode),
                    color = VpnTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            protocols.forEach { protocol ->
                val isSelected = protocol == selectedProtocol
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) VpnSurfaceElevated else VpnDarkBg.copy(alpha = 0.5f))
                        .clickable { onProtocolSelected(protocol) }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = protocol,
                        color = if (isSelected) VpnTextPrimary else VpnTextSecondary,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = VpnNeonGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun SecurityToggleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VpnSurface)
            .border(1.dp, VpnCardBorder, RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(VpnSurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isChecked) VpnNeonGreen else VpnTextMuted,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    color = VpnTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = VpnTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = VpnDarkBg,
                checkedTrackColor = VpnNeonGreen,
                uncheckedThumbColor = VpnTextMuted,
                uncheckedTrackColor = VpnSurfaceElevated
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}
