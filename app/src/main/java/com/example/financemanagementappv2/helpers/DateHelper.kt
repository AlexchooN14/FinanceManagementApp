package com.example.financemanagementappv2.helpers

import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateHelper {
    val Int.days: Long get() = this * 24L * 60 * 60 * 1000

    val Int.months: Long get() = this * 30L * 24 * 60 * 60 * 1000

    fun Int.toMonthName(): String {
        return DateFormatSymbols().months.getOrElse(this.coerceIn(0, 11)) { "Unknown" }
    }

    fun Date.toFormattedString(): String {
        val simpleDateFormat = SimpleDateFormat("LLLL dd, yyyy", Locale.getDefault())
        return simpleDateFormat.format(this)
    }

    fun getStartOfCurrentMonthInMillis(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    fun getEndOfCurrentMonthInMillis(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }

    fun getStartOfCurrentYearInMillis(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    fun getEndOfCurrentYearInMillis(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.DECEMBER)
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }

    fun getDateForDay(day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, 12)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun getLastMonthsAsStrings(count: Int): List<String> {
        val calendar = Calendar.getInstance()
        return (0 until count).map {
            val monthName = SimpleDateFormat("MMM", Locale.ENGLISH).format(calendar.time)
            calendar.add(Calendar.MONTH, -1)
            monthName
        }.reversed()
    }
}