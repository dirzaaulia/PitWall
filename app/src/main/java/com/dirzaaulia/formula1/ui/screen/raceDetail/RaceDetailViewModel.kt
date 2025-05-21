package com.dirzaaulia.formula1.ui.screen.raceDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirzaaulia.formula1.model.response.DriverResponse
import com.dirzaaulia.formula1.model.response.RaceResultResponse
import com.dirzaaulia.formula1.repository.NetworkRepository
import com.dirzaaulia.formula1.util.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RaceDetailViewModel @Inject constructor(
    private val repository: NetworkRepository
) : ViewModel() {

    private val _driverDetail = MutableStateFlow<ResponseResult<DriverResponse?>>(
        ResponseResult.Success(null)
    )
    val driverDetail = _driverDetail.asStateFlow()

    private val _raceResult = MutableStateFlow<ResponseResult<RaceResultResponse?>>(
        ResponseResult.Success(null)
    )
    val raceResult = _raceResult.asStateFlow()

    fun getDriverDetail(driverId: String) {
        repository.getDriverDetail(driverId = driverId).onEach {
            _driverDetail.value = it
        }.launchIn(viewModelScope)
    }

    fun getRaceResult(
        year: Int,
        round: Int
    ) {
        repository.getRaceResult(
            type = "race",
            year = year,
            round = round
        ).onEach {
            _raceResult.value = it
        }.launchIn(viewModelScope)
    }
}