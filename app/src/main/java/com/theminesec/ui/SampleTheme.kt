package com.theminesec.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.theminesec.MineHades.R
import com.theminesec.MineHades.activity.ui.ThemeProvider

object SampleTheme {
    val mpocColors: ThemeProvider.MPoCColors
        @Composable
        @ReadOnlyComposable
        get() = ThemeProvider.MPoCColorsLight()

    val spacing: ThemeProvider.Spacing
        @Composable
        @ReadOnlyComposable
        get() = ThemeProvider.Spacing()

    val iconSize: ThemeProvider.IconSize
        @Composable
        @ReadOnlyComposable
        get() = ThemeProvider.IconSize()

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = Typography(
            displayLarge = h1,
            displayMedium = h1,
            displaySmall = h1,

            headlineLarge = h2,
            headlineMedium = h2,
            headlineSmall = h2,

            titleLarge = h3,
            titleMedium = h3,
            titleSmall = h4,

            bodyLarge = h5,
            bodyMedium = body,
            bodySmall = body,
            labelLarge = label,
            labelMedium = label,
            labelSmall = label,
        )

}



private val inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_extrabold, FontWeight.ExtraBold),
)

const val baseFontFeature = "ss01,ss04,cv10"

private fun Double.emToSp(): TextUnit = (this * 16).sp
private fun Int.emToSp(): TextUnit = (this * 16).sp

internal val h1 = TextStyle(
    fontFamily = inter,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 3.emToSp(),
    lineHeight = 1.em,
    letterSpacing = (-0.025).em,
    fontFeatureSettings = baseFontFeature,
)

internal val h2 = TextStyle(
    fontFamily = inter,
    fontWeight = FontWeight.Bold,
    fontSize = 2.5.emToSp(),
    lineHeight = 1.2.em,
    letterSpacing = (-0.025).em,
    fontFeatureSettings = baseFontFeature,
)

internal val h3 = TextStyle(
    fontFamily = inter,
    fontWeight = FontWeight.SemiBold,
    fontSize = 1.5.emToSp(),
    lineHeight = 1.2.em,
    letterSpacing = (-0.025).em,
    fontFeatureSettings = baseFontFeature,
)

internal val h4 = TextStyle(
    fontFamily = inter,
    fontWeight = FontWeight.SemiBold,
    fontSize = 1.25.emToSp(),
    lineHeight = 1.3.em,
    letterSpacing = (-0.025).em,
    fontFeatureSettings = baseFontFeature,
)

internal val h5 = TextStyle(
    fontFamily = inter,
    fontWeight = FontWeight.Medium,
    fontSize = 1.25.emToSp(),
    lineHeight = 1.3.em,
    letterSpacing = 0.em,
    fontFeatureSettings = baseFontFeature,
)

internal val body = TextStyle(
    fontFamily = inter,
    fontWeight = FontWeight.Normal,
    fontSize = 1.emToSp(),
    lineHeight = 1.3.em,
    letterSpacing = 0.em,
    fontFeatureSettings = baseFontFeature,
)

internal val label = TextStyle(
    fontFamily = inter,
    fontWeight = FontWeight.Medium,
    fontSize = 0.875.emToSp(),
    lineHeight = 1.2.em,
    letterSpacing = 0.em,
    fontFeatureSettings = baseFontFeature,
)
