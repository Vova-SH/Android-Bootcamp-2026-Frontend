package com.example.planify.main.common.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.planify.main.common.entities.ThemeId
import com.example.planify.main.common.themes.dimens.Dimens
import com.example.planify.main.common.themes.dimens.LocalDimens
import com.example.planify.main.common.themes.icons.Icons
import com.example.planify.main.common.themes.icons.LocalIcons
import com.example.planify.main.common.themes.icons.defaultIcons
import com.example.planify.main.common.themes.shapes.LocalShapes
import com.example.planify.main.common.themes.shapes.Shapes
import com.example.planify.main.common.themes.shapes.shapes
import com.example.planify.main.common.themes.spacing.LocalSpacing
import com.example.planify.main.common.themes.spacing.Spacing
import com.example.planify.main.features.settings.data.repositories_impl.SettingsRepositoryImpl
import com.example.planify.main.features.settings.domain.services_impl.SettingsServiceImpl
import com.example.planify.main.features.settings.domain.entities.LocalSettings
import com.example.planify.main.navigation.screens.settings_screen.SettingsViewModel

/* ---------------------------
   1) Raw colors (из theme.css)
---------------------------- */

// Base
private val BgLight = Color(0xFFF6F8FF)
private val FgLight = Color(0xFF070B18)

private val BgDark  = Color(0xFF070B18)
private val FgDark  = Color(0xFFF6F8FF)

// Primary / Accent
private val BluePrimary = Color(0xFF0B5CFF)
private val CyanAccent  = Color(0xFF4CC3FF)
private val OrangeAccent = Color(0xFFFF6B35)

// Destructive
private val DestructiveLight = Color(0xFFE63946)
private val DestructiveDark  = Color(0xFFEF4444)

// Charts
private val Chart1 = Color(0xFF0B5CFF)
private val Chart2 = Color(0xFF4CC3FF)
private val Chart3 = Color(0xFF7DD3FC)
private val Chart4 = Color(0xFFA5F3FC)
private val Chart5 = Color(0xFFCFFAFE)

// ===== Light rgba tokens (ARGB) =====
private val CardLight           = Color(0xA6FFFFFF)      // rgba(255,255,255,0.65)
private val CardBorderLight     = Color(0x66FFFFFF)      // rgba(255,255,255,0.4)
private val PopoverLight        = Color(0xBFFFFFFF)      // rgba(255,255,255,0.75)

private val PrimaryGlowLight    = Color(0x400B5CFF)      // rgba(11,92,255,0.25) -> 0x40
private val SecondaryLight      = Color(0x140B5CFF)      // rgba(11,92,255,0.08)
private val MutedLight          = Color(0x0D070B18)      // rgba(7,11,24,0.05)
private val MutedFgLight        = Color(0x99070B18)      // rgba(7,11,24,0.6)
private val AccentLight         = Color(0x1A4CC3FF)      // rgba(76,195,255,0.1)

private val GlassBgLight        = Color(0x80FFFFFF)      // rgba(255,255,255,0.5)
private val GlassBgStrongLight  = Color(0xBFFFFFFF)      // rgba(255,255,255,0.75)
private val GlassBorderLight    = Color(0x99FFFFFF)      // rgba(255,255,255,0.6)
private val GlassBorderGlowLight= Color(0x260B5CFF)      // rgba(11,92,255,0.15)
private val GlassShadowLight    = Color(0x140B5CFF)      // rgba(11,92,255,0.08)

private val Aurora1Light        = Color(0x260B5CFF)      // rgba(11,92,255,0.15)
private val Aurora2Light        = Color(0x264CC3FF)      // rgba(76,195,255,0.15)
private val Aurora3Light        = Color(0x1AB4DCFF)      // rgba(180,220,255,0.1)

private val BorderLight         = Color(0x14070B18)      // rgba(7,11,24,0.08)
private val InputLight          = Color(0x80FFFFFF)      // rgba(255,255,255,0.5)
private val SwitchBgLight       = Color(0x26070B18)      // rgba(7,11,24,0.15)
private val RingLight           = Color(0xFF0B5CFF)

