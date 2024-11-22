package com.jdamcd.runlog.android.training

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdamcd.runlog.android.main.ROUTE_ACTIVITY_ID
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.StravaActivity
import com.jdamcd.runlog.shared.util.ifError
import com.jdamcd.runlog.shared.util.ifSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val stravaActivity: StravaActivity
) : ViewModel(),
    LifecycleObserver {

    private val id: Long

    private val _flow = MutableStateFlow<ActivityState>(ActivityState.Loading)
    val flow = _flow as StateFlow<ActivityState>

    init {
        id = savedStateHandle.get<Long>(ROUTE_ACTIVITY_ID)!!
        load()
    }

    fun load() {
        _flow.value = ActivityState.Loading
        viewModelScope.launch {
            val result = stravaActivity.activityDetails(id)
            result.ifSuccess { _flow.value = ActivityState.Data(it) }
            result.ifError { _flow.value = ActivityState.Error }
        }
    }

    fun activityWebLink() = stravaActivity.linkUrl(id)
}

sealed class ActivityState {
    object Loading : ActivityState()
    data class Data(val activity: ActivityDetails) : ActivityState()
    object Error : ActivityState()
}
