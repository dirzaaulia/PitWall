package com.dirzaaulia.formula1.ui.screen.raceResult

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirzaaulia.formula1.R
import com.dirzaaulia.formula1.navigation.RaceResult
import com.dirzaaulia.formula1.util.ResponseResult
import com.dirzaaulia.formula1.util.success
import com.valentinilk.shimmer.shimmer
import kotlinx.serialization.json.Json
import com.dirzaaulia.formula1.model.RaceResult as RaceResultModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RaceResult(
    viewModel: RaceResultViewModel = hiltViewModel(),
    data: RaceResult,
) {
    val raceResultState = viewModel.raceResult.collectAsStateWithLifecycle()

    val resultType = Json.decodeFromString<List<String>>(data.resultType).toMutableList()

    val alpha = if (isSystemInDarkTheme()) 0.2f else 0.1f
    var selectedIndex by remember { mutableIntStateOf(4) }
    var expanded by remember { mutableStateOf(false) }
    val isSprint = resultType[selectedIndex].contains("sprint", true)

    LaunchedEffect(selectedIndex) {
        viewModel.getRaceResult(
            isSprint = isSprint,
            type = resultType[selectedIndex].transformTypeForAPI(),
            year = data.year,
            round = data.round
        )
    }

    Scaffold { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Image(
                painter = painterResource(id = R.drawable.result),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.height(250.dp),
                alignment = Alignment.BottomEnd,
                alpha = alpha
            )
            Column {
                Text(
                    text = "Result - ${data.city} ${data.year}",
                    style = MaterialTheme.typography.headlineLarge
                )
                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.7f),
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                ) {
                    OutlinedTextField(
                        // The `menuAnchor` modifier must be passed to the text field to handle
                        // expanding/collapsing the menu on click. A read-only text field has
                        // the anchor type `PrimaryNotEditable`.
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
                        readOnly = true,
                        singleLine = true,
                        label = { Text("Session") },
                        trailingIcon = { TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        value = resultType[selectedIndex],
                        onValueChange = { },
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        resultType.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                                onClick = {
                                    selectedIndex = resultType.indexOf(option)
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
                when (val state = raceResultState.value) {
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
                            it?.let { data ->
                                when {
                                    data.races.fp1Results.isNotEmpty() -> PracticeResultList(
                                        list = data.races.fp1Results
                                    )

                                    data.races.fp2Results.isNotEmpty() -> PracticeResultList(
                                        list = data.races.fp2Results
                                    )

                                    data.races.fp3Results.isNotEmpty() -> PracticeResultList(
                                        list = data.races.fp3Results
                                    )

                                    data.races.qualyResults.isNotEmpty() -> QualifyingResultList(
                                        isSprint = isSprint,
                                        list = data.races.qualyResults
                                    )

                                    data.races.sprintQualyResults.isNotEmpty() -> QualifyingResultList(
                                        isSprint = isSprint,
                                        list = data.races.sprintQualyResults
                                    )

                                    data.races.sprintRaceResults.isNotEmpty() -> SprintRaceResultList(list = data.races.sprintRaceResults)

                                    data.races.results.isNotEmpty() -> RaceResultList(list = data.races.results)
                                }
                            }
                        }

                    }

                    is ResponseResult.Error -> {

                    }
                }
            }
        }
    }
}

