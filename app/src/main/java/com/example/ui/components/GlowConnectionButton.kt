package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppStrings
import com.example.data.VpnStatus
import com.example.ui.theme.VpnCardBorder
import com.example.ui.theme.VpnPillBg
import com.example.ui.theme.VpnTextPrimary
import com.example.ui.theme.VpnTextSecondary

@Composable
fun GlowConnectionButton(
    status: VpnStatus,
    pingMs: Int,
    langCode: String,
    onToggleConnect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isConnected = status == VpnStatus.CONNECTED
    val isConnecting = status == VpnStatus.CONNECTING

    // JumpJump VPN exact colors
    val jumpJumpBlue = Color(0xFF3A82F7)
    val trackOffColor = Color(0xFF252834)
    val thumbOffColor = Color(0xFF444858)

    val trackWidth = 236.dp
    val trackHeight = 104.dp
    val thumbSize = 86.dp
    val padding = 9.dp
    val travelDistance = trackWidth - thumbSize - (padding * 2)

    // Smooth animated thumb sliding from left (0) to right (travelDistance)
    val thumbOffset by animateDpAsState(
        targetValue = if (isConnected) travelDistance else 0.dp,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "thumb_offset"
    )

    // Animated track background color: changes to JumpJump Blue when connected!
    val trackColor by animateColorAsState(
        targetValue = if (isConnected) jumpJumpBlue else trackOffColor,
        animationSpec = tween(durationMillis = 350),
        label = "track_color"
    )

    // Animated thumb color: changes to white when connected!
    val thumbColor by animateColorAsState(
        targetValue = if (isConnected) Color.White else thumbOffColor,
        animationSpec = tween(durationMillis = 350),
        label = "thumb_color"
    )

    // Animated power icon tint: white when OFF, JumpJump Blue when ON! (NO checkmark!)
    val powerIconTint by animateColorAsState(
        targetValue = if (isConnected) jumpJumpBlue else Color.White,
        animationSpec = tween(durationMillis = 350),
        label = "power_icon_tint"
    )

    // Rotating animation for connecting state
    val infiniteTransition = rememberInfiniteTransition(label = "connecting_anim")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin_angle"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- EXACT JUMPJUMP VPN CAPSULE SWITCH BUTTON ---
        Box(
            modifier = Modifier
                .width(trackWidth)
                .height(trackHeight)
                .clip(CircleShape)
                .background(trackColor)
                .border(
                    width = 1.dp,
                    color = if (isConnected) Color.White.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.08f),
                    shape = CircleShape
                )
                .shadow(
                    elevation = if (isConnected) 16.dp else 6.dp,
                    shape = CircleShape,
                    ambientColor = if (isConnected) jumpJumpBlue else Color.Black,
                    spotColor = if (isConnected) jumpJumpBlue else Color.Black
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onToggleConnect
                )
                .testTag("vpn_connect_button")
        ) {
            // Sliding Circular Thumb (Power Switch Knob)
            Box(
                modifier = Modifier
                    .padding(padding)
                    .offset(x = thumbOffset)
                    .size(thumbSize)
                    .clip(CircleShape)
                    .background(thumbColor)
                    .shadow(elevation = 8.dp, shape = CircleShape)
                    .border(
                        width = 1.dp,
                        color = if (isConnected) Color.White.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.12f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // If connecting, draw spinning ring around thumb
                if (isConnecting) {
                    Canvas(modifier = Modifier.size(thumbSize)) {
                        drawArc(
                            color = jumpJumpBlue,
                            startAngle = spinAngle,
                            sweepAngle = 120f,
                            useCenter = false,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }

                // Power Icon inside Thumb (Pure Power symbol, NO checkmark!)
                Icon(
                    imageVector = Icons.Filled.PowerSettingsNew,
                    contentDescription = "Power Button",
                    tint = powerIconTint,
                    modifier = Modifier.size(42.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Status Label Below Button
        val isError = status == VpnStatus.ERROR
        Text(
            text = when (status) {
                VpnStatus.CONNECTED -> AppStrings.get("connected", langCode)
                VpnStatus.CONNECTING -> AppStrings.get("connecting", langCode)
                VpnStatus.DISCONNECTED -> AppStrings.get("tap_to_connect", langCode)
                VpnStatus.ERROR -> if (langCode == "fa") "خطا در اتصال - لمس برای تلاش مجدد" else "Connection Failed - Tap to Retry"
            },
            color = when {
                isConnected -> jumpJumpBlue
                isError -> Color(0xFFFF5252)
                else -> VpnTextSecondary
            },
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
