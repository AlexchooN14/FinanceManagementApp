package com.example.financemanagementappv2

import android.content.Context
import androidx.room.Room
import com.example.financemanagementappv2.data.dao.ExpensesDao
import com.example.financemanagementappv2.data.dao.IncomesDao
import com.example.financemanagementappv2.data.db.AppDatabase
import com.example.financemanagementappv2.data.entities.Expenses
import com.example.financemanagementappv2.data.repositories.ExpensesRepository
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
    fun provideIncomesRepository(incomesDao: IncomesDao): IncomesRepository {
        return IncomesRepository(incomesDao)
    }

    @Provides
    @Singleton
    fun provideExpensesDao(appDatabase: AppDatabase): ExpensesDao {
        return appDatabase.expensesDao()
    }

    @Provides
    @Singleton
    fun provideExpensesRepository(expensesDao: ExpensesDao): ExpensesRepository {
        return ExpensesRepository(expensesDao)
    }
}