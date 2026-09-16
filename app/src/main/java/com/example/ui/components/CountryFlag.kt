package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CountryFlag(
    countryCode: String,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        when (countryCode.uppercase()) {
            "US" -> UsaFlagCanvas(size)
            "DE" -> GermanyFlagCanvas(size)
            "GB", "UK" -> UkFlagCanvas(size)
            "CA" -> CanadaFlagCanvas(size)
            "FR" -> FranceFlagCanvas(size)
            "NL" -> NetherlandsFlagCanvas(size)
            "JP" -> JapanFlagCanvas(size)
            "SG" -> SingaporeFlagCanvas(size)
            "IR" -> IranFlagCanvas(size)
            "ES" -> SpainFlagCanvas(size)
            "TR" -> TurkeyFlagCanvas(size)
            "SA" -> SaudiFlagCanvas(size)
            "IT" -> ItalyFlagCanvas(size)
            "RU" -> RussiaFlagCanvas(size)
            "AT" -> AustriaFlagCanvas(size)
            "FI" -> FinlandFlagCanvas(size)
            "SE" -> SwedenFlagCanvas(size)
            "CH" -> SwitzerlandFlagCanvas(size)
            "UA" -> UkraineFlagCanvas(size)
            "AE" -> UaeFlagCanvas(size)
            "IN" -> IndiaFlagCanvas(size)
            "NG" -> NigeriaFlagCanvas(size)
            else -> {
                // Fallback text badge
                Text(
                    text = countryCode.take(2).uppercase(),
                    fontSize = (size.value * 0.4f).sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun UsaFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stripeH = h / 7f
        val red = Color(0xFFB22234)
        val white = Color(0xFFFFFFFF)
        val blue = Color(0xFF3C3B6E)

        // Draw 7 alternating stripes for circle
        for (i in 0 until 7) {
            drawRect(
                color = if (i % 2 == 0) red else white,
                topLeft = Offset(0f, i * stripeH),
                size = Size(w, stripeH)
            )
        }
        // Draw blue canton
        drawRect(
            color = blue,
            topLeft = Offset(0f, 0f),
            size = Size(w * 0.5f, h * 0.55f)
        )
        // Draw small star dots
        val dotRadius = w * 0.022f
        for (row in 0..2) {
            for (col in 0..2) {
                drawCircle(
                    color = white,
                    radius = dotRadius,
                    center = Offset(
                        w * 0.08f + col * w * 0.14f,
                        h * 0.09f + row * h * 0.15f
                    )
                )
            }
        }
    }
}

@Composable
private fun GermanyFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stripeH = h / 3f

        drawRect(Color(0xFF1B1B1B), Offset(0f, 0f), Size(w, stripeH))
        drawRect(Color(0xFFDD0000), Offset(0f, stripeH), Size(w, stripeH))
        drawRect(Color(0xFFFFCE00), Offset(0f, stripeH * 2), Size(w, stripeH))
    }
}

@Composable
private fun UkFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val blue = Color(0xFF012169)
        val white = Color(0xFFFFFFFF)
        val red = Color(0xFFC8102E)

        // Background blue
        drawRect(blue, Offset(0f, 0f), Size(w, h))

        // Diagonal white saltires
        val path1 = Path().apply {
            moveTo(0f, 0f)
            lineTo(w * 0.15f, 0f)
            lineTo(w, h * 0.85f)
            lineTo(w, h)
            lineTo(w * 0.85f, h)
            lineTo(0f, h * 0.15f)
            close()
        }
        val path2 = Path().apply {
            moveTo(w, 0f)
            lineTo(w * 0.85f, 0f)
            lineTo(0f, h * 0.85f)
            lineTo(0f, h)
            lineTo(w * 0.15f, h)
            lineTo(w, h * 0.15f)
            close()
        }
        drawPath(path1, white)
        drawPath(path2, white)

        // Red diagonals
        val redDiag1 = Path().apply {
            moveTo(0f, 0f)
            lineTo(w, h)
            lineTo(w * 0.92f, h)
            lineTo(0f, h * 0.08f)
            close()
        }
        val redDiag2 = Path().apply {
            moveTo(w, 0f)
            lineTo(0f, h)
            lineTo(0f, h * 0.92f)
            lineTo(w * 0.92f, 0f)
            close()
        }
        drawPath(redDiag1, red)
        drawPath(redDiag2, red)

        // St. George white cross
        drawRect(white, Offset(w * 0.35f, 0f), Size(w * 0.3f, h))
        drawRect(white, Offset(0f, h * 0.35f), Size(w, h * 0.3f))

        // St. George red cross
        drawRect(red, Offset(w * 0.4f, 0f), Size(w * 0.2f, h))
        drawRect(red, Offset(0f, h * 0.4f), Size(w, h * 0.2f))
    }
}

