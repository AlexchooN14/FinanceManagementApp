package com.example.financemanagementappv2.data.repositories

import com.example.financemanagementappv2.data.dao.CategoriesDao
import com.example.financemanagementappv2.data.enums.CategoryFinancialType
import com.example.financemanagementappv2.data.entities.Categories
import kotlinx.coroutines.flow.Flow


class CategoriesRepository(private val categoriesDao: CategoriesDao) {

    suspend fun insertAll(vararg categories: Categories) {
        categoriesDao.insertAll(*categories)
    }

    suspend fun insertAll(categories: List<Categories>) {
        categories.forEach { categoriesDao.insertAll(it) }
    }

    suspend fun delete(category: Categories) {
        categoriesDao.delete(category)
    }

    fun getCategoryByCategoryId(id: Long): Categories {
        return categoriesDao.getCategoryByCategoryId(id)
    }

    fun getAllExpenseCategories(): Flow<List<Categories>> {
        return categoriesDao.getAllCategoriesByFinancialType(CategoryFinancialType.EXPENSE)
    }

    fun getAllIncomeCategories(): Flow<List<Categories>> {
        return categoriesDao.getAllCategoriesByFinancialType(CategoryFinancialType.INCOME)
    }

    fun getAllCategories(): Flow<List<Categories>> {
        return categoriesDao.getAllCategories()
    }

}