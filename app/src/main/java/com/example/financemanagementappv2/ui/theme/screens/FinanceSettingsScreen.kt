package com.example.financemanagementappv2.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.financemanagementappv2.data.viewmodels.HomeScreenViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FinanceSettingsScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onDrawerClicked: () -> Unit = {}
) {
    var buttonClicked by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

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

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                when {
                    buttonClicked == true -> {
                        Toast.makeText(
                            context,
                            "Dummy Data Is Already Loaded!",
                            Toast.LENGTH_SHORT

                        ).show()
                    }
                    else -> {
                        buttonClicked = true
                        viewModel.insertDummyData()
                        Toast.makeText(
                            context,
                            "Dummy Data Loaded!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text(
                text = "Load Dummy Data",
                style = MaterialTheme.typography.bodyLarge
            )
        }

    }
}