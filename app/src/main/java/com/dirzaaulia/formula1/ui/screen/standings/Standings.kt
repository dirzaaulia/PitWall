package com.dirzaaulia.formula1.ui.screen.standings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirzaaulia.formula1.model.ConstructorsStandings
import com.dirzaaulia.formula1.model.DriverStandings
import com.dirzaaulia.formula1.ui.screen.widget.YearOnlyPicker
import com.dirzaaulia.formula1.util.ResponseResult
import com.dirzaaulia.formula1.util.error
import com.dirzaaulia.formula1.util.success
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.time.Year

@Composable
fun Standings(
    viewModel: StandingsViewModel = hiltViewModel()
) {

    val driverStandings = viewModel.driverStandings.collectAsStateWithLifecycle()
    val constructorsStandings = viewModel.constructorsStandings.collectAsStateWithLifecycle()
    val currentYear = Year.now().value
    var showDialog by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableIntStateOf(currentYear) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val driverLazyListState = rememberLazyListState()
    val constructorsLazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val options = listOf("Driver", "Constructor")

    LaunchedEffect(key1 = selectedYear, key2 = selectedIndex) {
        viewModel.getStandings(
            index = selectedIndex,
            year = selectedYear
        )
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

    Scaffold(
        snackbarHost = { snackbarHostState }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Standings",
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    modifier = Modifier.clickable {
                        showDialog = true
                    },
                    text = "$selectedYear",
                    style = MaterialTheme.typography.headlineLarge
                )
                Icon(
                    modifier = Modifier.size(42.dp),
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Year Dropdown Icon"
                )
            }
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size,
                        ),
                        onClick = { selectedIndex = index },
                        selected = index == selectedIndex,
                        label = { Text(label) },
                        icon = { }
                    )
                }
            }
            when (selectedIndex) {
                0 -> {
                    AnimatedVisibility(visible = true) {
                        when (val state = driverStandings.value) {
                            ResponseResult.Loading -> {
                                LazyColumn() {
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
                                    val list = it?.driversStandings.orEmpty()
                                    LazyColumn(state = driverLazyListState) {
                                        items(items = list) { data ->
                                            DriverStandingsItem(data = data)
                                        }
                                    }
                                }
                            }

                            is ResponseResult.Error -> {
                                state.error {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("${it.message}")
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    AnimatedVisibility(visible = true) {
                        when (val state = constructorsStandings.value) {
                            ResponseResult.Loading -> {
                                LazyColumn() {
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
                                    val list = it?.constructorsStandings.orEmpty()
                                    LazyColumn(state = constructorsLazyListState) {
                                        items(items = list) { data ->
                                            ConstructorsStandingsItem(data = data)
                                        }
                                    }
                                }
                            }

                            is ResponseResult.Error -> {
                                state.error {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("${it.message}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DriverStandingsItem(
    data: DriverStandings
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.align(alignment = Alignment.CenterVertically)
            ) {
                Text(
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    text = data.points.toCustomString(),
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    text = "Pts",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            VerticalDivider(
                modifier = Modifier.padding(horizontal = 8.dp),
                thickness = 4.dp,
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = data.driver.name,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = data.driver.surname,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = data.team.teamName,
                        fontSize = TextUnit(10f, TextUnitType.Sp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun ConstructorsStandingsItem(
    data: ConstructorsStandings
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.align(alignment = Alignment.CenterVertically)
            ) {
                Text(
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    text = data.points.toCustomString(),
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    text = "Pts",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            VerticalDivider(
                modifier = Modifier.padding(horizontal = 8.dp),
                thickness = 4.dp,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterVertically)
                    .padding(4.dp),
                text = data.team.teamName,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}

fun Double.toCustomString(): String {
    // Check if the double is a whole number
    return if (this == this.toLong().toDouble()) {
        // If it's a whole number, format as an integer
        this.toLong().toString()
    } else {
        // Otherwise, format with decimal places
        // You can adjust the pattern for more complex needs
        val df = DecimalFormat("#.##") // Shows up to 2 decimal places if present
        df.format(this)
    }
}