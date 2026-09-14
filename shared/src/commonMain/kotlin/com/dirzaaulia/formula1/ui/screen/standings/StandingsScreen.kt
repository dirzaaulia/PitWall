package com.dirzaaulia.formula1.ui.screen.standings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dirzaaulia.formula1.model.ConstructorsStandings
import com.dirzaaulia.formula1.model.Driver
import com.dirzaaulia.formula1.model.DriverStandings
import com.dirzaaulia.formula1.model.Team
import com.dirzaaulia.formula1.network.NetworkRepository
import com.dirzaaulia.formula1.theme.DarkCard
import com.dirzaaulia.formula1.theme.DarkCardElevated
import com.dirzaaulia.formula1.theme.F1Red
import com.dirzaaulia.formula1.theme.F1RedSubtle
import com.dirzaaulia.formula1.theme.GlassBorderActive
import com.dirzaaulia.formula1.theme.GlassSurface
import com.dirzaaulia.formula1.theme.HairlineBorder
import com.dirzaaulia.formula1.theme.MonoMuted
import com.dirzaaulia.formula1.theme.MonoSilver
import com.dirzaaulia.formula1.theme.MonoWhite
import com.dirzaaulia.formula1.theme.ObsidianSurface
import com.dirzaaulia.formula1.theme.ObsidianSurfaceElevated
import com.dirzaaulia.formula1.theme.ObsidianSurfaceVariant
import com.dirzaaulia.formula1.theme.ObsidianVoid
import com.dirzaaulia.formula1.ui.component.FormulaTrackrImage
import com.dirzaaulia.formula1.ui.component.MetricTile
import com.dirzaaulia.formula1.ui.component.ShimmerCard
import com.dirzaaulia.formula1.util.LocalWindowSizeClass
import com.dirzaaulia.formula1.util.format2Digits
import com.dirzaaulia.formula1.util.getCountryFlagUrl
import com.dirzaaulia.formula1.util.getDriverHeadshotUrl
import com.dirzaaulia.formula1.util.getTeamCarUrl
import com.dirzaaulia.formula1.util.getTeamLogoUrl

private val GoldPodium = Color(0xFFFFD700)
private val SilverPodium = Color(0xFFC0C0C0)
private val BronzePodium = Color(0xFFCD7F32)

enum class StandingsSubTab {
    DRIVERS,
    CONSTRUCTORS
}

