package com.jdamcd.runlog.android.training

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.Strava
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val strava: Strava
) : ViewModel(), LifecycleObserver {

    private val liveData = MutableLiveData<TrainingState>()
    val uiModel = liveData as LiveData<TrainingState>

    fun init(): LiveData<TrainingState> {
        load()
        return uiModel
    }

    fun load() {
        liveData.value = TrainingState.Loading
        viewModelScope.launch {
            when (val result = strava.activities()) {
                is Result.Data -> {
                    liveData.value = TrainingState.Data(result.data)
                }
                is Result.Error -> {
                    liveData.value = TrainingState.Error(result.recoverable)
                }
            }
        }
    }

    fun generateLink(id: Long) = strava.linkUrl(id)
}

sealed class TrainingState {
    object Loading : TrainingState()
    data class Data(val activityCards: List<ActivityCard>) : TrainingState()
    data class Error(val recoverable: Boolean) : TrainingState()
}
