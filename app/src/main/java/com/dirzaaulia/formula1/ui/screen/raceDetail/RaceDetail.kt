package com.dirzaaulia.formula1.ui.screen.raceDetail

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirzaaulia.formula1.R
import com.dirzaaulia.formula1.util.formatToLocalTimezone
import com.dirzaaulia.formula1.util.hasDatePassed
import com.dirzaaulia.formula1.model.Race
import com.dirzaaulia.formula1.model.RaceDate
import com.dirzaaulia.formula1.model.RaceResult
import com.dirzaaulia.formula1.model.RaceSchedule
import com.dirzaaulia.formula1.navigation.RaceDetail
import com.dirzaaulia.formula1.util.ResponseResult
import com.dirzaaulia.formula1.util.success
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import com.dirzaaulia.formula1.navigation.RaceResult as RaceResultNavigation

@Composable
fun RaceDetail(
    viewModel: RaceDetailViewModel = hiltViewModel(),
    data: RaceDetail,
    navigateToRaceResult: (RaceResultNavigation) -> Unit
) {

    val item = Json.decodeFromString<Race>(data.race)

    val driverDetailState = viewModel.driverDetail.collectAsStateWithLifecycle()
    val raceResultState = viewModel.raceResult.collectAsStateWithLifecycle()
    val hasDatePassed = hasDatePassed(data.grandPrixWeekendStart)

    var isExpanded by remember { mutableStateOf(false) }
    val animationDuration = 300 // Adjust as needed
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Schedule", "Circuit")

    LaunchedEffect(viewModel) {
        viewModel.getDriverDetail(item.circuit.fastestLapDriverId)
        viewModel.getRaceResult(
            year = data.season.toInt(),
            round = item.round
        )
    }

    Scaffold(
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp) // Space between buttons
            ) {
                // Animated visibility for the column of action buttons
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(animationSpec = tween(animationDuration)),
                    exit = fadeOut(animationSpec = tween(animationDuration))
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            ActionButtonData(Icons.Filled.Info, "Grand Prix", item.url),
                            ActionButtonData(Icons.Filled.Info, "Circuit", item.circuit.url)
                        ).forEach { actionButtonData ->
                            ActionButton(actionButtonData = actionButtonData)
                        }
                    }
                }

                // Main FAB
                FloatingActionButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.size(56.dp)
                ) {
                    // Use Crossfade to animate the icon change
                    Crossfade(
                        targetState = isExpanded, // Animate based on the expanded state
                        animationSpec = tween(animationDuration)
                    ) { targetIsExpanded ->
                        if (targetIsExpanded) {
                            // When expanded, show a "Close" icon (or Add if you prefer)
                            Icon(
                                Icons.Filled.Close, // Or Icons.Filled.Add if you prefer a rotated plus
                                contentDescription = "Close FABs"
                            )
                        } else {
                            // When not expanded, show the "Info" icon
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = "Toggle FABs"
                            )
                        }
                    }
                }
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
        ) {
            item(key = 0) {
                Box(contentAlignment = Alignment.Center) {
                    val alpha = if (isSystemInDarkTheme()) 0.4f else 0.1f
                    Image(
                        painter = painterResource(id = R.drawable.racing),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.height(250.dp),
                        alignment = Alignment.BottomEnd,
                        alpha = alpha
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Round ${item.round} | ${data.grandPrixDate}",
                            style = MaterialTheme.typography.titleSmall,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = item.circuit.country,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = item.raceName,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (!hasDatePassed) {
                            CountdownTimer(data.grandPrixWeekendStart)
                        } else {
                            when (val state = raceResultState.value) {
                                ResponseResult.Loading -> {

                                }

                                is ResponseResult.Success<*> -> {
                                    val isSprint = item.schedule?.sprintQualy?.date != null
                                    state.success {
                                        it?.races?.results?.let { raceResult ->
                                            MiniLeaderboards(
                                                year = data.season.toInt(),
                                                round = item.round,
                                                city = item.circuit.city,
                                                isSprint = isSprint,
                                                raceResult = raceResult,
                                                navigateToRaceResult = navigateToRaceResult
                                            )
                                        }
                                    }
                                }

                                is ResponseResult.Error -> {}
                            }

                        }
                    }
                }
            }
            item(key = 1) {
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

                if (selectedIndex == 0) {
                    AnimatedVisibility(true) {
                        Card(
                            shape = RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 8.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Grand Prix Weekend",
                                    style = MaterialTheme.typography.displayLarge
                                )
                                for (index in 0..4) {
                                    val raceDate = getRaceDateByIndex(
                                        schedule = item.schedule,
                                        index = index
                                    )
                                    Card(
                                        modifier = Modifier.padding(bottom = 8.dp),
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                        ) {
                                            Text(
                                                text = getRaceTypeByIndex(
                                                    schedule = item.schedule,
                                                    index = index
                                                ).toString(),
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            "${raceDate?.date} ${raceDate?.time}".formatToLocalTimezone(
                                                "dd MMMM yyyy | HH:mm"
                                            )
                                                ?.let {
                                                    Text(
                                                        text = it,
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    AnimatedVisibility(true) {
                        Column {
                            Text(
                                text = item.circuit.circuitName,
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                text = "${item.circuit.city}, ${item.circuit.country}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CardItemCircuit(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp)
                                        .fillMaxHeight(),
                                    title = "Length",
                                    desc = item.circuit.circuitLength
                                )
                                CardItemCircuit(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp)
                                        .fillMaxHeight(),
                                    title = "Corners",
                                    desc = item.circuit.corners.toString()
                                )
                                CardItemCircuit(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    title = "First Grand Prix",
                                    desc = item.circuit.firstYear.toString()
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            when (val state = driverDetailState.value) {
                                ResponseResult.Loading -> {}
                                is ResponseResult.Success<*> -> {
                                    state.success {
                                        val driver = it?.driver?.first()
                                        Card(modifier = Modifier.fillMaxWidth()) {
                                            Column(modifier = Modifier.padding(4.dp)) {
                                                Text(
                                                    text = "Fastest Lap",
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                                Text(
                                                    text = "${driver?.name} ${driver?.surname} (${item.circuit.fastestLapYear})",
                                                    style = MaterialTheme.typography.headlineLarge
                                                )
                                            }
                                        }
                                    }
                                }

                                is ResponseResult.Error -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardItemCircuit(
    modifier: Modifier = Modifier,
    title: String,
    desc: String
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

fun getRaceTypeByIndex(
    schedule: RaceSchedule?,
    index: Int
): String? {
    if (schedule == null) return null
    val isSprint = schedule.sprintQualy?.time != null
    return if (!isSprint) {
        when (index) {
            0 -> "Practice 1"
            1 -> "Practice 2"
            2 -> "Practice 3"
            3 -> "Qualifiying"
            4 -> "Race"
            else -> null // Invalid index
        }
    } else {
        when (index) {
            0 -> "Practice 1"
            1 -> "Sprint Qualifying"
            2 -> "Sprint Race"
            3 -> "Qualifiying"
            4 -> "Race"
            else -> null // Invalid index
        }
    }
}

fun getRaceDateByIndex(
    schedule: RaceSchedule?,
    index: Int
): RaceDate? {
    if (schedule == null) return null
    val isSprint = schedule.sprintQualy?.time != null
    return if (!isSprint) {
        when (index) {
            0 -> schedule.fp1
            1 -> schedule.fp2
            2 -> schedule.fp3
            3 -> schedule.qualy
            4 -> schedule.race
            else -> null // Invalid index
        }
    } else {
        when (index) {
            0 -> schedule.fp1
            1 -> schedule.sprintQualy
            2 -> schedule.sprintRace
            3 -> schedule.qualy
            4 -> schedule.race
            else -> null // Invalid index
        }
    }
}

@Composable
fun CountdownTimer(
    targetDateTimeString: String,
    modifier: Modifier = Modifier,
) {
    var remainingTime by remember { mutableStateOf(Duration.ZERO) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss'Z'")

    // Parse the targetDateTimeString to a ZonedDateTime with UTC timezone
    val targetDateTimeUTC =
        LocalDateTime.parse(targetDateTimeString, formatter).atZone(ZoneId.of("UTC"))

    // Convert to the user's local timezone
    val targetDateTimeLocal = targetDateTimeUTC.withZoneSameInstant(ZoneId.systemDefault())

    LaunchedEffect(key1 = Unit) {
        while (true) {
            // Get the current time in the user's local timezone
            val now = ZonedDateTime.now(ZoneId.systemDefault())

            // Calculate the remaining time
            remainingTime = Duration.between(now, targetDateTimeLocal)

            // Handle cases where the target time has passed
            if (remainingTime.isNegative) {
                remainingTime = Duration.ZERO
            }
            delay(1000)
        }
    }

    val days = remainingTime.toDays()
    val hours = remainingTime.toHours() % 24
    val minutes = remainingTime.toMinutes() % 60
    val seconds = remainingTime.seconds % 60

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Grand Prix Weekend",
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                val dayText = if (days == 1L) {
                    "Day"
                } else "Days"
                val hoursText = if (hours == 1L) {
                    "Hour"
                } else "Hours"
                val minuteText = if (minutes == 1L) {
                    "Minute"
                } else "Minutes"
                val secondText = if (seconds == 1L) {
                    "Second"
                } else "Seconds"
                Text(
                    text = "$days\n$dayText",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$hours\n$hoursText",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$minutes\n$minuteText",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$seconds\n$secondText",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ActionButton(actionButtonData: ActionButtonData) {
    val context = LocalContext.current

    ExtendedFloatingActionButton(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, actionButtonData.url.toUri())
            context.startActivity(intent)
        },
        icon = {
            Icon(
                imageVector = actionButtonData.icon,
                contentDescription = actionButtonData.name
            )
        },
        text = { Text(text = actionButtonData.name) }
    )
}

@Composable
fun MiniLeaderboards(
    year: Int,
    round: Int,
    city: String,
    isSprint: Boolean,
    raceResult: List<RaceResult>,
    navigateToRaceResult: (RaceResultNavigation) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                text = "Race Result",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Bold
            )
            raceResult.subList(0, 3).forEach { item ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(0.1f),
                            text = "${item.position}",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.weight(0.4f),
                            text = "${item.driver.name} ${item.driver.surname}",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.weight(0.4f),
                            text = item.time,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                onClick = {
                    val resultType = if (isSprint) {
                        listOf("Practice 1", "Sprint Qualifying", "Sprint Race", "Qualifying", "Race")
                    } else {
                        listOf("Practice 1", "Practice 2", "Practice 3", "Qualifying", "Race")
                    }
                    val raceResult = RaceResultNavigation(
                        year = year,
                        round = round,
                        city = city,
                        resultType = Json.encodeToString(resultType)
                    )
                    navigateToRaceResult.invoke(raceResult)
                }
            ) {
                Text(
                    text = "See Full Result",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class ActionButtonData(
    val icon: ImageVector,
    val name: String,
    val url: String
)