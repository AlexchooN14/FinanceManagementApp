package com.example.financemanagementappv2.ui.theme.cards

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financemanagementappv2.R
import com.example.financemanagementappv2.data.entities.FinancialGoals
import com.example.financemanagementappv2.ui.theme.FinanceManagementAppV2Theme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoalsCard(
    modifier: Modifier = Modifier,
    goals: List<FinancialGoals>,
    currentBalance: Double
) {
    BasicInformationalCard(
        modifier = modifier
            .widthIn(max = 400.dp)
            .heightIn(min = 200.dp),
        borderColor = FinanceManagementAppV2Theme.financeAppColors.goalCompletionCardBorder,
        backgroundColor = FinanceManagementAppV2Theme.financeAppColors.goalCompletionCardBackground
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(vertical = 12.dp)
        ) {
            HomeScreenCardHeading(text = stringResource(R.string.goals_card_heading_text))
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                if (goals.isEmpty()) {
                    Text(
                        text = stringResource(R.string.goals_card_no_goals_data),
                        modifier = Modifier.fillMaxWidth(0.75f),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    goals.forEach { goal ->
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Text(
                                text = goal.goalName,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(50.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            GoalBubble(
                                completionRate = ((currentBalance / goal.targetAmount ) * 100).coerceIn(0.0, 100.0).toInt().toString(),
                                metric = "%",
                                bubbleColor = FinanceManagementAppV2Theme.financeAppColors.goalCompletionCardBubble
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GoalBubble(
    completionRate: String,
    metric: String,
    bubbleColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(4.dp)
            .sizeIn(maxHeight = 100.dp)
            .aspectRatio(1f)
            .drawBehind {
                drawCircle(bubbleColor)
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(completionRate, fontSize = 32.sp)
        Text(metric, fontSize = 20.sp)
    }
}