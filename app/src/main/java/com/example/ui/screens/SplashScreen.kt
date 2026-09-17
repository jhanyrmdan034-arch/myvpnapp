package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.VpnDarkBg
import com.example.ui.theme.VpnTextPrimary
import kotlinx.coroutines.delay

/**
 * Premium 15-Second Launch Screen:
 * - Purely visual animation with NO numbers, percentages, or seconds.
 * - Prominently displays the official App Icon centered on a sleek solid dark background.
 * - Horizontal Linear Progress Bar filling smoothly from 0% to 100% over exactly 15 seconds.
 * - Synchronizes with the background subscription fetch to ensure server locations are populated.
 */
@Composable
fun SplashScreen(
    isServerListReady: Boolean,
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }
    var isAnimationCompleted by remember { mutableStateOf(false) }

    // Exactly 15 seconds (15,000 milliseconds) smooth fill from 0% to 100%
    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 15000,
                easing = LinearEasing
            )
        )
        isAnimationCompleted = true
    }

    // Transition seamlessly once the 15-second animation finishes AND server list is ready
    LaunchedEffect(isAnimationCompleted, isServerListReady) {
        if (isAnimationCompleted) {
            if (isServerListReady) {
                onSplashFinished()
            } else {
                // Safety guard: allow a grace period for network completion, then transition with fallback servers
                delay(2000)
                onSplashFinished()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VpnDarkBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            // Ambient glow behind the official App Icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(136.dp)
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0x383A82F7),
                                    Color(0x1800E5FF),
                                    Color.Transparent
                                )
                            ),
                            radius = size.minDimension * 0.75f
                        )
                    }
            ) {
                // Official App Icon prominently displayed
                Image(
                    painter = painterResource(id = R.drawable.vpn_lightning_shield_logo_1789471588711),
                    contentDescription = "VectaVPN Emblem",
                    modifier = Modifier
                        .size(112.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .border(1.5.dp, Color(0x333A82F7), RoundedCornerShape(28.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Official App Title branding
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Vecta",
                    color = VpnTextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "VPN",
                    color = Color(0xFF3A82F7),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Bottom Element: Horizontal Linear Progress Bar (clean line filling smoothly from left to right)
            // NO numbers, percentages, or seconds displayed
            Box(
                modifier = Modifier
                    .width(220.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color(0xFF1B2333))
                    .testTag("splash_linear_progress_bar")
            ) {
                val currentProgress = progress.value.coerceIn(0f, 1f)
                if (currentProgress > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(currentProgress)
                            .clip(RoundedCornerShape(100.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF2563EB),
                                        Color(0xFF3A82F7),
                                        Color(0xFF00E5FF)
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}
