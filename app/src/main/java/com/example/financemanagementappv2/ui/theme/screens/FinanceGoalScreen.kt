package com.example.financemanagementappv2.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.financemanagementappv2.R
import com.example.financemanagementappv2.ui.theme.HeadingStyle
import com.example.financemanagementappv2.ui.theme.VerySmallHeadingStyle
import com.example.financemanagementappv2.ui.theme.cards.GoalFormCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FinanceGoalScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    onDrawerClicked: () -> Unit = {}
) {

    val insets = WindowInsets.safeDrawing.only( WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal )

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
                .windowInsetsPadding(insets)
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            maxItemsInEachRow = 3
        ) {

            FlowColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(R.string.goals_screen_heading),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.goals_screen_subtext),
                    modifier = Modifier
                        .padding(bottom = 14.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = VerySmallHeadingStyle,
                    color = Color.Gray
                )

                GoalFormCard(
                    modifier = Modifier.widthIn(max = 400.dp)
                )
            }
        }

//        FlowRow(
//            modifier = Modifier
//                .fillMaxSize()
//                .windowInsetsPadding(insets),
//            horizontalArrangement = Arrangement.Center,
//            verticalArrangement = Arrangement.Center,
//            maxItemsInEachRow = 3
//        ) {
//
//        }
    }
}