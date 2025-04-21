package com.example.financemanagementappv2.ui.theme.cards

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.financemanagementappv2.R
import com.example.financemanagementappv2.data.enums.PeriodTab
import com.example.financemanagementappv2.ui.theme.FinanceManagementAppV2Theme
import com.example.financemanagementappv2.ui.theme.SmallHeadingStyle


@Composable
fun BalanceChartGraphCard(
    balanceData: List<Pair<Long, Double>>,
    labelFormatter: (Long) -> String,
    onTabSelected: (PeriodTab) -> Unit,
    selectedTab: PeriodTab,
    modifier: Modifier = Modifier
) {
    key(balanceData.hashCode()) {
        BasicInformationalCard(
            modifier = modifier,
            borderColor = FinanceManagementAppV2Theme.financeAppColors.balanceChartCardBorder,
            backgroundColor = FinanceManagementAppV2Theme.financeAppColors.balanceChartCardBackground
        ) {
            Column {
                HomeScreenCardHeading(text = stringResource(R.string.balance_chart_card_heading_text))
                GraphCardHeaderTabs(
                    onTabSelected = onTabSelected,
                    selectedTab = selectedTab,
                    borderColor = FinanceManagementAppV2Theme.financeAppColors.balanceChartCardBorder,
                    backgroundColor = FinanceManagementAppV2Theme.financeAppColors.balanceChartCardBackground,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (balanceData.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .height(300.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.balance_chart_no_balance_data),
                            modifier = Modifier
                                .fillMaxWidth(0.75f),
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Graph(
                        data = balanceData,
                        labelFormatter = labelFormatter,
                        chartBackgroundColor = FinanceManagementAppV2Theme.financeAppColors.balanceChartCardBackground,
                        yAxisBackgroundColor = FinanceManagementAppV2Theme.financeAppColors.balanceChartCardBackground,
                        xAxisBackgroundColor = FinanceManagementAppV2Theme.financeAppColors.balanceChartCardBackground,
                        lineColor = FinanceManagementAppV2Theme.financeAppColors.balanceChartCardBorder,
                        shadowUnderLineColor = FinanceManagementAppV2Theme.financeAppColors.balanceChartCardBorder
                    )
                }
            }
        }
    }
}


@Composable
fun GraphCardHeaderTabs(
    onTabSelected: (PeriodTab) -> Unit,
    selectedTab: PeriodTab,
    borderColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    ScrollableTabRow(
        containerColor = backgroundColor,
        modifier = modifier,
        edgePadding = 12.dp,
        selectedTabIndex = selectedTab.ordinal,
        // Overriding the default indicator and providing a custom box-based one
        indicator = { tabPositions: List<TabPosition> ->
            Box(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTab.ordinal])
                    .fillMaxSize()
                    .padding(horizontal = 2.dp)
                    .border(
                        BorderStroke(2.dp, borderColor),
                        RoundedCornerShape(10.dp)
                    )
            )
        },
        // Disable default horizontal divider
        divider = {},
    ) {
        PeriodTab.entries.forEachIndexed { index, tab ->
            val selected = index == selectedTab.ordinal
            GraphCardHeaderTab(
                onTabSelected = onTabSelected,
                selected = selected,
                tab = tab,
                index = index
            )
        }
    }
}

@Composable
fun GraphCardHeaderTab(
    onTabSelected: (PeriodTab) -> Unit,
    selected: Boolean,
    tab: PeriodTab,
    index: Int
) {
    val backgroundColor = if (selected) {
        FinanceManagementAppV2Theme.financeAppColors.balanceChartCardBackgroundSelected
    } else {
        FinanceManagementAppV2Theme.financeAppColors.balanceChartCardBackground
    }

    Tab(
        selected = selected,
        onClick = {
            onTabSelected(PeriodTab.entries[index])
        },
        unselectedContentColor = MaterialTheme.colorScheme.onBackground,
        selectedContentColor = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
            text = stringResource(id = tab.title),
            style = SmallHeadingStyle
        )
    }
}

@Composable
fun Graph(
    data: List<Pair<Long, Double>>,
    labelFormatter: (Long) -> String,
    yAxisBackgroundColor: Color = MaterialTheme.colorScheme.secondary,
    xAxisBackgroundColor: Color = MaterialTheme.colorScheme.secondary,
    chartBackgroundColor: Color = MaterialTheme.colorScheme.background,
    lineColor: Color = Color.Black,
    shadowUnderLineColor: Color = Color.Black
) {
    val sortedData = data.sortedBy { it.first }
    val pointsData = sortedData.mapIndexed { index, (timestamp, value) ->
        Point(x = index.toFloat(), y = value.toFloat())
    }

    val maxBalance = sortedData.maxOfOrNull { it.second } ?: 0.0
    val ySteps = 5
    val stepValue = maxBalance.toFloat() / ySteps

    val xAxisData = AxisData.Builder()
        .steps(sortedData.size - 1)
        .axisStepSize(400.dp / sortedData.size)
        .backgroundColor(xAxisBackgroundColor)
        .labelAndAxisLinePadding(20.dp)
        .labelData { index ->
            val timestamp = sortedData.getOrNull(index)?.first ?: 0L
            labelFormatter(timestamp)
        }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(ySteps)
        .backgroundColor(yAxisBackgroundColor)
        .labelAndAxisLinePadding(40.dp)
        .labelData { index ->
            val value = stepValue * index
            value.toInt().toString()
        }.build()

    val lineChartData = LineChartData(
        backgroundColor = chartBackgroundColor,
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(
                        color = lineColor
                    ),
                    IntersectionPoint(),
                    SelectionHighlightPoint(
                        color = lineColor
                    ),
                    ShadowUnderLine(
                        color = shadowUnderLineColor
                    ),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(
            color = com.example.financemanagementappv2.ui.theme.Gray
        ),
        containerPaddingEnd = 30.dp
    )
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = lineChartData
    )
}

