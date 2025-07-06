package ui.theme

import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color

/**
 * Цветовая палитра для тёмной темы приложения
 */
val DarkColorPalette = darkColors(
    primary = Color(0xFFBB86FC),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC5),
    secondaryVariant = Color(0xFF018786),
    background = Color(0xFF000000),
    surface = Color(0xFF1A1A1A),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFCCCCCC),
    error = Color(0xFFCF6679)
)