package com.phoebus.demo.phastpay.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PhastpayPrimaryDark,
    onPrimary = Color(0xFF002E68), // Texto azul bem escuro sobre o primário claro

    secondary = PhastpaySelectedDark,
    onSecondary = Color(0xFF003351),

    primaryContainer = PhastpayContainerDark,
    onPrimaryContainer = Color(0xFFDDE7FF),

    background = PhastpayBackgroundDark,
    onBackground = Color(0xFFE2E2E6), // Texto esbranquiçado

    surface = PhastpaySurfaceDark,
    onSurface = Color(0xFFE2E2E6),

    error = Color(0xFFFFB4AB), // Um vermelho mais rosado/pastel para o modo escuro
    onError = Color(0xFF690005)
)

private val LightColorScheme = lightColorScheme(
    primary = PhastpayPrimary,
    onPrimary = Color.White, // Texto branco sobre o azul primário

    secondary = PhastpaySelected,
    onSecondary = Color.White,

    primaryContainer = PhastpayContainer,
    onPrimaryContainer = Color(0xFF1A2B4D), // Texto azul escuro sobre o fundo claro

    background = PhastpayBackground,
    onBackground = PhastpayOnBackground,

    surface = Color.White,
    onSurface = PhastpayOnBackground
)

@Composable
fun AppSmartDemoPhastPayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}