@Composable
private fun CanadaFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val red = Color(0xFFFF0000)
        val white = Color(0xFFFFFFFF)

        // Red edges
        drawRect(red, Offset(0f, 0f), Size(w * 0.28f, h))
        drawRect(white, Offset(w * 0.28f, 0f), Size(w * 0.44f, h))
        drawRect(red, Offset(w * 0.72f, 0f), Size(w * 0.28f, h))

        // Maple leaf silhouette stylized
        val leaf = Path().apply {
            val cx = w * 0.5f
            val cy = h * 0.5f
            moveTo(cx, cy - h * 0.24f)
            lineTo(cx + w * 0.06f, cy - h * 0.12f)
            lineTo(cx + w * 0.15f, cy - h * 0.14f)
            lineTo(cx + w * 0.09f, cy - h * 0.04f)
            lineTo(cx + w * 0.14f, cy + h * 0.06f)
            lineTo(cx + w * 0.05f, cy + h * 0.05f)
            lineTo(cx + w * 0.02f, cy + h * 0.18f)
            lineTo(cx - w * 0.02f, cy + h * 0.18f)
            lineTo(cx - w * 0.05f, cy + h * 0.05f)
            lineTo(cx - w * 0.14f, cy + h * 0.06f)
            lineTo(cx - w * 0.09f, cy - h * 0.04f)
            lineTo(cx - w * 0.15f, cy - h * 0.14f)
            lineTo(cx - w * 0.06f, cy - h * 0.12f)
            close()
        }
        drawPath(leaf, red)
    }
}

@Composable
private fun FranceFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stripeW = w / 3f

        drawRect(Color(0xFF002395), Offset(0f, 0f), Size(stripeW, h))
        drawRect(Color(0xFFFFFFFF), Offset(stripeW, 0f), Size(stripeW, h))
        drawRect(Color(0xFFED2939), Offset(stripeW * 2, 0f), Size(stripeW, h))
    }
}

@Composable
private fun NetherlandsFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stripeH = h / 3f

        drawRect(Color(0xFFAE1C28), Offset(0f, 0f), Size(w, stripeH))
        drawRect(Color(0xFFFFFFFF), Offset(0f, stripeH), Size(w, stripeH))
        drawRect(Color(0xFF21468B), Offset(0f, stripeH * 2), Size(w, stripeH))
    }
}

@Composable
private fun JapanFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        drawRect(Color(0xFFFFFFFF), Offset(0f, 0f), Size(w, h))
        drawCircle(
            color = Color(0xFFBC002D),
            radius = w * 0.3f,
            center = Offset(w * 0.5f, h * 0.5f)
        )
    }
}

@Composable
private fun SingaporeFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        drawRect(Color(0xFFED2939), Offset(0f, 0f), Size(w, h * 0.5f))
        drawRect(Color(0xFFFFFFFF), Offset(0f, h * 0.5f), Size(w, h * 0.5f))
        // Crescent
        drawCircle(Color.White, w * 0.18f, Offset(w * 0.25f, h * 0.25f))
        drawCircle(Color(0xFFED2939), w * 0.16f, Offset(w * 0.28f, h * 0.25f))
    }
}

@Composable
private fun IranFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stripeH = h / 3f

        drawRect(Color(0xFF239F40), Offset(0f, 0f), Size(w, stripeH))
        drawRect(Color(0xFFFFFFFF), Offset(0f, stripeH), Size(w, stripeH))
        drawRect(Color(0xFFDA0000), Offset(0f, stripeH * 2), Size(w, stripeH))
        // Center emblem representation
        drawCircle(Color(0xFFDA0000), w * 0.12f, Offset(w * 0.5f, h * 0.5f))
    }
}

@Composable
private fun SpainFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        drawRect(Color(0xFFAA151B), Offset(0f, 0f), Size(w, h * 0.25f))
        drawRect(Color(0xFFF1BF00), Offset(0f, h * 0.25f), Size(w, h * 0.5f))
        drawRect(Color(0xFFAA151B), Offset(0f, h * 0.75f), Size(w, h * 0.25f))
    }
}

@Composable
private fun TurkeyFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        drawRect(Color(0xFFE30A17), Offset(0f, 0f), Size(w, h))
        drawCircle(Color.White, w * 0.25f, Offset(w * 0.42f, h * 0.5f))
        drawCircle(Color(0xFFE30A17), w * 0.2f, Offset(w * 0.47f, h * 0.5f))
        drawCircle(Color.White, w * 0.07f, Offset(w * 0.65f, h * 0.5f))
    }
}

