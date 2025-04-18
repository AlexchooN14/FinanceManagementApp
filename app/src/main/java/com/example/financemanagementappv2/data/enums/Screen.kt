package com.example.financemanagementappv2.data.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(val text: String, val icon: ImageVector) {
    Home("Home", Icons.Default.Home),
    // Add more Screen states
    Settings("Settings", Icons.Default.Settings),
}