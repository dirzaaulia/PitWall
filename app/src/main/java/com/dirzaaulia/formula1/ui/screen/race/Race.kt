package com.dirzaaulia.formula1.ui.screen.race

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirzaaulia.formula1.util.findClosestFutureDate
import com.dirzaaulia.formula1.util.formatDateTimeRangeToLocal
import com.dirzaaulia.formula1.navigation.RaceDetail
import com.dirzaaulia.formula1.ui.screen.widget.YearOnlyPicker
import com.dirzaaulia.formula1.util.ResponseResult
import com.dirzaaulia.formula1.util.error
import com.dirzaaulia.formula1.util.success
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.Year
import com.dirzaaulia.formula1.model.Race as RaceModel

@Composable
fun Race(
    viewModel: RaceViewModel = hiltViewModel(),
    navigateToRaceDetail: (RaceDetail) -> Unit
) {

    val currentScheduleState = viewModel.scheduleResult.collectAsStateWithLifecycle()
    val isAnimationDone = viewModel.isAnimationDone.collectAsStateWithLifecycle()
    val currentYear = Year.now().value
    var showDialog by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableIntStateOf(currentYear) }
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = selectedYear) {
        viewModel.getSchedule(selectedYear)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Input Year") },
            text = {
                YearOnlyPicker(
                    year = selectedYear,
                    onYearSelected = { year ->
                        selectedYear = year
                        showDialog = false // Close the dialog after selection
                    }
                )
            },
            confirmButton = {
                // You might not need a confirm button if selecting a year closes the dialog
            }
        )
    }

    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Race",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    modifier = Modifier.clickable {
                        showDialog = true
                    },
                    text = "$selectedYear",
                    style = MaterialTheme.typography.displaySmall
                )
                Icon(
                    modifier = Modifier.size(42.dp),
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Year Dropdown Icon"
                )
            }
            when (val state = currentScheduleState.value) {
                ResponseResult.Loading -> {
                    LazyColumn {
                        items(10) {
                            Card(
                                modifier = Modifier
                                    .shimmer()
                                    .padding(top = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                )
                            }
                        }
                    }
                }

                is ResponseResult.Success<*> -> {
                    state.success {
                        it.let {
                            val futureRace = findClosestFutureDate(it!!.races)
                            val indexFutureRace = it.races.indexOf(futureRace)

                            LazyColumn(
                                state = lazyListState
                            ) {
                                items(it.races) { item ->
                                    RaceItem(
                                        season = it.season,
                                        item = item,
                                        navigateToRaceDetail = navigateToRaceDetail
                                    )
                                }
                                if (!isAnimationDone.value) {
                                    scope.launch {
                                        delay(1000)
                                        lazyListState.animateScrollToItem(indexFutureRace)
                                        viewModel.setIsAnimationDone()
                                    }
                                }

                                if (selectedYear != currentYear) {
                                    scope.launch {
                                        delay(1000)
                                        lazyListState.animateScrollToItem(0)
                                    }
                                }
                            }
                        }
                    }
                }

                is ResponseResult.Error -> {
                    state.error {
                        Log.d("TAG_DIRZA", it.message.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun RaceItem(
    season: String,
    item: RaceModel,
    navigateToRaceDetail: (RaceDetail) -> Unit
) {
    val grandPrixDate = formatDateTimeRangeToLocal(
        dateTimeString1 = "${item.schedule?.fp1?.date} ${item.schedule?.fp1?.time}",
        dateTimeString2 = "${item.schedule?.race?.date} ${item.schedule?.race?.time}"
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable {
                navigateToRaceDetail.invoke(
                    RaceDetail(
                        season = season,
                        race = Json.encodeToString(item),
                        grandPrixDate = grandPrixDate.toString(),
                        grandPrixWeekendStart = "${item.schedule?.fp1?.date} ${item.schedule?.fp1?.time}",
                    )
                )
            }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            grandPrixDate?.let {
                Text(
                    text = "Round ${item.round} | $it ",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = item.circuit.country,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = item.raceName,
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}