package com.dirzaaulia.formula1.ui.screen.home

import androidx.lifecycle.ViewModel
import com.dirzaaulia.formula1.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NetworkRepository,
): ViewModel() {

    var isInitialAnimationFinished = false

}