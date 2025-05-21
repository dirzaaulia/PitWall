package com.dirzaaulia.formula1.ui.screen.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.time.Year

@Composable
fun YearOnlyPicker(
    year: Int,
    onYearSelected: (Int) -> Unit
) {
    val currentYear = Year.now().value
    var selectedYearState by remember { mutableIntStateOf(year) }
    var isError by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            singleLine = true,
            label = { Text("Year") },
            supportingText = {
                Text(text = "Only from 1950 until current year")
            },
            isError = isError,
            value = selectedYearState.toString(),
            onValueChange = {
                isError = false
                selectedYearState = if (it.isEmpty()) 0 else it.toInt()
            },
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (selectedYearState < 1950 || selectedYearState > currentYear) {
                    isError = true
                } else {
                    onYearSelected.invoke(selectedYearState)
                }
            }
        ) {
            Text("Confirm")
        }
    }
}