// Sidebar (light)
private val SidebarLight        = Color(0xA6FFFFFF)
private val SidebarAccentLight  = Color(0x140B5CFF)
private val SidebarBorderLight  = Color(0x66FFFFFF)
private val SidebarRingLight    = Color(0xFF0B5CFF)

// ===== Dark rgba tokens (ARGB) =====
private val CardDark            = Color(0xA60F172A)      // rgba(15,23,42,0.65)
private val CardBorderDark      = Color(0x264CC3FF)      // rgba(76,195,255,0.15)
private val PopoverDark         = Color(0xD90F172A)      // rgba(15,23,42,0.85)

private val PrimaryGlowDark     = Color(0x660B5CFF)      // rgba(11,92,255,0.4)
private val SecondaryDark       = Color(0x260B5CFF)      // rgba(11,92,255,0.15)
private val MutedDark           = Color(0x14F6F8FF)      // rgba(246,248,255,0.08)
private val MutedFgDark         = Color(0x99F6F8FF)      // rgba(246,248,255,0.6)
private val AccentDark          = Color(0x264CC3FF)      // rgba(76,195,255,0.15)

private val GlassBgDark         = Color(0x800F172A)      // rgba(15,23,42,0.5)
private val GlassBgStrongDark   = Color(0xD90F172A)      // rgba(15,23,42,0.85)
private val GlassBorderDark     = Color(0x334CC3FF)      // rgba(76,195,255,0.2)
private val GlassBorderGlowDark = Color(0x594CC3FF)      // rgba(76,195,255,0.35)
private val GlassShadowDark     = Color(0x1F4CC3FF)      // rgba(76,195,255,0.12)

private val Aurora1Dark         = Color(0x330B5CFF)      // rgba(11,92,255,0.2)
private val Aurora2Dark         = Color(0x334CC3FF)      // rgba(76,195,255,0.2)
private val Aurora3Dark         = Color(0x261E3A8A)      // rgba(30,58,138,0.15)

private val BorderDark          = Color(0x1F4CC3FF)      // rgba(76,195,255,0.12)
private val InputDark           = Color(0x800F172A)      // rgba(15,23,42,0.5)
private val SwitchBgDark        = Color(0x26F6F8FF)      // rgba(246,248,255,0.15)
private val RingDark            = Color(0xFF4CC3FF)

// Sidebar (dark)
private val SidebarDark         = Color(0xA60F172A)
private val SidebarAccentDark   = Color(0x260B5CFF)
private val SidebarBorderDark   = Color(0x264CC3FF)
private val SidebarRingDark     = Color(0xFF4CC3FF)

/* ---------------------------
   2) Material ColorScheme
---------------------------- */

private val lightScheme: ColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,

    primaryContainer = SecondaryLight,
    onPrimaryContainer = FgLight,

    secondary = CyanAccent,
    onSecondary = FgLight,
    secondaryContainer = AccentLight,
    onSecondaryContainer = FgLight,

    tertiary = OrangeAccent,
    onTertiary = Color.White,

    background = BgLight,
    onBackground = FgLight,

    surface = Color.White,
    onSurface = FgLight,

    surfaceVariant = InputLight,
    onSurfaceVariant = MutedFgLight,

    outline = BorderLight,

    error = DestructiveLight,
    onError = Color.White
)

private val darkScheme: ColorScheme = darkColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,

    primaryContainer = SecondaryDark,
    onPrimaryContainer = FgDark,

    secondary = CyanAccent,
    onSecondary = Color(0xFF070B18),
    secondaryContainer = AccentDark,
    onSecondaryContainer = FgDark,

    tertiary = OrangeAccent,
    onTertiary = Color(0xFF070B18),

    background = BgDark,
    onBackground = FgDark,

    surface = Color(0xFF0F172A),
    onSurface = FgDark,

    surfaceVariant = InputDark,
    onSurfaceVariant = MutedFgDark,

    outline = BorderDark,

    error = DestructiveDark,
    onError = Color.White
)

