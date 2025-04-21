package com.example.financemanagementappv2.data.enums

import androidx.room.TypeConverter

class EnumTypeConverter {
    @TypeConverter
    fun toInteger(value: CategoryFinancialType): Int = value.ordinal

    @TypeConverter
    fun toCategoryFinancialType(value: Int): CategoryFinancialType {
        return CategoryFinancialType.values().first { it.type == value }
    }
}