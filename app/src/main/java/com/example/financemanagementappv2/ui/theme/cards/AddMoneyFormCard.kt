package com.example.financemanagementappv2.ui.theme.cards

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financemanagementappv2.R
import com.example.financemanagementappv2.data.entities.Categories
import kotlin.collections.forEach


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoneyFormCard(
    addedMoney: Double,
    selectedCategory: Categories?,
    onMoneyChange: (Double) -> Unit,
    onCategorySelect: (Categories) -> Unit,
    onButtonPress: () -> Unit,
    categories: List<Categories>,
    formHeadingText: String,
    moneyInputHeadingText: String,
    buttonText: String,
    moneyCategoryText: String,
    modifier: Modifier = Modifier
) {
    FlowColumn (
        modifier = modifier
            .fillMaxHeight()
            .widthIn(max = 400.dp),
        horizontalArrangement = Arrangement.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        Text(
            text = formHeadingText,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = moneyInputHeadingText,
            style = MaterialTheme.typography.bodyLarge
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = if (addedMoney > 0.0) addedMoney.toString() else "",
            onValueChange = { onMoneyChange(it.toDoubleOrNull() ?: 0.0) },
            placeholder = { Text(text = "e.g. 1200") },
            maxLines = 1,
            textStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
        )

        val context = LocalContext.current

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                when {
                    addedMoney == 0.0 -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.money_form_error_invalid_money),
                            Toast.LENGTH_SHORT

                        ).show()
                    }
                    selectedCategory == null -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.money_form_error_no_category),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        onButtonPress()

                        Toast.makeText(
                            context,
                            context.getString(R.string.money_form_successfully_added),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            },
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = moneyCategoryText,
            style = MaterialTheme.typography.bodyLarge
        )
        FlowRow (
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 2
        ) {
            categories.forEach { category ->
                FilterChip(
                    selected = selectedCategory?.name == category.name,
                    onClick = { onCategorySelect(category) },
                    label = { Text(category.name) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Selected"
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}