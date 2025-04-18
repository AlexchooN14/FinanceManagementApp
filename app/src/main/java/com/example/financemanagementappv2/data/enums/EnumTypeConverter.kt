package com.example.financemanagementappv2.data.enums

import androidx.room.TypeConverter

class EnumTypeConverter {
    @TypeConverter
    fun toInteger(value: CategoryFinancialType): Int = value.ordinal
}