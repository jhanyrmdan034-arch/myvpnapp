package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VpnServer
import com.example.ui.components.CountryFlag
import com.example.ui.theme.VpnDarkBg
import com.example.ui.theme.VpnNeonGreen
import com.example.ui.theme.VpnTextMuted
import com.example.ui.theme.VpnTextPrimary
import com.example.ui.theme.VpnTextSecondary
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CyberConnectingScreen(
    server: VpnServer,
    progress: Float,
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
    val isDone = progress >= 0.99f

    // Animations for Cyber Reactor
    val infiniteTransition = rememberInfiniteTransition(label = "cyber_reactor")

    // Continuous smooth rotation for outer holographic rings
    val rotateClockwise by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotate_cw"
    )

    // Counter rotation for internal energy ring
    val rotateCounterCw by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(4500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotate_ccw"
    )

    // Radar scan beam rotation
    val radarSweep by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radar_sweep"
    )

    // Core pulsing glow
    val corePulse by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "core_pulse"
    )

    // Accent color shifts from Cyber Cyan to Neon Emerald as progress advances
    val reactorAccentColor by animateColorAsState(
        targetValue = if (isDone) VpnNeonGreen else Color(0xFF00E5FF),
        animationSpec = tween(400),
        label = "accent_color"
    )

    val secondaryGlowColor by animateColorAsState(
        targetValue = if (isDone) Color(0xFF00E676) else Color(0xFF8A2BE2),
        animationSpec = tween(400),
        label = "secondary_color"
    )

    val stepsPersian = listOf(
        "برقراری تونل امن ChaCha20",
        "تست مسیر و اندازه‌گیری تاخیر سرور",
        "تخصیص آی‌پی ناشناس و ضد نشت DNS",
        "تایید نهایی و بازگشایی دروازه امن اینترنت"
    )

    val stepsEnglish = listOf(
        "Establishing ChaCha20 Encrypted Tunnel",
        "Optimizing Route & Latency Handshake",
        "Allocating Stealth IP & DNS Shield",
        "Finalizing Security & Opening Gateway"
    )

    val currentStepTitle = if (isFa) {
        stepsPersian.getOrElse(currentStep) { stepsPersian.last() }
    } else {
        stepsEnglish.getOrElse(currentStep) { stepsEnglish.last() }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VpnDarkBg)
            .statusBarsPadding()
            .testTag("cyber_connecting_screen")
    ) {
        // Futuristic Cyber Background Canvas (laser grids and ambient energy orbs)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridColor = Color(0xFF1E283F).copy(alpha = 0.25f)
            val stepSize = 48.dp.toPx()

            // Subtle vertical cyber grid
            var x = 0f
            while (x < size.width) {
                drawLine(
                    color = gridColor,
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 0.8f
                )
                x += stepSize
            }

            // Subtle horizontal cyber grid
            var y = 0f
            while (y < size.height) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 0.8f
                )
                y += stepSize
            }

            // Top ambient flare
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        reactorAccentColor.copy(alpha = 0.12f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.5f, size.height * 0.35f),
                    radius = size.width * 0.6f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --- TOP BAR: Close button, Protocol Title, Latency Badge ---
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Futuristic Cancel Button
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1A2234).copy(alpha = 0.8f))
                            .border(1.2.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onCancel
                            )
                            .testTag("connecting_cancel_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = if (isFa) "لغو اتصال" else "Cancel",
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Quantum Protocol Title
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isFa) "پروتکل اتصال کوانتومی" else "QUANTUM SECURE TUNNEL",
                            color = reactorAccentColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "AES-256-GCM • ChaCha20",
                            color = VpnTextMuted,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Ping Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF161E30))
                            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Speed,
                            contentDescription = null,
                            tint = VpnNeonGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${server.pingMs}ms",
                            color = VpnTextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- CENTER: The Quantum Energy Reactor & Holographic Vortex Canvas ---
            Box(
                modifier = Modifier
                    .size(280.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(280.dp)) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val maxRadius = size.width * 0.46f

                    // 1. Outer Laser Segmented Track (Clockwise)
                    val outerTrackRadius = maxRadius
                    val segmentCount = 12
                    val gapDeg = 8f
                    val sweepDeg = (360f / segmentCount) - gapDeg

                    for (i in 0 until segmentCount) {
                        val startAngle = rotateClockwise + (i * (360f / segmentCount))
                        drawArc(
                            color = reactorAccentColor.copy(alpha = 0.22f),
                            startAngle = startAngle,
                            sweepAngle = sweepDeg,
                            useCenter = false,
                            topLeft = Offset(center.x - outerTrackRadius, center.y - outerTrackRadius),
                            size = androidx.compose.ui.geometry.Size(outerTrackRadius * 2, outerTrackRadius * 2),
                            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    // 2. Middle Counter-Rotating Track (Counter Clockwise)
                    val midRadius = maxRadius * 0.82f
                    drawCircle(
                        color = secondaryGlowColor.copy(alpha = 0.12f),
                        radius = midRadius,
                        center = center,
                        style = Stroke(width = 1.dp.toPx())
                    )

                    // 3. Radar Beam Sweep
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(
                                Color.Transparent,
                                reactorAccentColor.copy(alpha = 0.02f),
                                reactorAccentColor.copy(alpha = 0.28f)
                            ),
                            center = center
                        ),
                        startAngle = radarSweep,
                        sweepAngle = 60f,
                        useCenter = true,
                        topLeft = Offset(center.x - midRadius, center.y - midRadius),
                        size = androidx.compose.ui.geometry.Size(midRadius * 2, midRadius * 2)
                    )

                    // 4. Orbiting Quantum Energy Nodes (12 dynamic particle nodes)
                    val particleCount = 12
                    for (i in 0 until particleCount) {
                        val angle = Math.toRadians((rotateCounterCw + (i * (360.0 / particleCount))).toDouble())
                        val orbitRadiusX = midRadius * 0.94f
                        val orbitRadiusY = midRadius * 0.58f
                        val px = center.x + (orbitRadiusX * cos(angle)).toFloat()
                        val py = center.y + (orbitRadiusY * sin(angle)).toFloat()

                        drawCircle(
                            color = if (i % 2 == 0) reactorAccentColor else secondaryGlowColor,
                            radius = if (i % 3 == 0) 4.5.dp.toPx() else 3.dp.toPx(),
                            center = Offset(px, py)
                        )
                    }

                    // 5. Central Reactor Plasma Halo
                    val haloRadius = maxRadius * 0.52f * corePulse
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                reactorAccentColor.copy(alpha = 0.45f),
                                secondaryGlowColor.copy(alpha = 0.18f),
                                Color.Transparent
                            ),
                            center = center,
                            radius = haloRadius
                        ),
                        center = center,
                        radius = haloRadius
                    )

                    // 6. Glowing Circular Progress Arc
                    val progressRadius = maxRadius * 0.62f
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(
                                Color(0xFF00E5FF),
                                Color(0xFF8A2BE2),
                                VpnNeonGreen
                            )
                        ),
                        startAngle = -90f,
                        sweepAngle = progress * 360f,
                        useCenter = false,
                        topLeft = Offset(center.x - progressRadius, center.y - progressRadius),
                        size = androidx.compose.ui.geometry.Size(progressRadius * 2, progressRadius * 2),
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Inner HUD Center Display
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (isDone) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Success",
                            tint = VpnNeonGreen,
                            modifier = Modifier
                                .size(58.dp)
                                .scale(corePulse)
                        )
                    } else {
                        // Futuristic Large Percentage readout
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            color = VpnTextPrimary,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = (-1).sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        // Status Tag inside reactor
                        Text(
                            text = when (currentStep) {
                                0 -> "TUNNELING"
                                1 -> "OPTIMIZING"
                                2 -> "STEALTH IP"
                                else -> "SECURING"
                            },
                            color = reactorAccentColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- SERVER HOLOGRAPHIC CARD ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF222B40).copy(alpha = 0.7f),
                                Color(0xFF131928).copy(alpha = 0.85f)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        Brush.verticalGradient(
                            listOf(
                                Color.White.copy(alpha = 0.22f),
                                Color.White.copy(alpha = 0.05f)
                            )
                        ),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Flag with glowing frame
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1A2234))
                                .border(1.5.dp, reactorAccentColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            CountryFlag(
                                countryCode = server.countryCode,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            val serverDisplayName = if (isFa) {
                                when (server.countryCode) {
                                    "US" -> "ایالات متحده"
                                    "DE" -> "آلمان"
                                    "GB" -> "بریتانیا"
                                    "CA" -> "کانادا"
                                    "NL" -> "هلند"
                                    "FR" -> "فرانسه"
                                    "JP" -> "ژاپن"
                                    "SG" -> "سنگاپور"
                                    "CH" -> "سوئیس"
                                    "SE" -> "سوئد"
                                    else -> server.name
                                }
                            } else {
                                server.name
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = serverDisplayName,
                                    color = VpnTextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Outlined.Security,
                                    contentDescription = null,
                                    tint = reactorAccentColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "IP: ${server.ipAddress}",
                                color = VpnTextSecondary,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    // Encryption Protocol Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF101624))
                            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "ChaCha20",
                            color = reactorAccentColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- STEP TELEMETRY & PROGRESS TRACK ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isDone) {
                        if (isFa) "اتصال با موفقیت برقرار شد!" else "Connection Secured!"
                    } else {
                        currentStepTitle
                    },
                    color = if (isDone) VpnNeonGreen else VpnTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 4-Segment Futuristic Neon Progress Track
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (stepIndex in 0..3) {
                        val isStepCompleted = currentStep > stepIndex || isDone
                        val isStepActive = currentStep == stepIndex && !isDone

                        val segmentColor by animateColorAsState(
                            targetValue = when {
                                isStepCompleted -> VpnNeonGreen
                                isStepActive -> Color(0xFF00E5FF)
                                else -> Color(0xFF222B40)
                            },
                            label = "segment_color"
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(segmentColor)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Live Terminal Log Line
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0D121F))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "> [LOG] $logMessage",
                        color = VpnTextMuted,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- BOTTOM FLOATING NOTICE CARD ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF161D2D).copy(alpha = 0.9f))
                    .border(1.dp, Color(0xFFE5A93C).copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = Color(0xFFE5A93C),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isFa) {
                            "هنگام برقراری اتصال رمزگذاری‌شده در این صفحه بمانید"
                        } else {
                            "Please remain on this screen while connection secures"
                        },
                        color = Color(0xFFE5A93C),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
