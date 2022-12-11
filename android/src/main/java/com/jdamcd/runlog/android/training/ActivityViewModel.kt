package com.jdamcd.runlog.android.training

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdamcd.runlog.android.main.ROUTE_ACTIVITY_ID
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.StravaActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val stravaActivity: StravaActivity
) : ViewModel(), LifecycleObserver {

    private val id: Long

    private val _mutableFlow = MutableStateFlow<ActivityState>(ActivityState.Loading)
    val flow = _mutableFlow as StateFlow<ActivityState>

    init {
        id = savedStateHandle.get<Long>(ROUTE_ACTIVITY_ID)!!
        load()
    }

    fun load() {
        _mutableFlow.value = ActivityState.Loading
        viewModelScope.launch {
            when (val result = stravaActivity.activityDetails(id)) {
                is Result.Data -> {
                    _mutableFlow.value = ActivityState.Data(result.data)
                }
                is Result.Error -> {
                    _mutableFlow.value = ActivityState.Error(result.recoverable)
                }
            }
        }
    }

    fun activityWebLink() = stravaActivity.linkUrl(id)

    fun setDarkTheme(enabled: Boolean) {
        stravaActivity.requestDarkModeImages(enabled)
    }
}

sealed class ActivityState {
    object Loading : ActivityState()
    data class Data(val activity: ActivityDetails) : ActivityState()
    data class Error(val recoverable: Boolean) : ActivityState()
}