@Composable
fun StandingsScreen(
    selectedSeason: Int
) {
    var currentSeason by remember(selectedSeason) { mutableStateOf(selectedSeason) }
    var yearDropdownExpanded by remember { mutableStateOf(false) }
    var selectedDecade by remember { mutableStateOf<Int?>(null) }
    val availableYears = remember { (1950..2026).toList().reversed() }

    val filteredYears = remember(selectedDecade) {
        if (selectedDecade == null) availableYears
        else availableYears.filter { it in selectedDecade!!..(selectedDecade!! + 9) }
    }

    var subTab by remember { mutableStateOf(StandingsSubTab.DRIVERS) }
    var isLoading by remember(currentSeason) { mutableStateOf(true) }

    var driverStandings by remember(currentSeason) { mutableStateOf<List<DriverStandings>>(emptyList()) }
    var constructorStandings by remember(currentSeason) { mutableStateOf<List<ConstructorsStandings>>(emptyList()) }

    var selectedDriver by remember { mutableStateOf<Driver?>(null) }
    var selectedConstructor by remember { mutableStateOf<Team?>(null) }

    LaunchedEffect(currentSeason) {
        isLoading = true
        driverStandings = NetworkRepository.getDriverStandings(currentSeason)
        constructorStandings = NetworkRepository.getConstructorStandings(currentSeason)
        isLoading = false
    }

    val windowSizeClass = LocalWindowSizeClass.current
    val isWidescreen = windowSizeClass.isWidescreen

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Expressive PitWall Header Deck
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 6.dp, bottomStart = 6.dp),
                            color = F1Red.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, F1Red.copy(alpha = 0.6f))
                        ) {
                            Text(
                                text = "/ WORLD CHAMPIONSHIP /",
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = F1Red
                            )
                        }

                        Text(
                            text = "SEASON $currentSeason",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = MonoMuted
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "CHAMPIONSHIP STANDINGS",
                        fontSize = if (isWidescreen) 22.sp else 18.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        letterSpacing = 1.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GlassSurface,
                    border = BorderStroke(1.dp, GlassBorderActive),
                    modifier = Modifier.clickable { yearDropdownExpanded = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "$currentSeason",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MonoWhite
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Season",
                            tint = MonoWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Mobile Segmented Switch: DRIVERS vs CONSTRUCTORS (Full Width, Slim Height)
            if (!isWidescreen) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFF0F121C),
                    border = BorderStroke(1.dp, HairlineBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        val isDriver = subTab == StandingsSubTab.DRIVERS
                        val isConstructor = subTab == StandingsSubTab.CONSTRUCTORS

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (isDriver) F1Red else Color.Transparent,
                            border = BorderStroke(1.dp, if (isDriver) F1Red else Color.Transparent),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(50))
                                .clickable { subTab = StandingsSubTab.DRIVERS }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.5.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = if (isDriver) MonoWhite else MonoMuted,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "DRIVERS",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (isDriver) MonoWhite else MonoMuted,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (isConstructor) F1Red else Color.Transparent,
                            border = BorderStroke(1.dp, if (isConstructor) F1Red else Color.Transparent),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(50))
                                .clickable { subTab = StandingsSubTab.CONSTRUCTORS }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.5.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = if (isConstructor) MonoWhite else MonoMuted,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "CONSTRUCTORS",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (isConstructor) MonoWhite else MonoMuted,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Season Picker Modal Sheet
        if (yearDropdownExpanded) {
            @OptIn(ExperimentalMaterial3Api::class)
            ModalBottomSheet(
                onDismissRequest = { yearDropdownExpanded = false },
                containerColor = ObsidianSurface,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "SELECT SEASON",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoMuted
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            val isAll = selectedDecade == null
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isAll) F1Red else GlassSurface,
                                border = BorderStroke(1.dp, if (isAll) F1Red else GlassBorderActive),
                                modifier = Modifier.clickable { selectedDecade = null }
                            ) {
                                Text(
                                    text = "ALL YEARS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoWhite,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        val decadeList = listOf(2020, 2010, 2000, 1990, 1980, 1970, 1960, 1950)
                        items(decadeList) { d ->
                            val isSelected = selectedDecade == d
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) F1Red else GlassSurface,
                                border = BorderStroke(1.dp, if (isSelected) F1Red else GlassBorderActive),
                                modifier = Modifier.clickable { selectedDecade = d }
                            ) {
                                Text(
                                    text = "${d}s",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoWhite,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    ) {
                        items(filteredYears.size) { index ->
                            val year = filteredYears[index]
                            val isSelected = year == currentSeason
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) F1Red.copy(alpha = 0.2f) else GlassSurface,
                                border = BorderStroke(1.dp, if (isSelected) F1Red else GlassBorderActive),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        currentSeason = year
                                        yearDropdownExpanded = false
                                    }
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$year",
                                        fontSize = 13.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                        color = if (isSelected) F1Red else MonoWhite
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Main Standings Content
        if (isLoading) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(if (isWidescreen) 8 else 5) {
                    ShimmerCard(height = 140.dp)
                }
            }
        } else if (isWidescreen) {
            // Dual-Pane Widescreen Dashboard (Simultaneous Drivers & Constructors)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    StandingsSectionHeader(
                        title = "/ DRIVERS WORLD CHAMPIONSHIP /",
                        subtitle = "CHAMPIONSHIP STANDINGS",
                        accentColor = F1Red
                    )
                    Spacer(Modifier.height(10.dp))
                    DriversStandingsList(
                        driverStandings = driverStandings,
                        onSelectDriver = { selectedDriver = it },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    StandingsSectionHeader(
                        title = "/ CONSTRUCTORS CHAMPIONSHIP /",
                        subtitle = "CONSTRUCTORS STANDINGS",
                        accentColor = Color(0xFF00D7B6)
                    )
                    Spacer(Modifier.height(10.dp))
                    ConstructorsStandingsList(
                        constructorStandings = constructorStandings,
                        onSelectConstructor = { selectedConstructor = it },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        } else {
            when (subTab) {
                StandingsSubTab.DRIVERS -> {
                    DriversStandingsList(
                        driverStandings = driverStandings,
                        onSelectDriver = { selectedDriver = it },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                StandingsSubTab.CONSTRUCTORS -> {
                    ConstructorsStandingsList(
                        constructorStandings = constructorStandings,
                        onSelectConstructor = { selectedConstructor = it },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    // Driver Detail Modal Sheet
    if (selectedDriver != null) {
        val d = selectedDriver!!
        val headshotUrl = getDriverHeadshotUrl("${d.driverId} ${d.code} ${d.fullName}")

        @OptIn(ExperimentalMaterial3Api::class)
        ModalBottomSheet(
            onDismissRequest = { selectedDriver = null },
            containerColor = ObsidianSurface,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header with Team Gradient
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = ObsidianSurfaceElevated,
                    border = BorderStroke(1.dp, d.teamColor.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(d.teamColor.copy(alpha = 0.28f), Color.Transparent)
                                )
                            )
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(ObsidianVoid)
                                    .border(3.dp, d.teamColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                FormulaTrackrImage(
                                    url = headshotUrl,
                                    contentDescription = d.fullName,
                                    modifier = Modifier
                                        .size(76.dp)
                                        .clip(CircleShape)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (d.number > 0) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = d.teamColor.copy(alpha = 0.2f),
                                            border = BorderStroke(1.dp, d.teamColor)
                                        ) {
                                            Text(
                                                text = "#${d.number}",
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                fontSize = 11.sp,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Black,
                                                color = d.teamColor
                                            )
                                        }
                                    }

                                    if (d.code.isNotBlank()) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = F1RedSubtle,
                                            border = BorderStroke(1.dp, F1Red.copy(alpha = 0.5f))
                                        ) {
                                            Text(
                                                text = d.code,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                fontSize = 11.sp,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Black,
                                                color = MonoWhite
                                            )
                                        }
                                    }
                                }

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    text = d.fullName.uppercase(),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoWhite
                                )

                                Text(
                                    text = d.team,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MonoSilver
                                )
                            }
                        }
                    }
                }

                // Metric Tiles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricTile(
                        label = "SEASON POINTS",
                        value = "${d.points.toInt()}",
                        modifier = Modifier.weight(1f)
                    )
                    MetricTile(
                        label = "RACE WINS",
                        value = "${d.wins}",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Dossier Table
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ObsidianSurfaceElevated,
                    border = BorderStroke(1.dp, HairlineBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(d.teamColor))
                            Text(text = "Constructor: ", fontSize = 13.sp, color = MonoMuted)
                            Text(text = d.team, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MonoWhite)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            FormulaTrackrImage(
                                url = getCountryFlagUrl(d.nationality),
                                modifier = Modifier.size(20.dp).clip(RoundedCornerShape(2.dp))
                            )
                            Text(text = "Nationality: ", fontSize = 13.sp, color = MonoMuted)
                            Text(text = d.nationality, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MonoWhite)
                        }

                        if (d.birthday.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(text = "DOB: ", fontSize = 13.sp, color = MonoMuted)
                                Text(text = d.birthday, fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = MonoWhite)
                            }
                        }
                    }
                }
            }
        }
    }

    // Constructor Detail Modal Sheet
    if (selectedConstructor != null) {
        val t = selectedConstructor!!

        @OptIn(ExperimentalMaterial3Api::class)
        ModalBottomSheet(
            onDismissRequest = { selectedConstructor = null },
            containerColor = ObsidianSurface,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header with Team Branding
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = ObsidianSurfaceElevated,
                    border = BorderStroke(1.dp, t.color.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(t.color.copy(alpha = 0.28f), Color.Transparent)
                                )
                            )
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(54.dp)
                                    .width(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ObsidianSurfaceVariant)
                                    .border(1.dp, HairlineBorder, RoundedCornerShape(8.dp))
                                    .padding(6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                FormulaTrackrImage(
                                    url = getTeamLogoUrl(t.teamName),
                                    removeWhiteBackground = false,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = t.teamName.uppercase(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoWhite
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    FormulaTrackrImage(
                                        url = getCountryFlagUrl(t.nationality ?: ""),
                                        modifier = Modifier.size(16.dp).clip(RoundedCornerShape(2.dp))
                                    )
                                    Text(
                                        text = t.nationality ?: "Global",
                                        fontSize = 12.sp,
                                        color = MonoSilver
                                    )
                                }
                            }
                        }
                    }
                }

                // Big Constructor Car Livery Render
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = ObsidianVoid,
                    border = BorderStroke(1.dp, HairlineBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        FormulaTrackrImage(
                            url = getTeamCarUrl(t.teamName),
                            contentDescription = "${t.teamName} Car",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                        )
                    }
                }

                // Metrics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricTile(
                        label = "TEAM POINTS",
                        value = "${t.points.toInt()}",
                        modifier = Modifier.weight(1f)
                    )
                    MetricTile(
                        label = "RACE WINS",
                        value = "${t.wins}",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Power Unit & Lineup
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ObsidianSurfaceElevated,
                    border = BorderStroke(1.dp, HairlineBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(t.color))
                            Text(text = "Power Unit: ", fontSize = 13.sp, color = MonoMuted)
                            Text(text = t.powerUnit, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MonoWhite)
                        }

                        if (t.drivers.isNotEmpty()) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(text = "Active Drivers Lineup:", fontSize = 12.sp, color = MonoMuted)
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    t.drivers.forEach { driverName ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(20.dp))
                                                .background(ObsidianVoid)
                                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                        ) {
                                            FormulaTrackrImage(
                                                url = getDriverHeadshotUrl(driverName),
                                                contentDescription = driverName,
                                                modifier = Modifier
                                                    .size(26.dp)
                                                    .clip(CircleShape)
                                                    .background(ObsidianSurface)
                                            )
                                            Text(
                                                text = driverName,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MonoWhite
                                            )
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
}

