package com.example.financemanagementappv2.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class FinancialGoals (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val goalName: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val dueDate: Long,
    val userId: Int,
    val createdAt: Long = System.currentTimeMillis()
)
