package com.jdamcd.runlog.android.profile

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.StravaProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val stravaProfile: StravaProfile
) : ViewModel(), LifecycleObserver {

    val flow = stravaProfile.profileFlow()
        .map { ProfileState.Data(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfileState.Loading)

    fun refresh() {
        stravaProfile.refresh()
    }
}

sealed class ProfileState {
    object Loading : ProfileState()
    data class Data(val profile: AthleteProfile) : ProfileState()
    object Error : ProfileState()
}
