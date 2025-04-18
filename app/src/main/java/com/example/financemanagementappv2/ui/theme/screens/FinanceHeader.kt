package com.example.financemanagementappv2.ui.theme.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.financemanagementappv2.R
import com.example.financemanagementappv2.ui.theme.HeaderTitleStyle

@Preview
@Composable
fun FinanceHeader(
    modifier: Modifier = Modifier,
    onDrawerClicked: () -> Unit = {}
) {
    Box(
        modifier.height(100.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton( onClick = onDrawerClicked ) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = stringResource(R.string.finance_navigation_menu)
                )
            }

            Text(
                text = stringResource(R.string.app_name),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                style = HeaderTitleStyle,
                textAlign = TextAlign.Start
            )
        }
    }

}