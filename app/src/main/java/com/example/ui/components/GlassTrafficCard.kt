package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppStrings
import com.example.data.VpnStats
import com.example.data.VpnStatus
import com.example.ui.theme.VpnNeonGreen
import com.example.ui.theme.VpnTextMuted
import com.example.ui.theme.VpnTextPrimary
import com.example.ui.theme.VpnTextSecondary
import java.util.Locale

@Composable
fun GlassTrafficCard(
    stats: VpnStats,
    status: VpnStatus,
    langCode: String,
    modifier: Modifier = Modifier
) {
    val isConnected = status == VpnStatus.CONNECTED

    val downloadSpeedStr = if (isConnected) {
        String.format(Locale.US, "%.1f MB/s", stats.downloadSpeedMbps)
    } else {
        "0.0 MB/s"
    }

    val uploadSpeedStr = if (isConnected) {
        String.format(Locale.US, "%.1f MB/s", stats.uploadSpeedMbps)
    } else {
        "0.0 MB/s"
    }

    val downloadTotalStr = if (isConnected || stats.bytesReceivedMb > 0f) {
        String.format(Locale.US, "%.1f MB", stats.bytesReceivedMb)
    } else {
        "0.0 MB"
    }

    val uploadTotalStr = if (isConnected || stats.bytesSentMb > 0f) {
        String.format(Locale.US, "%.1f MB", stats.bytesSentMb)
    } else {
        "0.0 MB"
    }

    // Glassmorphism Container
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = if (isConnected) VpnNeonGreen.copy(alpha = 0.25f) else Color.Black.copy(alpha = 0.6f)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF232C42).copy(alpha = 0.65f),
                        Color(0xFF131927).copy(alpha = 0.80f)
                    )
                )
            )
            .border(
                width = 1.2.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.28f),
                        Color.White.copy(alpha = 0.06f),
                        Color.White.copy(alpha = 0.12f)
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(vertical = 16.dp, horizontal = 20.dp)
            .testTag("glass_traffic_card")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Bar: Traffic Monitoring & Live Status Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Speed,
                        contentDescription = "Traffic",
                        tint = if (isConnected) VpnNeonGreen else VpnTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (langCode == "fa") "سرعت و ترافیک شبکه" else "Network Traffic",
                        color = VpnTextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Live status badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isConnected) VpnNeonGreen.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.06f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(if (isConnected) VpnNeonGreen else Color(0xFF708096))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isConnected) AppStrings.get("live", langCode) else AppStrings.get("idle", langCode),
                        color = if (isConnected) VpnNeonGreen else VpnTextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Traffic Stats: Download on left, Upload on right, frosted divider in center
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Download Column
                TrafficMetricItem(
                    icon = Icons.Outlined.ArrowDownward,
                    iconTint = VpnNeonGreen,
                    iconBg = VpnNeonGreen.copy(alpha = 0.15f),
                    label = AppStrings.get("download_speed", langCode),
                    speed = downloadSpeedStr,
                    total = downloadTotalStr,
                    totalLabel = AppStrings.get("total_in", langCode),
                    modifier = Modifier.weight(1f)
                )

                // Frosted Vertical Glass Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(52.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Upload Column
                TrafficMetricItem(
                    icon = Icons.Outlined.ArrowUpward,
                    iconTint = Color(0xFF00E5FF),
                    iconBg = Color(0xFF00E5FF).copy(alpha = 0.15f),
                    label = AppStrings.get("upload_speed", langCode),
                    speed = uploadSpeedStr,
                    total = uploadTotalStr,
                    totalLabel = AppStrings.get("total_out", langCode),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun TrafficMetricItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    iconBg: Color,
    label: String,
    speed: String,
    total: String,
    totalLabel: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                color = VpnTextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Large Speed Value
        Text(
            text = speed,
            color = VpnTextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(3.dp))

        // Total Transferred
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$totalLabel ",
                color = VpnTextMuted,
                fontSize = 11.sp
            )
            Text(
                text = total,
                color = VpnTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
