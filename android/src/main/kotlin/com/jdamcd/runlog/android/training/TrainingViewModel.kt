package com.jdamcd.runlog.android.training

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.StravaActivity
import com.jdamcd.runlog.shared.util.ifError
import com.jdamcd.runlog.shared.util.ifSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val stravaActivity: StravaActivity
) : ViewModel(), LifecycleObserver {

    private val _mutableFlow = MutableStateFlow<TrainingState>(TrainingState.Loading)
    val flow = _mutableFlow as StateFlow<TrainingState>

    init {
        load()
    }

    fun load() {
        _mutableFlow.value = TrainingState.Loading
        getActivities()
    }

    fun refresh() {
        val state = _mutableFlow.value
        if (state is TrainingState.Data) {
            _mutableFlow.value = TrainingState.Refreshing(state.activityCards)
        } else {
            _mutableFlow.value = TrainingState.Loading
        }
        getActivities()
    }

    fun setDarkTheme(enabled: Boolean) {
        stravaActivity.requestDarkModeImages(enabled)
    }

    private fun getActivities() {
        viewModelScope.launch {
            val result = stravaActivity.activities()
            result.ifSuccess { _mutableFlow.value = TrainingState.Data(it) }
            result.ifError { _mutableFlow.value = TrainingState.Error }
        }
    }
}

sealed class TrainingState {
    object Loading : TrainingState()
    data class Data(val activityCards: List<ActivityCard>) : TrainingState()
    data class Refreshing(val activityCards: List<ActivityCard>) : TrainingState()
    object Error : TrainingState()

    fun isRefreshing(): Boolean {
        return this is Refreshing
    }
}
