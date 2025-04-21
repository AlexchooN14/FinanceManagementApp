package com.example.financemanagementappv2

import android.content.Context
import androidx.room.Room
import com.example.financemanagementappv2.data.dao.BalanceDao
import com.example.financemanagementappv2.data.dao.CategoriesDao
import com.example.financemanagementappv2.data.dao.ExpensesDao
import com.example.financemanagementappv2.data.dao.FinancialGoalsDao
import com.example.financemanagementappv2.data.dao.IncomesDao
import com.example.financemanagementappv2.data.db.AppDatabase
import com.example.financemanagementappv2.data.entities.Expenses
import com.example.financemanagementappv2.data.repositories.BalanceRepository
import com.example.financemanagementappv2.data.repositories.CategoriesRepository
import com.example.financemanagementappv2.data.repositories.ExpensesRepository
import com.example.financemanagementappv2.data.repositories.FinancialGoalsRepository
import com.example.financemanagementappv2.data.repositories.IncomesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        callback: AppDatabase.Callback
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "FinanceAppDB"
        ).addCallback(callback).build()
    }

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }


    @Provides
    @Singleton
    fun provideIncomesDao(appDatabase: AppDatabase): IncomesDao {
        return appDatabase.incomesDao()
    }

    @Provides
    @Singleton
    fun provideIncomesRepository(incomesDao: IncomesDao, balanceDao: BalanceDao): IncomesRepository {
        return IncomesRepository(incomesDao, balanceDao)
    }

    @Provides
    @Singleton
    fun provideExpensesDao(appDatabase: AppDatabase): ExpensesDao {
        return appDatabase.expensesDao()
    }

    @Provides
    @Singleton
    fun provideExpensesRepository(expensesDao: ExpensesDao, balanceDao: BalanceDao): ExpensesRepository {
        return ExpensesRepository(expensesDao, balanceDao)
    }

    @Provides
    @Singleton
    fun provideBalanceDao(appDatabase: AppDatabase): BalanceDao {
        return appDatabase.balanceDao()
    }

    @Provides
    @Singleton
    fun provideBalanceRepository(balanceDao: BalanceDao): BalanceRepository {
        return BalanceRepository(balanceDao)
    }

    @Provides
    @Singleton
    fun provideFinancialGoalsDao(appDatabase: AppDatabase): FinancialGoalsDao {
        return appDatabase.financialGoalsDao()
    }

    @Provides
    @Singleton
    fun provideFinancialGoalsRepository(financialGoalsDao: FinancialGoalsDao): FinancialGoalsRepository {
        return FinancialGoalsRepository(financialGoalsDao)
    }

    @Provides
    @Singleton
    fun provideCategoriesDao(appDatabase: AppDatabase): CategoriesDao {
        return appDatabase.categoriesDao()
    }

    @Provides
    @Singleton
    fun provideCategoriesRepository(categoriesDao: CategoriesDao): CategoriesRepository {
        return CategoriesRepository(categoriesDao)
    }

}