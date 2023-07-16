package net.meilcli.hubber.splash.ui.main.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.meilcli.hubber.authentication.data.main.IAuthenticationRepository
import net.meilcli.hubber.authentication.data.main.entity.Authentication
import net.meilcli.hubber.core.ui.main.nullableSaver
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class SplashPageViewModel @Inject constructor(
    private val authenticationRepository: IAuthenticationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var authentication by savedStateHandle.saveable("authentication", stateSaver = nullableSaver()) {
        mutableStateOf<Authentication?>(null)
    }
        private set

    fun loadAuthentication() {
        viewModelScope.launch {
            authentication = authenticationRepository.getAuthentication()
        }
    }
}
