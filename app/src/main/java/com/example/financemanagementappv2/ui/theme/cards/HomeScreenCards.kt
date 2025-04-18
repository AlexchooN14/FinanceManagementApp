package com.example.financemanagementappv2.ui.theme.cards

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import android.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.financemanagementappv2.R
import com.example.financemanagementappv2.data.constants.Dimensions
import com.example.financemanagementappv2.data.entities.Balance
import com.example.financemanagementappv2.helpers.Graphable
import com.example.financemanagementappv2.ui.theme.FinanceManagementAppV2Theme
import com.example.financemanagementappv2.ui.theme.HeadingStyle
import com.example.financemanagementappv2.ui.theme.SmallHeadingStyle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun BalanceChartGraphCard(
    modifier: Modifier = Modifier,
    graphableData: List<Graphable>
) {
    BasicInformationalCard(
        borderColor = FinanceManagementAppV2Theme.financeAppColors.balanceChartCard,
        modifier = Modifier.height(260.dp)
    ) {
        Column(
            modifier = Modifier
            .fillMaxSize()
        ) {
            HomeScreenCardHeading(text = stringResource(R.string.balance_chart_card_heading_text))
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                BalanceGraph(
                    data = graphableData
                )
            }
        }
    }
}

@Preview
@Composable
fun LastMonthIncomesCard(
    modifier: Modifier = Modifier,
    incomeValue: Double = 0.0,
) {
    TwoLineInfoCard(
        borderColor = FinanceManagementAppV2Theme.financeAppColors.incomesCard,
        firstLineText = stringResource(R.string.incomes_card_heading_text),
        secondLineText = "$incomeValue lv",
        icon = Icons.Default.Add,
        modifier = modifier
            .wrapContentWidth()
            .heightIn(min = 156.dp)
    )
}

@Preview
@Composable
fun LastMonthExpensesCard(
    modifier: Modifier = Modifier,
    expenseValue: Double = 0.0
) {
    TwoLineInfoCard(
        borderColor = FinanceManagementAppV2Theme.financeAppColors.expensesCard,
        firstLineText = stringResource(R.string.expenses_card_heading_text),
        secondLineText = "$expenseValue lv",
        icon = Icons.Default.Clear,
        modifier = modifier
            .wrapContentWidth()
            .heightIn(min = 156.dp)
    )
}