/* ---------------------------
   3) Extra tokens: glass, aurora, charts, sidebar, ring, etc.
---------------------------- */

@Immutable
data class Glass(
    val bg: Color,
    val bgStrong: Color,
    val border: Color,
    val borderGlow: Color,
    val shadow: Color,
)

@Immutable
data class Aurora(
    val c1: Color,
    val c2: Color,
    val c3: Color,
)

@Immutable
data class Sidebar(
    val bg: Color,
    val foreground: Color,
    val primary: Color,
    val onPrimary: Color,
    val accent: Color,
    val onAccent: Color,
    val border: Color,
    val ring: Color
)

@Immutable
data class ExtraColors(
    val background: Color,
    val foreground: Color,

    val card: Color,
    val cardForeground: Color,
    val cardBorder: Color,

    val popover: Color,
    val popoverForeground: Color,

    val primary: Color,
    val onPrimary: Color,
    val primaryGlow: Color,

    val secondary: Color,
    val secondaryForeground: Color,

    val muted: Color,
    val mutedForeground: Color,

    val accent: Color,
    val accentForeground: Color,
    val accentGlow: Color,

    val destructive: Color,
    val destructiveForeground: Color,

    val border: Color,
    val input: Color,
    val inputBackground: Color,
    val switchBackground: Color,
    val ring: Color,

    val charts: List<Color>,
    val glass: Glass,
    val aurora: Aurora,
    val sidebar: Sidebar,
)

private val ExtrasLight = ExtraColors(
    background = BgLight,
    foreground = FgLight,

    card = CardLight,
    cardForeground = FgLight,
    cardBorder = CardBorderLight,

    popover = PopoverLight,
    popoverForeground = FgLight,

    primary = BluePrimary,
    onPrimary = Color.White,
    primaryGlow = PrimaryGlowLight,

    secondary = SecondaryLight,
    secondaryForeground = FgLight,

    muted = MutedLight,
    mutedForeground = MutedFgLight,

    accent = AccentLight,
    accentForeground = FgLight,
    accentGlow = CyanAccent,

    destructive = DestructiveLight,
    destructiveForeground = Color.White,

    border = BorderLight,
    input = InputLight,
    inputBackground = InputLight,
    switchBackground = SwitchBgLight,
    ring = RingLight,

    charts = listOf(Chart1, Chart2, Chart3, Chart4, Chart5),

    glass = Glass(
        bg = GlassBgLight,
        bgStrong = GlassBgStrongLight,
        border = GlassBorderLight,
        borderGlow = GlassBorderGlowLight,
        shadow = GlassShadowLight
    ),

    aurora = Aurora(
        c1 = Aurora1Light,
        c2 = Aurora2Light,
        c3 = Aurora3Light
    ),

    sidebar = Sidebar(
        bg = SidebarLight,
        foreground = FgLight,
        primary = BluePrimary,
        onPrimary = Color.White,
        accent = SidebarAccentLight,
        onAccent = FgLight,
        border = SidebarBorderLight,
        ring = SidebarRingLight
    )
)

private val ExtrasDark = ExtraColors(
    background = BgDark,
    foreground = FgDark,

    card = CardDark,
    cardForeground = FgDark,
    cardBorder = CardBorderDark,

    popover = PopoverDark,
    popoverForeground = FgDark,

    primary = BluePrimary,
    onPrimary = Color.White,
    primaryGlow = PrimaryGlowDark,

    secondary = SecondaryDark,
    secondaryForeground = FgDark,

    muted = MutedDark,
    mutedForeground = MutedFgDark,

    accent = AccentDark,
    accentForeground = FgDark,
    accentGlow = CyanAccent,

    destructive = DestructiveDark,
    destructiveForeground = Color.White,

    border = BorderDark,
    input = InputDark,
    inputBackground = InputDark,
    switchBackground = SwitchBgDark,
    ring = RingDark,

    charts = listOf(Chart1, Chart2, Chart3, Chart4, Chart5),

    glass = Glass(
        bg = GlassBgDark,
        bgStrong = GlassBgStrongDark,
        border = GlassBorderDark,
        borderGlow = GlassBorderGlowDark,
        shadow = GlassShadowDark
    ),

    aurora = Aurora(
        c1 = Aurora1Dark,
        c2 = Aurora2Dark,
        c3 = Aurora3Dark
    ),

    sidebar = Sidebar(
        bg = SidebarDark,
        foreground = FgDark,
        primary = BluePrimary,
        onPrimary = Color.White,
        accent = SidebarAccentDark,
        onAccent = FgDark,
        border = SidebarBorderDark,
        ring = SidebarRingDark
    )
)

