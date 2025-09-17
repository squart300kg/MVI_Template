package kr.co.architecture.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightColors = lightColorScheme(
  primary = Color(0xFF6366F1),
  onPrimary = Color.White,
  primaryContainer = Color(0xFFE0E7FF),
  onPrimaryContainer = Color(0xFF1E1B4B),

  secondary = Color(0xFF10B981),
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFD1FAE5),
  onSecondaryContainer = Color(0xFF064E3B),

  background = Color(0xFFF7F8FA),
  onBackground = Color(0xFF111827),

  surface = Color.White,
  onSurface = Color(0xFF111827),
  surfaceVariant = Color(0xFFF1F5F9),
  onSurfaceVariant = Color(0xFF6B7280),

  outline = Color(0xFFE2E8F0),
  error = Color(0xFFEF4444),
  onError = Color.White
)

val DarkColors = darkColorScheme(
  primary = Color(0xFF818CF8),
  onPrimary = Color(0xFF1E1B4B),
  primaryContainer = Color(0xFF312E81),
  onPrimaryContainer = Color(0xFFE0E7FF),

  secondary = Color(0xFF34D399),
  onSecondary = Color(0xFF052E24),
  secondaryContainer = Color(0xFF064E3B),
  onSecondaryContainer = Color(0xFFD1FAE5),

  background = Color(0xFF0B0F14),
  onBackground = Color(0xFFE5E7EB),

  surface = Color(0xFF111418),
  onSurface = Color(0xFFE5E7EB),
  surfaceVariant = Color(0xFF1A1F24),
  onSurfaceVariant = Color(0xFF94A3B8),

  outline = Color(0xFF29313A),
  error = Color(0xFFF87171),
  onError = Color(0xFF3B0B0B)
)