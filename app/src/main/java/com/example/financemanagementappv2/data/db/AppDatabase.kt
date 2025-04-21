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

            applicationScope.launch {

                database.get().categoriesDao().insertAll(
                    // Income Categories
                    Categories(name = "Salary", type = CategoryFinancialType.INCOME),
                    Categories(name = "Freelancing", type = CategoryFinancialType.INCOME),
                    Categories(name = "Investments", type = CategoryFinancialType.INCOME),
                    Categories(name = "Bonus", type = CategoryFinancialType.INCOME),
                    Categories(name = "Side Business", type = CategoryFinancialType.INCOME),
                    Categories(name = "Rental Income", type = CategoryFinancialType.INCOME),

                    // Expense Categories
                    Categories(name = "Groceries", type = CategoryFinancialType.EXPENSE),
                    Categories(name = "Rent", type = CategoryFinancialType.EXPENSE),
                    Categories(name = "Utilities", type = CategoryFinancialType.EXPENSE),
                    Categories(name = "Dining Out", type = CategoryFinancialType.EXPENSE),
                    Categories(name = "Subscriptions", type = CategoryFinancialType.EXPENSE),
                    Categories(name = "Transport", type = CategoryFinancialType.EXPENSE),
                    Categories(name = "Entertainment", type = CategoryFinancialType.EXPENSE)
                )

                /*
                database.get().incomesDao().insertAll(
                    // January
                    Incomes(amount = 3000.0, description = "Monthly Salary", date = getTimestamp(2025, 0, 3), categoryId = 1),
                    Incomes(amount = 500.0, description = "Freelance Logo Design",date =  getTimestamp(2025, 0, 17), categoryId = 2),

                    // February
                    Incomes(amount = 3100.0, description = "Monthly Salary", date = getTimestamp(2025, 1, 5), categoryId = 1),
                    Incomes(amount = 600.0, description = "Consulting", date = getTimestamp(2025, 1, 19), categoryId = 2),

                    // March
                    Incomes(amount = 3200.0, description = "Monthly Salary", date = getTimestamp(2025, 2, 3), categoryId = 1),
                    Incomes(amount = 550.0, description = "Crypto Trading", date = getTimestamp(2025, 2, 21), categoryId = 3),

                    // April
                    Incomes(amount = 3300.0, description = "Monthly Salary", date = getTimestamp(2025, 3, 4), categoryId = 1),
                    Incomes(amount = 480.0, description = "Course Sales", date = getTimestamp(2025, 3, 14), categoryId =  2)
                )

                database.get().expensesDao().insertAll(
                    // January
                    Expenses(amount = 800.0, description = "Rent", date = getTimestamp(2025, 0, 1), categoryId = 5),
                    Expenses(amount = 150.0, description = "Groceries", date = getTimestamp(2025, 0, 10), categoryId = 4),
                    Expenses(amount = 60.0, description = "Movies & Dining", date = getTimestamp(2025, 0, 22), categoryId = 6),

                    // February
                    Expenses(amount = 800.0, description = "Rent", date = getTimestamp(2025, 1, 1), categoryId = 5),
                    Expenses(amount = 140.0, description = "Groceries", date = getTimestamp(2025, 1, 11), categoryId = 4),
                    Expenses(amount = 70.0, description = "Streaming & Bars", date = getTimestamp(2025, 1, 23), categoryId = 6),

                    // March
                    Expenses(amount = 800.0, description = "Rent", date = getTimestamp(2025, 2, 1), categoryId = 5),
                    Expenses(amount = 160.0, description = "Groceries", date = getTimestamp(2025, 2, 12), categoryId = 4),
                    Expenses(amount = 90.0, description = "Weekend Trip", date = getTimestamp(2025, 2, 25), categoryId = 6),

                    // April
                    Expenses(amount = 800.0, description = "Rent", date = getTimestamp(2025, 3, 1), categoryId = 5),
                    Expenses(amount = 150.0, description = "Groceries", date = getTimestamp(2025, 3, 10), categoryId = 4),
                    Expenses(amount = 85.0, description = "Games & Subscriptions", date = getTimestamp(2025, 3, 20), categoryId = 6)
                )

                val totalIncome = database.get().incomesDao().getTotalSum()
                val totalExpenses = database.get().expensesDao().getTotalSum()
                val initialBalance = totalIncome - totalExpenses

                database.get().balanceDao().insert(
                    Balance(amount = initialBalance, timestamp = System.currentTimeMillis())
                )

                database.get().financialGoalsDao().insertAll(
                    FinancialGoals(
                        goalName = "Vacation to Greece",
                        targetAmount = 4000.0,
                        dueDate = System.currentTimeMillis() + 60L * 24 * 60 * 60 * 1000 // in 60 days
                    ),
                    FinancialGoals(
                        goalName = "New Laptop",
                        targetAmount = 1200.0,
                        dueDate = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000 // in 30 days
                    ),
                    FinancialGoals(
                        goalName = "Emergency Fund",
                        targetAmount = 5000.0,
                        dueDate = System.currentTimeMillis() + 180L * 24 * 60 * 60 * 1000 // in 180 days
                    )
                )
                */
            }
        }

        fun getTimestamp(year: Int, month: Int, day: Int): Long {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar.timeInMillis
        }
    }
}