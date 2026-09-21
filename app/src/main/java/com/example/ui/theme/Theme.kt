package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = TerracottaLight,
  onPrimary = TerracottaDark,
  primaryContainer = TerracottaDark,
  onPrimaryContainer = Color.White,
  secondary = TurmericGold,
  onSecondary = DeepCharcoal,
  secondaryContainer = DarkSurfaceVariant,
  onSecondaryContainer = SaffronAmber,
  background = DarkBackground,
  onBackground = DarkTextPrimary,
  surface = DarkSurface,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkTextSecondary
)

private val LightColorScheme = lightColorScheme(
  primary = TerracottaPrimary,
  onPrimary = Color.White,
  primaryContainer = AmberContainer,
  onPrimaryContainer = TerracottaDark,
  secondary = SaffronAmber,
  onSecondary = Color.White,
  secondaryContainer = AmberContainer,
  onSecondaryContainer = DeepCharcoal,
  background = WarmCreamBackground,
  onBackground = DeepCharcoal,
  surface = WarmSurface,
  onSurface = DeepCharcoal,
  surfaceVariant = WarmSurfaceVariant,
  onSurfaceVariant = MutedSlate,
  outline = DividerMuted
)

@Composable
fun RasoiTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our intentional warm culinary brand colors
  content: @Composable () -> Unit,
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
