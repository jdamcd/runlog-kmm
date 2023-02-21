package com.jdamcd.runlog.android.training

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.StravaActivity
import com.jdamcd.runlog.shared.StravaProfile
import com.jdamcd.runlog.shared.util.RefreshState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val stravaActivity: StravaActivity,
    private val stravaProfile: StravaProfile
) : ViewModel(), LifecycleObserver {

    private val _statusBarFlow = MutableStateFlow<StatusBarState>(StatusBarState.NoProfileImage)
    val statusFlow = _statusBarFlow as StateFlow<StatusBarState>

    private val _refreshState = MutableStateFlow(RefreshState.LOADING)

    val contentFlow = stravaActivity.activitiesFlow()
        .combine(_refreshState) { activities, refreshState ->
            if (activities.isEmpty()) {
                when (refreshState) {
                    RefreshState.LOADING, RefreshState.SUCCESS -> TrainingState.Loading
                    RefreshState.ERROR -> TrainingState.Error
                }
            } else {
                if (refreshState == RefreshState.LOADING) {
                    TrainingState.Refreshing(activities)
                } else {
                    TrainingState.Data(activities)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TrainingState.Loading)

    init {
        refresh()
        getProfileImage()
    }

    fun refresh() {
        _refreshState.value = RefreshState.LOADING
        viewModelScope.launch {
            _refreshState.value = stravaActivity.refresh()
        }
    }

    private fun getProfileImage() {
        viewModelScope.launch {
            stravaProfile.userImageUrl()?.let {
                _statusBarFlow.value = StatusBarState.ProfileImage(it)
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