/* ---------------------------
   4) Gradients (Brush)
---------------------------- */

@Immutable
data class Gradients(
    val orange: Brush,
    val blue: Brush,
    val blueDeep: Brush,
    val radialOrange: (sizePx: Size) -> Brush,
    val radialBlue: (sizePx: Size) -> Brush,
)

private val GradientsObj = Gradients(
    orange = Brush.linearGradient(
        colors = listOf(Color(0xFFFF6B35), Color(0xFFF7931E), Color(0xFFFDB44B)),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    ),
    blue = Brush.linearGradient(
        colors = listOf(Color(0xFF0B5CFF), Color(0xFF1E90FF), Color(0xFF4CC3FF)),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    ),
    blueDeep = Brush.linearGradient(
        colors = listOf(Color(0xFF070B18), Color(0xFF0A1024), Color(0xFF0B1B3A)),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    ),
    radialOrange = { size ->
        Brush.radialGradient(
            colors = listOf(Color(0x66FF6B35), Color.Transparent), // 0.4 alpha
            center = Offset(size.width * 0.30f, size.height * 0.40f),
            radius = (minOf(size.width, size.height) * 0.70f)
        )
    },
    radialBlue = { size ->
        Brush.radialGradient(
            colors = listOf(Color(0x661E90FF), Color.Transparent), // rgba(30,144,255,0.4)
            center = Offset(size.width * 0.70f, size.height * 0.60f),
            radius = (minOf(size.width, size.height) * 0.70f)
        )
    }
)

/* ---------------------------
   6) CompositionLocals + Theme
---------------------------- */

private val LocalExtras = staticCompositionLocalOf<ExtraColors> {
    error("PlanifyTheme is not provided")
}

private val LocalGradients = staticCompositionLocalOf<Gradients> {
    error("PlanifyTheme is not provided")
}


@Composable
fun PlanifyTheme(
    windowSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
        darkTheme -> darkScheme
        else -> lightScheme
    }

    val extras = when {
        darkTheme -> ExtrasDark
        else -> ExtrasLight
    }

    val dimens = when (windowSize) {
        WindowWidthSizeClass.Compact -> Dimens(
            iconSize = 6.dp,
            bottomBarHeight = 88.dp
        )

        WindowWidthSizeClass.Medium -> Dimens(
            iconSize = 24.dp,
            bottomBarHeight = 96.dp
        )
        else -> Dimens(
            iconSize = 36.dp,
            bottomBarHeight = 112.dp
        )
    }

    val spacing = Spacing()
    val customShapes = shapes(dimens)
    val icons = defaultIcons(dimens)

    CompositionLocalProvider(
        LocalDimens provides dimens,
        LocalSpacing provides spacing,
        LocalShapes provides customShapes,
        LocalIcons provides icons,
        LocalExtras provides extras,
        LocalGradients provides GradientsObj
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
object Locals {
    val dimens: Dimens
        @Composable
        get() = LocalDimens.current

    val spacing: Spacing
        @Composable
        get() = LocalSpacing.current

    val shapes: Shapes
        @Composable
        get() = LocalShapes.current

    val icons: Icons
        @Composable
        get() = LocalIcons.current

    val extras: ExtraColors
        @Composable get() = LocalExtras.current

    val gradients: Gradients
        @Composable get() = LocalGradients.current
}