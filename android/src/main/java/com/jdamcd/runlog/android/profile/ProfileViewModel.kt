package com.jdamcd.runlog.android.profile

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.StravaProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val stravaProfile: StravaProfile
) : ViewModel(), LifecycleObserver {

    private val _mutableFlow = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val flow = _mutableFlow as StateFlow<ProfileState>

    init {
        load()
    }

    fun load() {
        _mutableFlow.value = ProfileState.Loading
        viewModelScope.launch {
            when (val result = stravaProfile.profile()) {
                is Result.Data -> {
                    _mutableFlow.value = ProfileState.Data(result.data)
                }
                is Result.Error -> {
                    _mutableFlow.value = ProfileState.Error(result.recoverable)
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
