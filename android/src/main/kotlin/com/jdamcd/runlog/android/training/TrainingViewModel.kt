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
) : ViewModel(),
    LifecycleObserver {

    private val _toolbarFlow = MutableStateFlow<ToolbarState>(ToolbarState.NoProfileImage)
    val toolbarFlow = _toolbarFlow as StateFlow<ToolbarState>

    private val refreshState = MutableStateFlow(RefreshState.LOADING)

    val contentFlow = stravaActivity.activitiesFlow()
        .combine(refreshState) { activities, refreshState ->
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
        refreshState.value = RefreshState.LOADING
        viewModelScope.launch {
            refreshState.value = stravaActivity.refresh()
        }
    }

    private fun getProfileImage() {
        viewModelScope.launch {
            stravaProfile.userImageUrl()?.let {
                _toolbarFlow.value = ToolbarState.ProfileImage(it)
            }
        }
    }
}

sealed class TrainingState {
    object Loading : TrainingState()
    data class Data(val activityCards: List<ActivityCard>) : TrainingState()
    data class Refreshing(val activityCards: List<ActivityCard>) : TrainingState()
    object Error : TrainingState()

    fun isRefreshing(): Boolean = this is Refreshing
}

sealed class ToolbarState {
    object NoProfileImage : ToolbarState()
    data class ProfileImage(val url: String) : ToolbarState()
}