@Preview
@Composable
fun GoalCompletionCard(modifier: Modifier = Modifier) {

}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun TwoLineInfoCard(
    borderColor: Color,
    firstLineText: String,
    secondLineText: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    BasicInformationalCard(
        borderColor = borderColor,
        modifier = modifier.size(200.dp)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
        ) {
            if (maxWidth > 400.dp) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(CenterStart)
                ) {
                    CircularProgressOverlay(
                        progress = 0.7f,
                        strokeColor = borderColor,
                        strokeWidth = Dimensions.CircularProgressStroke,
                        modifier = Modifier.Companion
                            .size(Dimensions.HorizontalCircularProgressSize)
                            .padding( start = 12.dp )
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .align(CenterVertically)
                            .wrapContentSize()
                    ) {
                        Text(
                            firstLineText,
                            style = SmallHeadingStyle
                        )
                        Text(
                            secondLineText,
                            style = HeadingStyle,
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = CenterHorizontally
                ) {
                    CircularProgressOverlay(
                        progress = 0.7f,
                        strokeColor = borderColor,
                        strokeWidth = Dimensions.CircularProgressStroke,
                        modifier = Modifier.Companion
                            .size(Dimensions.VerticalCircularProgressSize)
                            .padding( start = 6.dp )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier.align(CenterHorizontally)) {
                        Text(
                            firstLineText,
                            style = SmallHeadingStyle,
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Text(
                            secondLineText,
                            style = HeadingStyle,
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CircularProgressOverlay(
    progress: Float,
    strokeColor: Color = Color.Blue,
    strokeWidth: Dp = 8.dp,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) progress else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Canvas(modifier = modifier) {
        val stroke = strokeWidth.toPx()
        val size = size.minDimension
        val radius = size / 2f
        val topLeft = Offset((this.size.width - size) / 2f, (this.size.height - size) / 2f)
        drawArc(
            color = strokeColor,
            startAngle = -90f,
            sweepAngle = 360f * animatedProgress,
            useCenter = false,
            style = Stroke(width = stroke, cap = StrokeCap.Round),
            topLeft = topLeft,
            size = Size(size, size)
        )
    }
}

@Composable
fun BasicInformationalCard(
    modifier: Modifier = Modifier,
    borderColor: Color,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = FinanceManagementAppV2Theme.financeAppColors.cardDefaultBackground
        ),
        modifier = modifier
            .padding(8.dp),
        border = BorderStroke(3.dp, borderColor)
    ) {
        Box {
            content()
        }
    }
}

@Composable
fun HomeScreenCardHeading(text: String) {
    Text(
        text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        textAlign = TextAlign.Center,
        style = HeadingStyle
    )
}

@Composable
fun BalanceGraph(data: List<Graphable>) {
    Box(Modifier.width(400.dp)) {
        Graph(
            modifier = Modifier.padding(16.dp),
            data = data
        )
    }
}


@Composable
fun Graph(
    modifier: Modifier = Modifier,
    data: List<Graphable>,
    verticalStepsCount: Int = 10,
    horizontalStepsCount: Int = 10,
) {
    val dataValuesList: List<Double> = data.map { it.getData() }
    val dataTimestampsList: List<Long> = data.map { it.getDataTimestamp() }
    // val dataMap: Map<Long, Double> = data.associate { it.getDataTimestamp() to it.getData() }

    val minY = dataValuesList.minOrNull() ?: 0.0
    val maxY = dataValuesList.maxOrNull() ?: 0.0
    val yRange = maxY - minY
    val yStep = if (yRange == 0.0) 1.0 else yRange / verticalStepsCount

    val minX = dataTimestampsList.minOrNull() ?: 0L
    val maxX = dataTimestampsList.maxOrNull() ?: 0L
    val xRange = (maxX - minX).toFloat()
    val xStep = if (xRange == 0f) 1f else xRange / horizontalStepsCount

    val points = data.map {
        val normalizedX = (it.getDataTimestamp() - minX).toFloat() / xRange
        val normalizedY = (it.getData() - minY) / yRange
        normalizedX to normalizedY
    }

    val textPaint = remember {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = 30f
        }
    }

    Box(
        modifier = modifier
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val paddingX = width * 0.1f
            val paddingY = height * 0.1f

            // Draw Y-axis labels
            for (i in 0..verticalStepsCount) {
                val value = minY + (i * yStep)
                val y = height - paddingY - (i.toFloat() / verticalStepsCount * (height - 2 * paddingY))
                drawContext.canvas.nativeCanvas.drawText(
                    String.format("%.1f", value),
                    paddingX / 2f,
                    y,
                    textPaint
                )
            }

            // Draw X-axis labels
            for (i in 0..horizontalStepsCount) {
                val timestamp = minX + (i * xStep)
                val x = paddingX + (i.toFloat() / horizontalStepsCount * (width - 2 * paddingX))
                val date = SimpleDateFormat("MM-dd", Locale.getDefault()).format(timestamp)
                drawContext.canvas.nativeCanvas.drawText(
                    date,
                    x,
                    height - paddingY / 2f,
                    textPaint
                )
            }

            // Calculate canvas points
            val canvasPoints = points.map { (normX, normY) ->
                Offset(
                    x = paddingX + normX * (width - 2 * paddingX),
                    y = height - paddingY - normY.toFloat() * (height - 2 * paddingY)
                )
            }

            // Draw Bézier curve
            val path = Path().apply {
                if (canvasPoints.isNotEmpty()) {
                    moveTo(canvasPoints.first().x, canvasPoints.first().y)
                    for (i in 1 until canvasPoints.size) {
                        val prev = canvasPoints[i - 1]
                        val curr = canvasPoints[i]
                        val controlPoint1 = Offset((prev.x + curr.x) / 2, prev.y)
                        val controlPoint2 = Offset((prev.x + curr.x) / 2, curr.y)
                        cubicTo(
                            controlPoint1.x, controlPoint1.y,
                            controlPoint2.x, controlPoint2.y,
                            curr.x, curr.y
                        )
                    }
                }
            }

            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(width = 5f, cap = StrokeCap.Round)
            )

            // Draw data points
            canvasPoints.forEach { point ->
                drawCircle(
                    color = Color.Red,
                    radius = 10f,
                    center = point
                )
            }
        }
    }
}