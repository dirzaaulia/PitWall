package com.dirzaaulia.formula1.repository

import com.dirzaaulia.formula1.model.response.ConstructorsStandingsResponse
import com.dirzaaulia.formula1.model.response.DriverResponse
import com.dirzaaulia.formula1.model.response.DriverStandingsResponse
import com.dirzaaulia.formula1.model.response.RaceResultResponse
import com.dirzaaulia.formula1.model.response.ScheduleResponse
import com.dirzaaulia.formula1.network.Drivers
import com.dirzaaulia.formula1.network.ScheduleCurrent
import com.dirzaaulia.formula1.util.ResponseResult
import com.dirzaaulia.formula1.util.executeWithData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.dirzaaulia.formula1.network.RaceResult as RaceResultResource

class NetworkRepositoryImpl @Inject constructor(
    private val ktorClient: HttpClient
) : NetworkRepository {
    override fun getCurrentSchedule() = flow {
        emit(ResponseResult.Loading)
        emit(executeWithData<ScheduleResponse> {
            ktorClient.get(ScheduleCurrent()).body()
        })
    }.flowOn(Dispatchers.IO)

    override fun getSchedule(year: Int) = flow {
        emit(ResponseResult.Loading)
        emit(executeWithData<ScheduleResponse> {
            val resource = RaceResultResource.Year(
                year = year
            )
            ktorClient.get(resource).body()
        })
    }

    override fun getDriverDetail(driverId: String) = flow {
        emit(ResponseResult.Loading)
        emit(
            executeWithData<DriverResponse> {
                ktorClient.get(Drivers.DriverId(driverId = driverId)).body()
            }
        )
    }.flowOn(Dispatchers.IO)

    override fun getRaceResult(
        type: String,
        year: Int,
        round: Int
    ) = flow {
        emit(ResponseResult.Loading)
        emit(
            executeWithData<RaceResultResponse> {
                val resource = RaceResultResource.Year.Round.Type(
                    parent = RaceResultResource.Year.Round(
                        parent = RaceResultResource.Year(year = year),
                        round = round
                    ),
                    type = type
                )
                ktorClient.get(resource).body()
            }
        )
    }.flowOn(Dispatchers.IO)

    override fun getSprintResult(
        type: String,
        year: Int,
        round: Int
    ) = flow {
        emit(ResponseResult.Loading)
        if (type.equals("sprint qualifying", true)) {
            emit(
                executeWithData<RaceResultResponse> {
                    val resource = RaceResultResource.Year.Round.Sprint.Type(
                        parent =  RaceResultResource.Year.Round.Sprint(
                          parent =  RaceResultResource.Year.Round(
                              parent = RaceResultResource.Year(
                                  year = year
                              ),
                              round = round
                          )
                        ),
                        type = "qualy"
                    )
                    ktorClient.get(resource).body()
                }
            )
        } else {
            emit(
                executeWithData {
                    val resource = RaceResultResource.Year.Round.Sprint.Type(
                        parent =  RaceResultResource.Year.Round.Sprint(
                            parent =  RaceResultResource.Year.Round(
                                parent = RaceResultResource.Year(
                                    year = year
                                ),
                                round = round
                            )
                        ),
                        type = "race"
                    )
                    ktorClient.get(resource).body()
                }
            )
        }
    }.flowOn(Dispatchers.IO)

    override fun getDriverStandings(year: Int) = flow {
        emit(ResponseResult.Loading)
        emit(
            executeWithData<DriverStandingsResponse> {
                val resource = RaceResultResource.Year.DriverChampionship(
                    parent = RaceResultResource.Year(
                        year = year
                    )
                )
                ktorClient.get(resource).body()
            }
        )
    }.flowOn(Dispatchers.IO)

    override fun getConstructorsStandings(year: Int) = flow {
        emit(ResponseResult.Loading)
        emit(
            executeWithData<ConstructorsStandingsResponse> {
                val resource = RaceResultResource.Year.ConstructorsChampionship(
                    parent = RaceResultResource.Year(
                        year = year
                    )
                )
                ktorClient.get(resource).body()
            }
        )
    }.flowOn(Dispatchers.IO)
}