@Composable
private fun StandingsSectionHeader(
    title: String,
    subtitle: String,
    accentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = ObsidianSurfaceElevated,
        border = BorderStroke(1.dp, HairlineBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp,
                    color = MonoWhite
                )
            }
            Text(
                text = subtitle,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = MonoMuted
            )
        }
    }
}

@Composable
private fun DriversStandingsList(
    driverStandings: List<DriverStandings>,
    onSelectDriver: (Driver) -> Unit,
    modifier: Modifier = Modifier
) {
    val maxPoints = (driverStandings.firstOrNull()?.points ?: 1.0).coerceAtLeast(1.0)
    val leader = driverStandings.firstOrNull()
    val p2 = driverStandings.getOrNull(1)
    val p3 = driverStandings.getOrNull(2)
    val restOfGrid = if (driverStandings.size > 3) driverStandings.drop(3) else emptyList()

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // P1 Leader Championship Hero Deck
        if (leader != null) {
            item {
                val driver = leader.driver
                val gapToP2 = p2?.let { (leader.points - it.points).toInt() }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onSelectDriver(driver) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCardElevated),
                    border = BorderStroke(1.5.dp, driver.teamColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        driver.teamColor.copy(alpha = 0.35f),
                                        driver.teamColor.copy(alpha = 0.12f),
                                        Color(0xFF0F1118)
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        // Flag Silhouette Watermark
                        FormulaTrackrImage(
                            url = getCountryFlagUrl(driver.nationality),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(140.dp)
                                .graphicsLayer { alpha = 0.08f }
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Top Row: P1 Badge & Gap
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 8.dp, bottomStart = 8.dp),
                                    color = GoldPodium.copy(alpha = 0.2f),
                                    border = BorderStroke(1.dp, GoldPodium)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(7.dp)
                                                .clip(CircleShape)
                                                .background(GoldPodium)
                                        )
                                        Text(
                                            text = "/ P01 • CHAMPIONSHIP LEADER /",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace,
                                            color = GoldPodium,
                                            letterSpacing = 1.sp
                                        )
                                    }
                                }

                                if (gapToP2 != null && gapToP2 > 0) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = F1RedSubtle,
                                        border = BorderStroke(1.dp, F1Red.copy(alpha = 0.6f))
                                    ) {
                                        Text(
                                            text = "+${gapToP2} PTS GAP",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace,
                                            color = MonoWhite
                                        )
                                    }
                                }
                            }

                            // Middle Row: Big Driver Image, Name, Team
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(ObsidianVoid)
                                        .border(3.dp, driver.teamColor, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    FormulaTrackrImage(
                                        url = getDriverHeadshotUrl("${driver.driverId} ${driver.code} ${driver.fullName}"),
                                        contentDescription = driver.fullName,
                                        modifier = Modifier
                                            .size(74.dp)
                                            .clip(CircleShape)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        if (driver.number > 0) {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = driver.teamColor.copy(alpha = 0.25f),
                                                border = BorderStroke(1.dp, driver.teamColor)
                                            ) {
                                                Text(
                                                    text = "#${driver.number}",
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    fontSize = 11.sp,
                                                    fontFamily = FontFamily.Monospace,
                                                    fontWeight = FontWeight.Black,
                                                    color = driver.teamColor
                                                )
                                            }
                                        }

                                        FormulaTrackrImage(
                                            url = getCountryFlagUrl(driver.nationality),
                                            contentDescription = driver.nationality,
                                            modifier = Modifier
                                                .height(14.dp)
                                                .width(22.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                        )

                                        Text(
                                            text = driver.nationality.uppercase(),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            color = MonoSilver
                                        )
                                    }

                                    Spacer(Modifier.height(4.dp))

                                    Text(
                                        text = driver.fullName.uppercase(),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = MonoWhite,
                                        letterSpacing = 0.5.sp
                                    )

                                    Text(
                                        text = driver.team,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MonoSilver
                                    )
                                }
                            }

                            // Bottom Metrics Row: Big Points, Wins
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = ObsidianVoid,
                                        border = BorderStroke(1.dp, HairlineBorder)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = "${leader.points.toInt()}",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Black,
                                                fontFamily = FontFamily.Monospace,
                                                color = MonoWhite
                                            )
                                            Text(
                                                text = "PTS",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black,
                                                fontFamily = FontFamily.Monospace,
                                                color = F1Red
                                            )
                                        }
                                    }

                                    if (leader.wins > 0) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = GoldPodium.copy(alpha = 0.15f),
                                            border = BorderStroke(1.dp, GoldPodium.copy(alpha = 0.5f))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.EmojiEvents,
                                                    contentDescription = null,
                                                    tint = GoldPodium,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Text(
                                                    text = "${leader.wins} ${if (leader.wins == 1) "WIN" else "WINS"}",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black,
                                                    fontFamily = FontFamily.Monospace,
                                                    color = GoldPodium
                                                )
                                            }
                                        }
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "VIEW DOSSIER",
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Black,
                                        color = driver.teamColor
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = driver.teamColor,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // P2 and P3 Podium Contenders
        if (p2 != null) {
            item {
                DriverPodiumCard(
                    standing = p2,
                    leaderPoints = leader?.points ?: 1.0,
                    position = 2,
                    medalColor = SilverPodium,
                    onClick = { onSelectDriver(p2.driver) }
                )
            }
        }

        if (p3 != null) {
            item {
                DriverPodiumCard(
                    standing = p3,
                    leaderPoints = leader?.points ?: 1.0,
                    position = 3,
                    medalColor = BronzePodium,
                    onClick = { onSelectDriver(p3.driver) }
                )
            }
        }

        // Rest of Grid: P4 to P22
        itemsIndexed(restOfGrid) { index, item ->
            val pos = index + 4
            val driver = item.driver
            val relativeProgress = (item.points.toFloat() / maxPoints.toFloat()).coerceIn(0f, 1f)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSelectDriver(driver) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = BorderStroke(1.dp, HairlineBorder)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = format2Digits(pos),
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = MonoSilver,
                                modifier = Modifier.width(26.dp)
                            )

                            FormulaTrackrImage(
                                url = getDriverHeadshotUrl("${driver.driverId} ${driver.code} ${driver.fullName}"),
                                contentDescription = driver.fullName,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(ObsidianVoid)
                                    .border(1.5.dp, driver.teamColor, CircleShape)
                            )

                            if (driver.code.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = F1RedSubtle,
                                    border = BorderStroke(1.dp, F1Red.copy(alpha = 0.3f))
                                ) {
                                    Text(
                                        text = driver.code,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Black,
                                        color = MonoWhite
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .width(3.5.dp)
                                    .height(32.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(driver.teamColor)
                            )

                            Column {
                                Text(
                                    text = driver.fullName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = driver.team,
                                    fontSize = 11.sp,
                                    color = MonoMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${item.points.toInt()} PTS",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = MonoWhite
                            )

                            if (item.wins > 0) {
                                Text(
                                    text = "${item.wins} WIN",
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPodium
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.5.dp)
                            .background(ObsidianVoid)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(relativeProgress)
                                .height(2.5.dp)
                                .background(driver.teamColor)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DriverPodiumCard(
    standing: DriverStandings,
    leaderPoints: Double,
    position: Int,
    medalColor: Color,
    onClick: () -> Unit
) {
    val driver = standing.driver
    val gapToLeader = (leaderPoints - standing.points).toInt()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurfaceElevated),
        border = BorderStroke(1.dp, medalColor.copy(alpha = 0.6f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            medalColor.copy(alpha = 0.16f),
                            driver.teamColor.copy(alpha = 0.08f),
                            Color(0xFF0F1118)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 6.dp, bottomStart = 6.dp),
                        color = medalColor.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, medalColor)
                    ) {
                        Text(
                            text = "/ P${format2Digits(position)} PODIUM CONTENDER /",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = medalColor,
                            letterSpacing = 0.8.sp
                        )
                    }

                    if (gapToLeader > 0) {
                        Text(
                            text = "-${gapToLeader} PTS TO P1",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = MonoMuted
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(ObsidianVoid)
                            .border(2.dp, medalColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        FormulaTrackrImage(
                            url = getDriverHeadshotUrl("${driver.driverId} ${driver.code} ${driver.fullName}"),
                            contentDescription = driver.fullName,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FormulaTrackrImage(
                                url = getCountryFlagUrl(driver.nationality),
                                contentDescription = driver.nationality,
                                modifier = Modifier
                                    .height(12.dp)
                                    .width(18.dp)
                                    .clip(RoundedCornerShape(2.dp))
                            )
                            Text(
                                text = driver.code,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = MonoSilver
                            )
                        }

                        Text(
                            text = driver.fullName.uppercase(),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = MonoWhite
                        )

                        Text(
                            text = driver.team,
                            fontSize = 12.sp,
                            color = MonoSilver
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ObsidianVoid,
                        border = BorderStroke(1.dp, HairlineBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "${standing.points.toInt()}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = MonoWhite
                            )
                            Text(
                                text = "PTS",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = medalColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConstructorsStandingsList(
    constructorStandings: List<ConstructorsStandings>,
    onSelectConstructor: (Team) -> Unit,
    modifier: Modifier = Modifier
) {
    val leader = constructorStandings.firstOrNull()
    val p2 = constructorStandings.getOrNull(1)
    val p3 = constructorStandings.getOrNull(2)
    val restOfGrid = if (constructorStandings.size > 3) constructorStandings.drop(3) else emptyList()
    val maxPoints = (leader?.points ?: 1.0).coerceAtLeast(1.0)

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // P1 Constructors Champion Hero Deck
        if (leader != null) {
            item {
                val gapToP2 = p2?.let { (leader.points - it.points).toInt() }
                val team = leader.team

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onSelectConstructor(team) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCardElevated),
                    border = BorderStroke(1.5.dp, team.color)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        team.color.copy(alpha = 0.35f),
                                        team.color.copy(alpha = 0.12f),
                                        Color(0xFF0F1118)
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Header Row: P1 Badge & Gap
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 8.dp, bottomStart = 8.dp),
                                    color = GoldPodium.copy(alpha = 0.2f),
                                    border = BorderStroke(1.dp, GoldPodium)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(7.dp)
                                                .clip(CircleShape)
                                                .background(GoldPodium)
                                        )
                                        Text(
                                            text = "/ P01 • CONSTRUCTORS LEADER /",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace,
                                            color = GoldPodium,
                                            letterSpacing = 1.sp
                                        )
                                    }
                                }

                                if (gapToP2 != null && gapToP2 > 0) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = F1RedSubtle,
                                        border = BorderStroke(1.dp, F1Red.copy(alpha = 0.6f))
                                    ) {
                                        Text(
                                            text = "+${gapToP2} PTS GAP",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace,
                                            color = MonoWhite
                                        )
                                    }
                                }
                            }

                            // Team Name & Logo
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(44.dp)
                                        .width(68.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ObsidianVoid)
                                        .border(1.dp, team.color.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                        .padding(6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    FormulaTrackrImage(
                                        url = getTeamLogoUrl(team.teamName),
                                        contentDescription = team.teamName,
                                        removeWhiteBackground = false,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Column {
                                    Text(
                                        text = team.teamName.uppercase(),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = MonoWhite
                                    )
                                    Text(
                                        text = "POWER UNIT: ${team.powerUnit.uppercase()}",
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = MonoSilver
                                    )
                                }
                            }

                            // Big Livery Car Render
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = ObsidianVoid.copy(alpha = 0.6f),
                                border = BorderStroke(1.dp, HairlineBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp, horizontal = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    FormulaTrackrImage(
                                        url = getTeamCarUrl(team.teamName),
                                        contentDescription = "${team.teamName} Car",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(85.dp)
                                    )
                                }
                            }

                            // Drivers Lineup & Metrics
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (team.drivers.isNotEmpty()) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        team.drivers.forEach { drvName ->
                                            Surface(
                                                shape = RoundedCornerShape(20.dp),
                                                color = ObsidianVoid,
                                                border = BorderStroke(1.dp, HairlineBorder)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    FormulaTrackrImage(
                                                        url = getDriverHeadshotUrl(drvName),
                                                        contentDescription = drvName,
                                                        modifier = Modifier
                                                            .size(18.dp)
                                                            .clip(CircleShape)
                                                    )
                                                    Text(
                                                        text = drvName.substringAfterLast(" "),
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        fontFamily = FontFamily.Monospace,
                                                        color = MonoWhite
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = ObsidianVoid,
                                        border = BorderStroke(1.dp, HairlineBorder)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = "${leader.points.toInt()}",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Black,
                                                fontFamily = FontFamily.Monospace,
                                                color = MonoWhite
                                            )
                                            Text(
                                                text = "PTS",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black,
                                                fontFamily = FontFamily.Monospace,
                                                color = team.color
                                            )
                                        }
                                    }

                                    if (leader.wins > 0) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = GoldPodium.copy(alpha = 0.15f),
                                            border = BorderStroke(1.dp, GoldPodium.copy(alpha = 0.5f))
                                        ) {
                                            Text(
                                                text = "${leader.wins} WINS",
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black,
                                                fontFamily = FontFamily.Monospace,
                                                color = GoldPodium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // P2 & P3 Constructor Podium Cards
        if (p2 != null) {
            item {
                ConstructorPodiumCard(
                    standing = p2,
                    leaderPoints = leader?.points ?: 1.0,
                    position = 2,
                    medalColor = SilverPodium,
                    onClick = { onSelectConstructor(p2.team) }
                )
            }
        }

        if (p3 != null) {
            item {
                ConstructorPodiumCard(
                    standing = p3,
                    leaderPoints = leader?.points ?: 1.0,
                    position = 3,
                    medalColor = BronzePodium,
                    onClick = { onSelectConstructor(p3.team) }
                )
            }
        }

        // Rest of Grid: P4 to P10+
        itemsIndexed(restOfGrid) { index, item ->
            val pos = index + 4
            val team = item.team
            val relativeProgress = (item.points.toFloat() / maxPoints.toFloat()).coerceIn(0f, 1f)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSelectConstructor(team) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = BorderStroke(1.dp, HairlineBorder)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = format2Digits(pos),
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = MonoSilver,
                                modifier = Modifier.width(26.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .height(36.dp)
                                    .width(52.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ObsidianSurfaceVariant)
                                    .border(1.dp, HairlineBorder, RoundedCornerShape(6.dp))
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                FormulaTrackrImage(
                                    url = getTeamLogoUrl(team.teamName),
                                    contentDescription = team.teamName,
                                    removeWhiteBackground = false,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .width(3.5.dp)
                                    .height(32.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(team.color)
                            )

                            Column {
                                Text(
                                    text = team.teamName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoWhite
                                )
                                Text(
                                    text = "${team.powerUnit} • ${team.drivers.joinToString(", ")}",
                                    fontSize = 11.sp,
                                    color = MonoMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ObsidianVoid,
                            border = BorderStroke(1.dp, HairlineBorder)
                        ) {
                            Text(
                                text = "${item.points.toInt()} PTS",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = MonoWhite
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.5.dp)
                            .background(ObsidianVoid)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(relativeProgress)
                                .height(2.5.dp)
                                .background(team.color)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConstructorPodiumCard(
    standing: ConstructorsStandings,
    leaderPoints: Double,
    position: Int,
    medalColor: Color,
    onClick: () -> Unit
) {
    val team = standing.team
    val gapToLeader = (leaderPoints - standing.points).toInt()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurfaceElevated),
        border = BorderStroke(1.dp, medalColor.copy(alpha = 0.6f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            medalColor.copy(alpha = 0.16f),
                            team.color.copy(alpha = 0.08f),
                            Color(0xFF0F1118)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 6.dp, bottomStart = 6.dp),
                        color = medalColor.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, medalColor)
                    ) {
                        Text(
                            text = "/ P${format2Digits(position)} CONSTRUCTOR /",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = medalColor,
                            letterSpacing = 0.8.sp
                        )
                    }

                    if (gapToLeader > 0) {
                        Text(
                            text = "-${gapToLeader} PTS TO P1",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = MonoMuted
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .width(60.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(ObsidianVoid)
                            .border(1.dp, medalColor, RoundedCornerShape(6.dp))
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        FormulaTrackrImage(
                            url = getTeamLogoUrl(team.teamName),
                            contentDescription = team.teamName,
                            removeWhiteBackground = false,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = team.teamName.uppercase(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = MonoWhite
                        )
                        Text(
                            text = team.powerUnit,
                            fontSize = 11.sp,
                            color = MonoSilver
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ObsidianVoid,
                        border = BorderStroke(1.dp, HairlineBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "${standing.points.toInt()}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = MonoWhite
                            )
                            Text(
                                text = "PTS",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = medalColor
                            )
                        }
                    }
                }
            }
        }
    }
}