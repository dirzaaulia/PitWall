package com.dirzaaulia.formula1.ui.screen.standings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirzaaulia.formula1.model.response.ConstructorsStandingsResponse
import com.dirzaaulia.formula1.model.response.DriverStandingsResponse
import com.dirzaaulia.formula1.repository.NetworkRepository
import com.dirzaaulia.formula1.util.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class StandingsViewModel @Inject constructor(
    private val repository: NetworkRepository
): ViewModel() {

    private val _driverStandings: MutableStateFlow<ResponseResult<DriverStandingsResponse?>>
        = MutableStateFlow(ResponseResult.Success(null))
    val driverStandings = _driverStandings.asStateFlow()

    private val _constructorsStandings: MutableStateFlow<ResponseResult<ConstructorsStandingsResponse?>>
            = MutableStateFlow(ResponseResult.Success(null))
    val constructorsStandings = _constructorsStandings.asStateFlow()

    fun getStandings(index: Int, year: Int) {
        when (index) {
            0 -> getDriverStandings(year)
            1 -> getConstructorsStandings(year)
        }
    }

    private fun getDriverStandings(year: Int) {
        repository.getDriverStandings(year).onEach {
            _driverStandings.value = it
        }.launchIn(viewModelScope)
    }

    private fun getConstructorsStandings(year: Int) {
        repository.getConstructorsStandings(year).onEach {
            _constructorsStandings.value = it
        }.launchIn(viewModelScope)
    }
}