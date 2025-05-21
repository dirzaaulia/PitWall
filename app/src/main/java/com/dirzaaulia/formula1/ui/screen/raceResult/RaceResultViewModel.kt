package com.dirzaaulia.formula1.ui.screen.raceResult

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class RaceResultViewModel @Inject constructor(
    private val repository: NetworkRepository
): ViewModel() {

    private val _raceResult = MutableStateFlow<ResponseResult<RaceResultResponse?>>(
        ResponseResult.Success(null)
    )
    val raceResult = _raceResult.asStateFlow()

    fun getRaceResult(
        isSprint: Boolean,
        type: String,
        year: Int,
        round: Int
    ) {
        Log.d("TAG_DIRZA", "$isSprint | $type | $year | $round")
        if (isSprint) {
            repository.getSprintResult(
                type = type.lowercase(),
                year = year,
                round = round
            ).onEach {
                _raceResult.value = it
            }.launchIn(viewModelScope)
        } else {
            repository.getRaceResult(
                type = type.lowercase(),
                year = year,
                round = round
            ).onEach {
                _raceResult.value = it
            }.launchIn(viewModelScope)
        }
    }
}