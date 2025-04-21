package com.example.financemanagementappv2.helpers

import androidx.compose.ui.graphics.Color

object RandomColorHelper {

    private val baseColors = listOf(
        Color(0xFF333333),
        Color(0xFF666A86),
        Color(0xFF95B8D1),
        Color(0xFFF53844),
        Color(0xFF4CAF50),
        Color(0xFFFFC107),
        Color(0xFF03A9F4),
        Color(0xFF9C27B0),
        Color(0xFFE91E63),
        Color(0xFF795548),
        Color(0xFF2196F3),
        Color(0xFF8BC34A),
        Color(0xFFFF5722),
        Color(0xFF00BCD4),
        Color(0xFFCDDC39),
        Color(0xFF607D8B),
        Color(0xFFFF9800),
        Color(0xFF3F51B5),
        Color(0xFF673AB7),
        Color(0xFF009688)
    )

    fun generateColors(count: Int): List<Color> {
        val colorPool = mutableListOf<Color>()
        while (colorPool.size < count) {
            colorPool.addAll(baseColors.shuffled())
        }
        return colorPool.take(count)
    }
}