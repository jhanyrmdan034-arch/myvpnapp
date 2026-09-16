package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VpnServer

@Composable
fun ConnectingModalDialog(
    server: VpnServer,
    progress: Float,
    remainingSeconds: Int,
    currentStep: Int,
    logMessage: String,
    langCode: String,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onCancel()
    }

    val isFa = langCode == "fa"
    val jumpJumpBlue = Color(0xFF3A82F7)

    val infiniteTransition = rememberInfiniteTransition(label = "connecting_spinner")

    // Smooth continuous spinner rotation
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin_angle"
    )

    // Scrim overlay dimming the main screen
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.72f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { /* keep dialog modal */ }
            )
            .testTag("connecting_modal_dialog"),
        contentAlignment = Alignment.Center
    ) {
        // Centered White Dialog Card (matching disconnecting dialog as requested)
        Card(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(0.92f)
                .testTag("connecting_dialog_card"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 18.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Cancel Close Button
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onCancel
                            )
                            .testTag("dialog_close_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = if (isFa) "بستن" else "Close",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Title (Dark text for white card)
                Text(
                    text = if (isFa) "...در حال اتصال است VPN" else "VPN is connecting...",
                    color = Color(0xFF1E2638),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Subtitle
                Text(
                    text = if (isFa) "...لطفاً منتظر بمانید" else "Please wait...",
                    color = Color(0xFF7A869A),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Central Spinner with Animated Rocket and Fire (No countdown numbers!)
                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer Blue Arc Spinner
                    Canvas(modifier = Modifier.size(130.dp)) {
                        val strokeWidth = 7.dp.toPx()
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val radius = size.width * 0.44f

                        // Background muted circular track
                        drawCircle(
                            color = jumpJumpBlue.copy(alpha = 0.14f),
                            radius = radius,
                            center = center,
                            style = Stroke(width = strokeWidth)
                        )

                        // Rotating vibrant blue sweep arc
                        drawArc(
                            color = jumpJumpBlue,
                            startAngle = spinAngle,
                            sweepAngle = 260f,
                            useCenter = false,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    // In the center: Animated Launching Rocket with Fire! 🚀🔥
                    AnimatedRocketWithFire()
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Progress Indicator Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFE2E8F0))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress.coerceIn(0f, 1f))
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        jumpJumpBlue,
                                        Color(0xFF60A5FA)
                                    )
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Target Server Preview Pill (No Ping!)
                val serverDisplayName = if (isFa) {
                    when (server.countryCode) {
                        "US" -> "ایالات متحده"
                        "DE" -> "آلمان"
                        "GB" -> "انگلستان"
                        "CA" -> "کانادا"
                        "NL" -> "هلند"
                        "FR" -> "فرانسه"
                        "JP" -> "ژاپن"
                        "SG" -> "سنگاپور"
                        else -> server.nameFa
                    }
                } else {
                    server.name
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CountryFlag(
                        countryCode = server.countryCode,
                        size = 22.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = serverDisplayName,
                        color = Color(0xFF1E2638),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Cancel Button at bottom
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFEE2E2))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onCancel
                        )
                        .padding(vertical = 11.dp)
                        .testTag("dialog_cancel_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isFa) "لغو اتصال" else "Cancel",
                        color = Color(0xFFDC2626),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Animated rocket with active fiery thruster flames, sparks, and launch vibration!
 */
@Composable
fun AnimatedRocketWithFire(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "rocket_animation")

    // Vertical launch vibration / rumble
    val rumbleY by infiniteTransition.animateFloat(
        initialValue = -1.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(70, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rumble_y"
    )

    // Horizontal launch jitter
    val rumbleX by infiniteTransition.animateFloat(
        initialValue = -0.8f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(90, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rumble_x"
    )

    // Dynamic fire flame length scale
    val flameLengthScale by infiniteTransition.animateFloat(
        initialValue = 0.75f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(110, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_length"
    )

    // Dynamic fire flame width flicker
    val flameWidthScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(80, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_width"
    )

    // Sparks drift
    val sparkPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(280, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spark_phase"
    )

    Column(
        modifier = modifier
            .offset(x = rumbleX.dp, y = rumbleY.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Rocket Emoji upright (rotated -45 degrees so the nose points straight up)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(44.dp)
        ) {
            Text(
                text = "🚀",
                fontSize = 34.sp,
                modifier = Modifier.rotate(-45f)
            )
        }

        // Active Fiery Flame Canvas right underneath rocket thruster!
        Canvas(
            modifier = Modifier
                .size(width = 32.dp, height = 26.dp)
                .offset(y = (-4).dp)
        ) {
            val baseWidth = size.width * 0.45f * flameWidthScale
            val flameHeight = size.height * flameLengthScale
            val centerX = size.width / 2f
            val topY = 0f

            // 1. Outer Fierce Orange-Red Flame
            val outerFlamePath = Path().apply {
                moveTo(centerX - baseWidth / 2f, topY)
                // Left curve down to pointed tip
                quadraticBezierTo(
                    centerX - baseWidth * 0.7f,
                    flameHeight * 0.55f,
                    centerX,
                    flameHeight
                )
                // Right curve back up
                quadraticBezierTo(
                    centerX + baseWidth * 0.7f,
                    flameHeight * 0.55f,
                    centerX + baseWidth / 2f,
                    topY
                )
                close()
            }

            drawPath(
                path = outerFlamePath,
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFB300), // Amber yellow at base
                        Color(0xFFFF5722), // Deep orange
                        Color(0xFFE53935)  // Crimson red at tip
                    )
                )
            )

            // 2. Inner Glowing Core Flame (Bright Yellow & White Hot)
            val innerWidth = baseWidth * 0.5f
            val innerHeight = flameHeight * 0.6f
            val innerFlamePath = Path().apply {
                moveTo(centerX - innerWidth / 2f, topY)
                quadraticBezierTo(
                    centerX - innerWidth * 0.6f,
                    innerHeight * 0.5f,
                    centerX,
                    innerHeight
                )
                quadraticBezierTo(
                    centerX + innerWidth * 0.6f,
                    innerHeight * 0.5f,
                    centerX + innerWidth / 2f,
                    topY
                )
                close()
            }

            drawPath(
                path = innerFlamePath,
                brush = Brush.verticalGradient(
                    listOf(
                        Color.White,
                        Color(0xFFFFF176),
                        Color(0xFFFF9800)
                    )
                )
            )

            // 3. Falling Fiery Sparks / Embers
            val spark1Y = flameHeight * sparkPhase + 4f
            val spark2Y = flameHeight * ((sparkPhase + 0.5f) % 1f) + 6f

            drawCircle(
                color = Color(0xFFFFD54F).copy(alpha = (1f - sparkPhase).coerceIn(0f, 1f)),
                radius = 2.5f,
                center = Offset(centerX - 4f, spark1Y)
            )
            drawCircle(
                color = Color(0xFFFF7043).copy(alpha = (1f - (sparkPhase + 0.5f) % 1f).coerceIn(0f, 1f)),
                radius = 2f,
                center = Offset(centerX + 5f, spark2Y)
            )
        }
    }
}
