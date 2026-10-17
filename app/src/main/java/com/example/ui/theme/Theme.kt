package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SizoDarkColorScheme = darkColorScheme(
    primary = SizoOrangePrimary,
    onPrimary = SizoBlack,
    primaryContainer = SizoOrangeDark,
    onPrimaryContainer = SizoOrangeLight,
    secondary = SizoOrangeBright,
    onSecondary = SizoBlack,
    secondaryContainer = SizoSurfaceVariant,
    onSecondaryContainer = SizoOrangeLight,
    tertiary = SizoOrangeLight,
    onTertiary = SizoBlack,
    background = SizoBlack,
    onBackground = SizoTextWhite,
    surface = SizoSurface,
    onSurface = SizoTextWhite,
    surfaceVariant = SizoSurfaceVariant,
    onSurfaceVariant = SizoTextSecondary,
    outline = SizoOrangeSubtle,
    outlineVariant = SizoBorderLight
)

@Composable
fun SizoIaTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = SizoDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = SizoBlack.toArgb()
                window.navigationBarColor = SizoBlack.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward-compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    SizoIaTheme(content = content)
}
