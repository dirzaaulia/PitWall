package com.dirzaaulia.formula1.ui.screen.race

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirzaaulia.formula1.model.response.ScheduleResponse
import com.dirzaaulia.formula1.repository.NetworkRepository
import com.dirzaaulia.formula1.util.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RaceViewModel @Inject constructor(
    private val repository: NetworkRepository,
): ViewModel() {

    private val _scheduleResult = MutableStateFlow<ResponseResult<ScheduleResponse?>>(
        ResponseResult.Success(null)
    )
    val scheduleResult = _scheduleResult.asStateFlow()

    private val _isAnimationDone = MutableStateFlow(false)
    val isAnimationDone = _isAnimationDone.asStateFlow()

    fun getSchedule(year: Int) {
        repository.getSchedule(year).onEach {
            _scheduleResult.value = it
        }.launchIn(viewModelScope)
    }

    fun setIsAnimationDone()  {
        _isAnimationDone.value = true
    }
}