package com.example.financemanagementappv2.ui.theme.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import co.yml.charts.ui.piechart.models.PieChartData.Slice
import com.example.financemanagementappv2.R
import com.example.financemanagementappv2.helpers.RandomColorHelper

@Composable
fun PieChartCard(
    data: Map<String, Double>,
) {
    key(data.hashCode()) {
        if (data.isEmpty()) {
            Text(
                text = stringResource(R.string.pie_chart_no_stats_text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 100.dp),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

        } else {
            val colors = RandomColorHelper.generateColors(data.keys.size)

            val slices = data.entries.mapIndexed { index, entry ->
                Slice(
                    label = entry.key,
                    value = entry.value.toFloat(),
                    color = colors[index % colors.size]
                )
            }

            val pieChartData = PieChartData(
                slices = slices,
                plotType = PlotType.Pie
            )

            val pieChartConfig = PieChartConfig(
                isAnimationEnable = true,
                animationDuration = 500,
                backgroundColor = MaterialTheme.colorScheme.background
            )

            PieChart(
                modifier = Modifier
                    .width(400.dp)
                    .height(400.dp),
                pieChartData,
                pieChartConfig
            )

            PieChartLegend(
                slices = slices
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PieChartLegend(
    slices: List<Slice>
) {
    FlowRow(
        maxItemsInEachRow = 3,
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
    ) {
        slices.forEach { slice ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(slice.color)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = slice.label,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}