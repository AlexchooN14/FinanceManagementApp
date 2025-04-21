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
                    Categories(name = "Salary", type = CategoryFinancialType.INCOME),
                    Categories(name = "Freelancing", type = CategoryFinancialType.INCOME),
                    Categories(name = "Investments", type = CategoryFinancialType.INCOME),
                    Categories(name = "Groceries", type = CategoryFinancialType.EXPENSE),
                    Categories(name = "Rent", type = CategoryFinancialType.EXPENSE),
                    Categories(name = "Entertainment", type = CategoryFinancialType.EXPENSE)
                )

                database.get().incomesDao().insertAll(
                    Incomes(amount = 3000.00, description = "Monthly Salary", date = DateHelper.getDateForDay(1), categoryId = 1),
                    Incomes(amount = 500.00, description = "Freelance project", date = DateHelper.getDateForDay(3), categoryId = 2),
                    Incomes(amount = 120.00, description = "Investment dividend", date = DateHelper.getDateForDay(5), categoryId = 3),
                    Incomes(amount = 200.00, description = "Online course revenue", date = DateHelper.getDateForDay(7), categoryId = 2),
                    Incomes(amount = 75.00, description = "Gift from family", date = DateHelper.getDateForDay(9), categoryId = 1),
                    Incomes(amount = 320.00, description = "Freelance design", date = DateHelper.getDateForDay(12), categoryId = 2),
                    Incomes(amount = 150.00, description = "Stock sale", date = DateHelper.getDateForDay(15), categoryId = 3),
                    Incomes(amount = 210.00, description = "Bonus", date = DateHelper.getDateForDay(18), categoryId = 1),
                    Incomes(amount = 90.00, description = "Podcast revenue", date = DateHelper.getDateForDay(22), categoryId = 2),
                    Incomes(amount = 130.00, description = "Crypto earnings", date = DateHelper.getDateForDay(28), categoryId = 3)
                )

                database.get().expensesDao().insertAll(
                    Expenses(amount = 45.50, description = "Groceries at local market", date = DateHelper.getDateForDay(1), categoryId = 4),
                    Expenses(amount = 1200.00, description = "April Rent", date = DateHelper.getDateForDay(2), categoryId = 5),
                    Expenses(amount = 60.00, description = "Dinner with friends", date = DateHelper.getDateForDay(4), categoryId = 6),
                    Expenses(amount = 30.25, description = "Snacks and drinks", date = DateHelper.getDateForDay(6), categoryId = 4),
                    Expenses(amount = 15.00, description = "Movie ticket", date = DateHelper.getDateForDay(8), categoryId = 6),
                    Expenses(amount = 100.00, description = "Monthly subscriptions", date = DateHelper.getDateForDay(10), categoryId = 6),
                    Expenses(amount = 90.00, description = "Pet supplies", date = DateHelper.getDateForDay(13), categoryId = 4),
                    Expenses(amount = 65.75, description = "Taxi fare", date = DateHelper.getDateForDay(16), categoryId = 6),
                    Expenses(amount = 38.00, description = "Gym membership", date = DateHelper.getDateForDay(20), categoryId = 6),
                    Expenses(amount = 22.30, description = "Lunch at cafe", date = DateHelper.getDateForDay(25), categoryId = 4)
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

            }
        }
    }
}