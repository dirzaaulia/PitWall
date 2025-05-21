package com.dirzaaulia.formula1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.dirzaaulia.formula1.ui.screen.app_info.AppInfo
import com.dirzaaulia.formula1.ui.screen.raceResult.RaceResult
import com.dirzaaulia.formula1.ui.screen.raceDetail.RaceDetail
import com.dirzaaulia.formula1.ui.screen.home.HomeScreen
import com.dirzaaulia.formula1.ui.screen.race.Race
import com.dirzaaulia.formula1.ui.screen.standings.Standings


@Composable
fun NavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            HomeScreen(
                navigateToRace = { navController.navigate(Race) },
                navigateToStandings = { navController.navigate(Standings) },
                navigateToAppInfo = { navController.navigate(AppInfo) }
            )
        }
        composable<Race> {
            Race(
                navigateToRaceDetail = { raceDetail ->
                    navController.navigate(raceDetail)
                }
            )
        }
        composable<RaceDetail> { backStackEntry ->
            val raceDetail: RaceDetail = backStackEntry.toRoute()
            RaceDetail(
                data = raceDetail,
                navigateToRaceResult = { raceResult ->
                    navController.navigate(raceResult)
                }
            )
        }
        composable<RaceResult> { backStackEntry ->
            val raceResult: RaceResult = backStackEntry.toRoute()
            RaceResult(data = raceResult)
        }
        composable<Standings> {
            Standings()
        }
        composable<AppInfo> {
            AppInfo()
        }
    }
}