@Composable
private fun SaudiFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        drawRect(Color(0xFF006C35), Offset(0f, 0f), Size(w, h))
        // White script & sword line
        drawRect(Color.White, Offset(w * 0.25f, h * 0.45f), Size(w * 0.5f, h * 0.05f))
        drawRect(Color.White, Offset(w * 0.2f, h * 0.55f), Size(w * 0.6f, h * 0.04f))
    }
}

@Composable
private fun ItalyFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stripeW = w / 3f

        drawRect(Color(0xFF009246), Offset(0f, 0f), Size(stripeW, h))
        drawRect(Color(0xFFFFFFFF), Offset(stripeW, 0f), Size(stripeW, h))
        drawRect(Color(0xFFCE2B37), Offset(stripeW * 2, 0f), Size(stripeW, h))
    }
}

@Composable
private fun RussiaFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stripeH = h / 3f

        drawRect(Color(0xFFFFFFFF), Offset(0f, 0f), Size(w, stripeH))
        drawRect(Color(0xFF0039A6), Offset(0f, stripeH), Size(w, stripeH))
        drawRect(Color(0xFFD52B1E), Offset(0f, stripeH * 2), Size(w, stripeH))
    }
}

@Composable
private fun AustriaFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stripeH = h / 3f

        drawRect(Color(0xFFED2939), Offset(0f, 0f), Size(w, stripeH))
        drawRect(Color(0xFFFFFFFF), Offset(0f, stripeH), Size(w, stripeH))
        drawRect(Color(0xFFED2939), Offset(0f, stripeH * 2), Size(w, stripeH))
    }
}

@Composable
private fun FinlandFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val blue = Color(0xFF003580)
        val white = Color(0xFFFFFFFF)

        drawRect(white, Offset(0f, 0f), Size(w, h))
        drawRect(blue, Offset(w * 0.3f, 0f), Size(w * 0.2f, h))
        drawRect(blue, Offset(0f, h * 0.4f), Size(w, h * 0.2f))
    }
}

@Composable
private fun SwedenFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val blue = Color(0xFF006AA7)
        val yellow = Color(0xFFFECC00)

        drawRect(blue, Offset(0f, 0f), Size(w, h))
        drawRect(yellow, Offset(w * 0.3f, 0f), Size(w * 0.18f, h))
        drawRect(yellow, Offset(0f, h * 0.4f), Size(w, h * 0.2f))
    }
}

@Composable
private fun SwitzerlandFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val red = Color(0xFFD52B1E)
        val white = Color(0xFFFFFFFF)

        drawRect(red, Offset(0f, 0f), Size(w, h))
        drawRect(white, Offset(w * 0.4f, h * 0.2f), Size(w * 0.2f, h * 0.6f))
        drawRect(white, Offset(w * 0.2f, h * 0.4f), Size(w * 0.6f, h * 0.2f))
    }
}

@Composable
private fun UkraineFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stripeH = h / 2f

        drawRect(Color(0xFF005BBB), Offset(0f, 0f), Size(w, stripeH))
        drawRect(Color(0xFFFFD500), Offset(0f, stripeH), Size(w, stripeH))
    }
}

@Composable
private fun UaeFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stripeH = h / 3f

        drawRect(Color(0xFF00732F), Offset(0f, 0f), Size(w, stripeH))
        drawRect(Color(0xFFFFFFFF), Offset(0f, stripeH), Size(w, stripeH))
        drawRect(Color(0xFF000000), Offset(0f, stripeH * 2), Size(w, stripeH))
        drawRect(Color(0xFFFF0000), Offset(0f, 0f), Size(w * 0.28f, h))
    }
}

@Composable
private fun IndiaFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stripeH = h / 3f

        drawRect(Color(0xFFFF9933), Offset(0f, 0f), Size(w, stripeH))
        drawRect(Color(0xFFFFFFFF), Offset(0f, stripeH), Size(w, stripeH))
        drawRect(Color(0xFF138808), Offset(0f, stripeH * 2), Size(w, stripeH))
        drawCircle(Color(0xFF000080), radius = stripeH * 0.35f, center = Offset(w * 0.5f, h * 0.5f))
    }
}

@Composable
private fun NigeriaFlagCanvas(size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stripeW = w / 3f

        drawRect(Color(0xFF008751), Offset(0f, 0f), Size(stripeW, h))
        drawRect(Color(0xFFFFFFFF), Offset(stripeW, 0f), Size(stripeW, h))
        drawRect(Color(0xFF008751), Offset(stripeW * 2, 0f), Size(stripeW, h))
    }
}

