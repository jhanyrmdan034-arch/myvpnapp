package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val JumpJumpDarkColorScheme = darkColorScheme(
    primary = VpnNeonGreen,
    onPrimary = VpnDarkBg,
    primaryContainer = VpnSurfaceElevated,
    onPrimaryContainer = VpnNeonGreen,
    secondary = VpnNeonGreen,
    onSecondary = VpnDarkBg,
    tertiary = VpnNeonRed,
    background = VpnDarkBg,
    onBackground = VpnTextPrimary,
    surface = VpnSurface,
    onSurface = VpnTextPrimary,
    surfaceVariant = VpnSurfaceElevated,
    onSurfaceVariant = VpnTextSecondary,
    outline = VpnCardBorder,
    error = VpnNeonRed
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = VpnDarkBg.toArgb()
            window.navigationBarColor = VpnDarkBg.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = JumpJumpDarkColorScheme,
        typography = Typography,
        content = content
    )
}
