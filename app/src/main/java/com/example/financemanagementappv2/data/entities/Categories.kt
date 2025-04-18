package com.example.financemanagementappv2.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.financemanagementappv2.data.enums.CategoryFinancialType

@Entity
data class Categories (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: CategoryFinancialType
)
