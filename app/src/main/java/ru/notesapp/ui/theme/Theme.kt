package ru.notesapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

enum class ThemeVariant {
    PASTEL,
    FOREST
}

@Composable
fun NotesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    themeVariant: ThemeVariant,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        themeVariant == ThemeVariant.PASTEL && darkTheme -> PastelDarkScheme
        themeVariant == ThemeVariant.PASTEL -> PastelLightScheme
        themeVariant == ThemeVariant.FOREST && darkTheme -> ForestDarkScheme

        else -> ForestLightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NotesAppTypography,
        content = content
    )
}