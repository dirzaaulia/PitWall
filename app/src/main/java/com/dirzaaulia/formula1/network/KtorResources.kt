package com.dirzaaulia.formula1.network

import io.ktor.resources.Resource

@Resource("/api/current")
class ScheduleCurrent()

@Resource("/api/drivers")
class Drivers() {
    @Resource("{driverId}")
    class DriverId(val parent: Drivers = Drivers(), val driverId: String)
}

@Resource("/api")
class RaceResult {
    @Resource("{year}")
    class Year(val parent: RaceResult = RaceResult(), val year: Int) {
        @Resource("{round}")
        class Round(val parent: Year, val round: Int) { // parent should be of type Year
            @Resource("{type}")
            class Type(val parent: Round, val type: String) // parent should be of type Round
            @Resource("/sprint")
            class Sprint(val parent: Round) {
                @Resource("{type}")
                class Type(val parent: Sprint, val type: String) // parent should be of type Round
            }
        }
        @Resource("/drivers-championship")
        class DriverChampionship(val parent: Year)
        @Resource("/constructors-championship")
        class ConstructorsChampionship(val parent: Year)
    }
}