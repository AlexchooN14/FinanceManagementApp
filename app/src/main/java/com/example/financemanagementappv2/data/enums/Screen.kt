package com.example.financemanagementappv2.data.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(val text: String, val icon: ImageVector) {
    Home("Home", Icons.Default.Home),
    Income("Add Income", Icons.Default.KeyboardArrowUp),
    Expense("Add Expense", Icons.Default.KeyboardArrowDown),
    Settings("Settings", Icons.Default.Settings),
}