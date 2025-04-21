package com.example.financemanagementappv2.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.financemanagementappv2.R
import com.example.financemanagementappv2.data.entities.Categories
import com.example.financemanagementappv2.data.viewmodels.ExpenseScreenViewModel
import com.example.financemanagementappv2.ui.theme.HeadingStyle
import com.example.financemanagementappv2.ui.theme.cards.MoneyFormCard
import com.example.financemanagementappv2.ui.theme.cards.PieChartCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FinanceExpenseScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    viewModel: ExpenseScreenViewModel = hiltViewModel(),
    onDrawerClicked: () -> Unit = {}
) {
    // To get the safe area dimensions for the bottom and horizontal edges of the screen.
    // It takes into account things like the navigation bar or system bars, so the content doesn't get hidden behind them.
    val insets = WindowInsets.safeDrawing.only( WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal )

    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column() {
            FinanceHeader(
                modifier = modifier.fillMaxWidth(),
                onDrawerClicked = onDrawerClicked
            )
        }

        FlowRow(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(insets),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            maxItemsInEachRow = 3
        ) {
            Text(
                text = stringResource(R.string.expense_screen_stats_heading),
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = HeadingStyle
            )

            PieChartCard(
                data = uiState.value.categoryExpensesMapped
            )

            Spacer(modifier = Modifier.height(16.dp))

            var addedMoney by rememberSaveable { mutableDoubleStateOf(0.0) }
            var selectedCategory by rememberSaveable { mutableStateOf<Categories?>(null) }
            val insets = WindowInsets.safeDrawing.only( WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal )

            MoneyFormCard(
                addedMoney = addedMoney,
                selectedCategory = selectedCategory,
                onMoneyChange = { addedMoney = it },
                onCategorySelect = { selectedCategory = it },
                onButtonPress = {
                    viewModel.insertExpense(addedMoney, selectedCategory!!)
                    addedMoney = 0.0
                    selectedCategory = null
                },
                categories = uiState.value.allExpenseCategories,
                formHeadingText = stringResource(id = R.string.expense_screen_money_form_form_heading),
                moneyInputHeadingText = stringResource(id = R.string.expense_screen_money_form_field_text),
                buttonText = stringResource(id = R.string.expense_screen_money_form_form_heading),
                moneyCategoryText = stringResource(id = R.string.expense_screen_money_form_categories_text),


                modifier = Modifier.windowInsetsPadding(insets)
            )
        }
    }
}