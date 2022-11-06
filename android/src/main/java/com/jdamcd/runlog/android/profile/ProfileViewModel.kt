package com.jdamcd.runlog.android.profile

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.Strava
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val strava: Strava
) : ViewModel(), LifecycleObserver {

    private val liveData = MutableLiveData<ProfileState>()
    private val uiModel = liveData as LiveData<ProfileState>

    fun init(): LiveData<ProfileState> {
        load()
        return uiModel
    }

    fun load() {
        liveData.value = ProfileState.Loading
        viewModelScope.launch {
            when (val result = strava.profile()) {
                is Result.Data -> {
                    liveData.value = ProfileState.Data(result.data)
                }
                is Result.Error -> {
                    liveData.value = ProfileState.Error(result.recoverable)
                }
            }
        }
    }
}

sealed class ProfileState {
    object Loading : ProfileState()
    data class Data(val profile: AthleteProfile) : ProfileState()
    data class Error(val recoverable: Boolean) : ProfileState()
}
