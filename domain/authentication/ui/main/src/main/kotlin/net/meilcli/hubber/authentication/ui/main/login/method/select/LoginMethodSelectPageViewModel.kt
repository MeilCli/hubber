package net.meilcli.hubber.authentication.ui.main.login.method.select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.meilcli.hubber.authentication.data.main.IAuthenticationRepository
import net.meilcli.hubber.authentication.data.main.entity.Authentication
import javax.inject.Inject

@HiltViewModel
class LoginMethodSelectPageViewModel @Inject constructor(
    private val authenticationRepository: IAuthenticationRepository
) : ViewModel() {

    sealed class Event {

        object AuthenticateCompleteAsGuest : Event()
    }

    private val _event = Channel<Event>()
    val event: Flow<Event> = _event.receiveAsFlow()

    fun authenticateAsGuest() {
        viewModelScope.launch {
            authenticationRepository.updateAuthentication(Authentication.Guest)
            _event.send(Event.AuthenticateCompleteAsGuest)
        }
    }
}
