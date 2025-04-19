package com.example.financemanagementappv2.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.financemanagementappv2.data.viewmodels.HomeScreenViewModel
import com.example.financemanagementappv2.ui.theme.cards.BalanceChartGraphCard
import com.example.financemanagementappv2.ui.theme.cards.GoalCompletionCard
import com.example.financemanagementappv2.ui.theme.cards.GoalsCard
import com.example.financemanagementappv2.ui.theme.cards.LastMonthExpensesCard
import com.example.financemanagementappv2.ui.theme.cards.LastMonthIncomesCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FinanceHomeScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onDrawerClicked: () -> Unit = {}
) {
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

        // To get the safe area dimensions for the bottom and horizontal edges of the screen.
        // It takes into account things like the navigation bar or system bars, so the content doesn't get hidden behind them.
        val insets = WindowInsets.safeDrawing.only( WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal )

        val uiState = viewModel.uiState.collectAsStateWithLifecycle()
        val selectedTab = viewModel.balanceSelectedTab.collectAsStateWithLifecycle().value
        val balanceData = viewModel.filteredBalanceData.collectAsStateWithLifecycle().value

        // FlowRow Arranges children horizontally and wraps to the next line if there's no space
        // Row does not wrap and needs horizontal scroll if content overflow
        FlowRow(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(insets),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            maxItemsInEachRow = 3
        ) {
            BalanceChartGraphCard(
                balanceData = balanceData,
                labelFormatter = { viewModel.labelFormatter(it) },
                onTabSelected = { viewModel.onTabSelected(it) },
                selectedTab = selectedTab,
                modifier = Modifier.widthIn(max = 600.dp)
            )
            if (windowSizeClass == WindowWidthSizeClass.Compact) {
                LastMonthIncomesCard(
                    incomeValue = uiState.value.monthlyIncomeSum,
                    incomeProgress = uiState.value.circularOverlayIncomeProgress
                )
                LastMonthExpensesCard(
                    expenseValue = uiState.value.monthlyExpenseSum,
                    expenseProgress = uiState.value.circularOverlayExpenseProgress
                )
                GoalsCard(
                    goals = uiState.value
                )
            } else {
                // Arranges items top to bottom, like a Column
                // But if it runs out of vertical space, it wraps to the next column
                FlowColumn {
                    LastMonthIncomesCard(
                        incomeValue = uiState.value.monthlyIncomeSum,
                        incomeProgress = uiState.value.circularOverlayIncomeProgress
                    )
                    LastMonthExpensesCard(
                        expenseValue = uiState.value.monthlyExpenseSum,
                        expenseProgress = uiState.value.circularOverlayExpenseProgress
                    )
                    GoalsCard(
                        goals = uiState.value
                    )
                }
            }

            GoalCompletionCard()
        }
    }
}