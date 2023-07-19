package net.meilcli.hubber.home.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.meilcli.hubber.authentication.data.main.IAuthenticationRepository
import net.meilcli.hubber.authentication.data.main.entity.Authentication
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val authenticationRepository: IAuthenticationRepository
) : ViewModel() {

    sealed class Event {

        object LogoutComplete : Event()
    }

    private val _event = Channel<Event>()
    val event = _event.receiveAsFlow()

    fun logout() {
        viewModelScope.launch {
            authenticationRepository.updateAuthentication(Authentication.Never)
            _event.send(Event.LogoutComplete)
        }
    }
}
