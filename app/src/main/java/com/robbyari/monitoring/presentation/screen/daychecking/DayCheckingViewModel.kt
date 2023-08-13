package com.robbyari.monitoring.presentation.screen.daychecking

import androidx.lifecycle.ViewModel
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DayCheckingViewModel @Inject constructor(
    private val repo: MonitoringRepository
) : ViewModel() {

}