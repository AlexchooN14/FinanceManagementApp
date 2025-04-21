package com.example.financemanagementappv2.ui.theme.cards

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.financemanagementappv2.R
import com.example.financemanagementappv2.data.viewmodels.GoalScreenViewModel
import com.example.financemanagementappv2.helpers.DateHelper.toFormattedString
import com.example.financemanagementappv2.helpers.DateHelper.toMonthName
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoalFormCard(
    modifier: Modifier = Modifier,
    viewModel: GoalScreenViewModel = hiltViewModel()
) {
    var goalName by rememberSaveable { mutableStateOf("") }
    var startDate by rememberSaveable { mutableLongStateOf(Date().time) }
    var endDate by rememberSaveable { mutableLongStateOf(Date().time) }
    var addedMoney by rememberSaveable { mutableDoubleStateOf(0.0) }
    var sliderPosition by rememberSaveable { mutableFloatStateOf(0f) }
    val context = LocalContext.current

    FlowColumn(
        modifier = modifier
            .fillMaxHeight()
            .widthIn(max = 400.dp),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = goalName,
            onValueChange = { goalName = it },
            placeholder = { Text(text = "e.g. New Laptop") },
            maxLines = 1,
            textStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
        )

        DatePickerTextField(
            textFieldLabel = stringResource(id = R.string.goals_screen_start_date_label),
            onSelectedDate = { startDate = it }
        )

        DatePickerTextField(
            textFieldLabel = stringResource(id = R.string.goals_screen_end_date_label),
            onSelectedDate = { endDate = it }
        )

        Text(
            text = stringResource(R.string.goals_screen_amount_heading),
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = if (addedMoney > 0.0) addedMoney.toString() else "",
            onValueChange = { addedMoney = (it.toDoubleOrNull() ?: 0.0) },
            placeholder = { Text(text = "e.g. 1200") },
            maxLines = 1,
            textStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
        )

        Spacer(modifier = Modifier.height(10.dp))

        SteppedSlider(
            steps = 9,
            sliderPosition = sliderPosition,
            onSliderChange = {
                val rounded = (it / 5).roundToInt() * 5
                sliderPosition = rounded.toFloat()
                addedMoney = (rounded * 100).toDouble()
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                when {
                    goalName.isEmpty() -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.goals_screen_error_no_goal_name),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    startDate == endDate -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.goals_screen_error_start_due_same),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    addedMoney == 0.0 -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.money_form_error_invalid_money),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        viewModel.insertGoal(goalName, startDate, endDate, addedMoney)
                        addedMoney = 0.0
                        goalName = ""

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
                text = stringResource(R.string.goals_screen_heading),
                style = MaterialTheme.typography.bodyLarge
            )
        }

    }

}

@Composable
fun DatePickerTextField(
    textFieldLabel: String,
    onSelectedDate: (Long) -> Unit
) {
    Text(
        text = textFieldLabel,
        style = MaterialTheme.typography.bodyLarge
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    val currentDate = Date().toFormattedString()
    var selectedDate by rememberSaveable { mutableStateOf(currentDate) }

    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH)
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val datePickerDialog =
        DatePickerDialog(context, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val newDate = Calendar.getInstance()
            newDate.set(year, month, dayOfMonth)
            selectedDate = "${month.toMonthName()} $dayOfMonth, $year"
            onSelectedDate(newDate.timeInMillis)
        }, year, month, day)

    TextField(
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        value = selectedDate,
        onValueChange = {},
        trailingIcon = { Icons.Default.DateRange },
        interactionSource = interactionSource
    )

    if (isPressed) {
        datePickerDialog.show()
    }
}


@Composable
fun SteppedSlider(
    steps: Int,
    sliderPosition: Float,
    onSliderChange: (Float) -> Unit
) {
    Column {
        Slider(
            value = sliderPosition,
            onValueChange = { onSliderChange(it) },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = steps,
            valueRange = 0f..50f,
            modifier = Modifier.padding(horizontal = 5.dp)
        )
        Text(text = sliderPosition.toInt().toString())
    }
}
