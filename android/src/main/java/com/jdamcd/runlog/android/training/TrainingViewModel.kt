package com.jdamcd.runlog.android.training

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.Strava
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val strava: Strava
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

    private fun getActivities() {
        viewModelScope.launch {
            when (val result = strava.activities()) {
                is Result.Data -> {
                    _mutableFlow.value = TrainingState.Data(result.data)
                }
                is Result.Error -> {
                    _mutableFlow.value = TrainingState.Error(result.recoverable)
                }
            }
        }
    }

    fun generateLink(id: Long) = strava.linkUrl(id)
}

sealed class TrainingState {
    object Loading : TrainingState()
    data class Data(val activityCards: List<ActivityCard>) : TrainingState()
    data class Refreshing(val activityCards: List<ActivityCard>) : TrainingState()
    data class Error(val recoverable: Boolean) : TrainingState()

    fun isRefreshing(): Boolean {
        return this is Refreshing
    }
}
