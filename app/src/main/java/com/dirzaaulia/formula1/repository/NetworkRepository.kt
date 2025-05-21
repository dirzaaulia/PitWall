package com.dirzaaulia.formula1.repository

import com.dirzaaulia.formula1.model.response.ConstructorsStandingsResponse
import com.dirzaaulia.formula1.model.response.DriverResponse
import com.dirzaaulia.formula1.model.response.DriverStandingsResponse
import com.dirzaaulia.formula1.model.response.RaceResultResponse
import com.dirzaaulia.formula1.model.response.ScheduleResponse
import com.dirzaaulia.formula1.util.ResponseResult
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun getCurrentSchedule(): Flow<ResponseResult<ScheduleResponse>>
    fun getSchedule(year: Int): Flow<ResponseResult<ScheduleResponse>>
    fun getDriverDetail(driverId: String): Flow<ResponseResult<DriverResponse>>
    fun getRaceResult(
        type: String,
        year: Int,
        round: Int
    ): Flow<ResponseResult<RaceResultResponse>>
    fun getSprintResult(
        type: String,
        year: Int,
        round: Int
    ): Flow<ResponseResult<RaceResultResponse>>
    fun getDriverStandings(year: Int): Flow<ResponseResult<DriverStandingsResponse>>
    fun getConstructorsStandings(year: Int): Flow<ResponseResult<ConstructorsStandingsResponse>>
}