@Composable
fun PracticeResultList(
    list: List<RaceResultModel>
) {
    Card(
        modifier = Modifier.padding(top = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(modifier = Modifier.padding(horizontal = 4.dp)) {
            Text(
                modifier = Modifier.weight(0.1f),
                text = "Pos",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(0.45f),
                text = "Driver",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(0.45f),
                text = "Time/Ret",
                textAlign = TextAlign.Center
            )
        }
    }
    LazyColumn {
        items(
            items = list,
            key = { item -> list.indexOf(item) }
        ) { item ->
            PracticeResultItem(
                index = list.indexOf(item).plus(1),
                item = item
            )
        }
    }
}


@Composable
fun SprintRaceResultList(
    list: List<RaceResultModel>
) {
    Card(
        modifier = Modifier.padding(top = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(modifier = Modifier.padding(horizontal = 4.dp)) {
            Text(
                modifier = Modifier.weight(0.1f),
                text = "Pos",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(0.8f),
                text = "Driver",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(0.1f),
                text = "Pts",
                textAlign = TextAlign.Center
            )

        }
    }
    LazyColumn {
        items(
            items = list,
            key = { item -> list.indexOf(item) }
        ) { item ->
            SprintRaceResultItem(item = item)
        }
    }
}

@Composable
fun RaceResultList(
    list: List<RaceResultModel>
) {
    Card(
        modifier = Modifier.padding(top = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(modifier = Modifier.padding(horizontal = 4.dp)) {
            Text(
                modifier = Modifier.weight(0.1f),
                text = "Pos",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(0.4f),
                text = "Driver",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(0.4f),
                text = "Time/Ret",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(0.1f),
                text = "Pts",
                textAlign = TextAlign.Center
            )
        }
    }
    LazyColumn {
        items(
            items = list,
            key = { item -> list.indexOf(item) }
        ) { item ->
            RaceResultItem(item = item)
        }
    }
}

@Composable
fun PracticeResultItem(
    index: Int,
    item: RaceResultModel
) {
    Card(
        modifier = Modifier.padding(top = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Row {
            Text(
                modifier = Modifier.weight(0.1f),
                text = "$index",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(0.45f),
                text = item.driver.shortName,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(0.45f),
                text = item.time,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SprintRaceResultItem(
    item: RaceResultModel
) {
    Card(
        modifier = Modifier.padding(top = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Row {
            Text(
                modifier = Modifier.weight(0.1f),
                text = "${item.position}",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(0.8f),
                text = "${item.driver.name} ${item.driver.surname}",
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.weight(0.1f),
                text = "${item.points}",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RaceResultItem(
    item: RaceResultModel
) {
    Card(
        modifier = Modifier.padding(top = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(0.1f),
                text = "${item.position}",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(0.4f),
                text = item.driver.shortName,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.weight(0.4f),
                text = item.time,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(0.1f),
                text = "${item.points}",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun QualifyingResultList(
    isSprint: Boolean,
    list: List<RaceResultModel>
) {
    Card(
        modifier = Modifier.padding(top = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row (modifier = Modifier.padding(horizontal = 4.dp)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f),
                text = "Pos",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                text = "Driver",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                text = if (isSprint) "SQ1" else "Q1",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                text = if (isSprint) "SQ2" else "Q2",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                text = if (isSprint) "SQ3" else "Q3",
                textAlign = TextAlign.Center
            )
        }
    }
    LazyColumn {
        items(
            items = list,
            key = { item -> list.indexOf(item) }
        ) { item ->
            if (isSprint) SprintQualifyingResultItem(item)
            else QualifyingResultItem(item)
        }
    }
}

@Composable
fun QualifyingResultItem(
    item: RaceResultModel
) {
    Card(
        modifier = Modifier.padding(top = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Row {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f),
                text = "${item.gridPosition}",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                text = item.driver.shortName,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                text = item.q1.toString().replace("null", "-"),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                text = item.q2.toString().replace("null", "-"),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                text = item.q3.toString().replace("null", "-"),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SprintQualifyingResultItem(
    item: RaceResultModel
) {
    Card(
        modifier = Modifier.padding(top = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Row {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f),
                text = "${item.gridPosition}",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                text = item.driver.shortName,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                text = item.sq1.toString().replace("null", "-"),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                text = item.sq2.toString().replace("null", "-"),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                text = item.sq3.toString().replace("null", "-"),
                textAlign = TextAlign.Center
            )
        }
    }
}

fun String.transformTypeForAPI(): String {
    return when {
        this.equals("qualifying", true) -> "qualy"
        this.equals("practice 3", true) -> "fp3"
        this.equals("practice 2", true) -> "fp2"
        this.equals("practice 1", true) -> "fp1"
        else -> this
    }
}