package com.example.financemanagementappv2.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.financemanagementappv2.data.dao.BalanceDao
import com.example.financemanagementappv2.data.dao.CategoriesDao
import com.example.financemanagementappv2.data.dao.ExpensesDao
import com.example.financemanagementappv2.data.dao.FinancialGoalsDao
import com.example.financemanagementappv2.data.dao.IncomesDao
import com.example.financemanagementappv2.data.enums.EnumTypeConverter
import com.example.financemanagementappv2.data.entities.Balance
import com.example.financemanagementappv2.data.entities.Categories
import com.example.financemanagementappv2.data.entities.Expenses
import com.example.financemanagementappv2.data.entities.FinancialGoals
import com.example.financemanagementappv2.data.entities.Incomes
import com.example.financemanagementappv2.data.enums.CategoryFinancialType
import com.example.financemanagementappv2.helpers.DateHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Provider


@Database(entities = [
    Balance::class,
    Categories::class,
    Expenses::class,
    FinancialGoals::class,
    Incomes::class], version = 1)
@TypeConverters(EnumTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun balanceDao(): BalanceDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun expensesDao(): ExpensesDao
    abstract fun financialGoalsDao(): FinancialGoalsDao
    abstract fun incomesDao(): IncomesDao

    class Callback @Inject constructor(
        private val database: Provider<AppDatabase>,
        private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Can be used to execute SQL queries...
        }
    }
}