package com.jdamcd.runlog.android.profile

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.StravaProfile
import com.jdamcd.runlog.shared.util.RefreshState
import com.jdamcd.runlog.shared.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val stravaProfile: StravaProfile
) : ViewModel(),
    LifecycleObserver {

    private val refreshState = MutableStateFlow(RefreshState.LOADING)

    val flow = stravaProfile.profileFlow()
        .combine(refreshState) { profile, refreshState ->
            when (profile) {
                is Result.Data -> ProfileState.Data(profile.value)
                else -> when (refreshState) {
                    RefreshState.LOADING, RefreshState.SUCCESS -> ProfileState.Loading
                    RefreshState.ERROR -> ProfileState.Error
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfileState.Loading)

    init {
        refresh()
    }

    fun refresh() {
        refreshState.value = RefreshState.LOADING
        viewModelScope.launch {
            refreshState.value = stravaProfile.refresh()
        }
    }
}

sealed class ProfileState {
    object Loading : ProfileState()
    data class Data(val profile: AthleteProfile) : ProfileState()
    object Error : ProfileState()
}
