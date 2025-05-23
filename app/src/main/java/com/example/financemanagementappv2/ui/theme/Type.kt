package com.example.financemanagementappv2.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val HeaderTitleStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 22.sp,
    letterSpacing = 0.5.sp,
)

val SmallHeadingStyle = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight(600),
    letterSpacing = 0.5.sp,
    fontFamily = FontFamily.Default,
    color = BlackGray
)

val VerySmallHeadingStyle = TextStyle(
    fontSize = 10.sp,
    fontWeight = FontWeight(400),
    letterSpacing = 0.5.sp,
    fontFamily = FontFamily.Default
)

val HeadingStyle = TextStyle(
    fontSize = 24.sp,
    fontWeight = FontWeight(600),
    letterSpacing = 0.5.sp,
    fontFamily = FontFamily.Default,
    color = BlackGray
)
