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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.financemanagementappv2.R
import com.example.financemanagementappv2.data.constants.Dimensions
import com.example.financemanagementappv2.ui.theme.FinanceManagementAppV2Theme
import com.example.financemanagementappv2.ui.theme.HeadingStyle
import com.example.financemanagementappv2.ui.theme.SmallHeadingStyle


@Preview
@Composable
fun LastMonthIncomesCard(
    modifier: Modifier = Modifier,
    incomeValue: Double = 0.0,
    incomeProgress: Float = 0f
) {
    TwoLineInfoCard(
        borderColor = FinanceManagementAppV2Theme.financeAppColors.incomesCardBorder,
        backgroundColor = FinanceManagementAppV2Theme.financeAppColors.incomesCardBackground,
        firstLineText = stringResource(R.string.incomes_card_heading_text),
        secondLineText = "$incomeValue lv",
        progress = incomeProgress,
        modifier = modifier
            .wrapContentWidth()
            .heightIn(min = 156.dp)
    )
}

@Preview
@Composable
fun LastMonthExpensesCard(
    modifier: Modifier = Modifier,
    expenseValue: Double = 0.0,
    expenseProgress: Float = 0f
) {
    TwoLineInfoCard(
        borderColor = FinanceManagementAppV2Theme.financeAppColors.expensesCardBorder,
        backgroundColor = FinanceManagementAppV2Theme.financeAppColors.expensesCardBackground,
        firstLineText = stringResource(R.string.expenses_card_heading_text),
        secondLineText = "$expenseValue lv",
        progress = expenseProgress,
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
    backgroundColor: Color,
    progress: Float,
    modifier: Modifier = Modifier
) {
    BasicInformationalCard(
        borderColor = borderColor,
        modifier = modifier.size(200.dp),
        backgroundColor = backgroundColor
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
                        progress = progress,
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
                        progress = progress,
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
    backgroundColor: Color,
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
        Box(
            modifier = Modifier.background(backgroundColor)
        ) {
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



