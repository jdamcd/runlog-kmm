package com.jdamcd.runlog.android.training

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.StravaActivity
import com.jdamcd.runlog.shared.StravaProfile
import com.jdamcd.runlog.shared.util.ifError
import com.jdamcd.runlog.shared.util.ifSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val stravaActivity: StravaActivity,
    private val stravaProfile: StravaProfile
) : ViewModel(), LifecycleObserver {

    private val _statusFlow = MutableStateFlow<StatusBarState>(StatusBarState.NoProfileImage)
    val statusFlow = _statusFlow as StateFlow<StatusBarState>

    private val _contentFlow = MutableStateFlow<TrainingState>(TrainingState.Loading)
    val contentFlow = _contentFlow as StateFlow<TrainingState>

    init {
        load()
    }

    fun load() {
        _contentFlow.value = TrainingState.Loading
        getActivities()
        getProfileImage()
    }

    fun refresh() {
        val state = _contentFlow.value
        _contentFlow.value = if (state is TrainingState.Data) {
            TrainingState.Refreshing(state.activityCards)
        } else {
            TrainingState.Loading
        }
        getActivities()
    }

    private fun getActivities() {
        viewModelScope.launch {
            val result = stravaActivity.activities()
            result.ifSuccess { _contentFlow.value = TrainingState.Data(it) }
            result.ifError { _contentFlow.value = TrainingState.Error }
        }
    }

    private fun getProfileImage() {
        viewModelScope.launch {
            stravaProfile.userImageUrl()?.let {
                _statusFlow.value = StatusBarState.ProfileImage(it)
            }
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

sealed class StatusBarState {
    object NoProfileImage : StatusBarState()
    data class ProfileImage(val url: String) : StatusBarState()
}
