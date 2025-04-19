package com.example.financemanagementappv2.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

data class FinanceAppColorScheme(
    val header: Color = Color.Unspecified,
    val cardDefaultBackground: Color = Color.Unspecified,
    val balanceChartCardBorder: Color = Color.Unspecified,
    val balanceChartCardBackground: Color = Color.Unspecified,
    val balanceChartCardBackgroundSelected: Color = Color.Unspecified,
    val incomesCardBorder: Color = Color.Unspecified,
    val incomesCardBackground: Color = Color.Unspecified,
    val expensesCardBorder: Color = Color.Unspecified,
    val expensesCardBackground: Color = Color.Unspecified,
    val goalCompletionCardBorder: Color = Color.Unspecified,
    val goalCompletionCardBackground: Color = Color.Unspecified,
    val goalCompletionCardBubble: Color = Color.Unspecified
)
val LocalExtraColors = staticCompositionLocalOf {
    FinanceAppColorScheme()
}
private val FinanceAppColors = FinanceAppColorScheme(
    header = Purple,
    cardDefaultBackground = White,
    balanceChartCardBorder = DeepBlue,
    balanceChartCardBackground = WashedBlue,
    balanceChartCardBackgroundSelected = LightBlue,
    incomesCardBorder = DarkMintGreen,
    expensesCardBorder = Red,
    incomesCardBackground = Green,
    expensesCardBackground = LightOrange,
    goalCompletionCardBorder = DarkLilac,
    goalCompletionCardBackground = Lilac,
    goalCompletionCardBubble = DarkLilac,
)

@Composable
fun FinanceManagementAppV2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme
    val extraColors: FinanceAppColorScheme
    if (darkTheme) {
        colorScheme = DarkColorScheme
        extraColors = FinanceAppColors
    } else {
        colorScheme = LightColorScheme
        extraColors = FinanceAppColors
    }

    CompositionLocalProvider(LocalExtraColors provides extraColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object FinanceManagementAppV2Theme {
    val financeAppColors: FinanceAppColorScheme
        @Composable
        get() = LocalExtraColors.current
}