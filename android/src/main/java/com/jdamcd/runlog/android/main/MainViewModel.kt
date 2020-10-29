package com.jdamcd.runlog.android.main

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.Strava
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val strava: Strava
) : ViewModel(), LifecycleObserver {

    private val liveData = MutableLiveData<ActivityFeedState>()
    val uiModel = liveData as LiveData<ActivityFeedState>

    fun load() {
        liveData.value = ActivityFeedState.Loading
        viewModelScope.launch {
            when (val result = strava.activities()) {
                is Result.Data -> {
                    liveData.value = ActivityFeedState.Data(result.data)
                }
                is Result.Error -> {
                    liveData.value = ActivityFeedState.Error(result.recoverable)
                }
            }
        }
    }

    fun openLink(context: Context, id: Long) {
        val intent = Intent(Intent.ACTION_VIEW, strava.linkUrl(id).toUri())
        context.startActivity(intent)
    }
}

sealed class ActivityFeedState {
    object Loading : ActivityFeedState()
    data class Data(val activityCards: List<ActivityCard>) : ActivityFeedState()
    data class Error(val recoverable: Boolean) : ActivityFeedState()
}
