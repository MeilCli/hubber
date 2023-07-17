package net.meilcli.hubber.authentication.ui.main.login.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun LoginPageHost(
    pageViewModel: LoginPageViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val pageState = rememberLoginPageState(navigator = navigator)

    LoginPage(pageState = pageState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginPage(pageState: LoginPageState) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Login as User")
                },
                navigationIcon = {
                    IconButton(
                        onClick = pageState.backNavigation
                    ) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Text(text = "ToDo")
        }
    }
}

@Preview(device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    MaterialTheme {
        LoginPage(pageState = LoginPageState(backNavigation = {}))
    }
}
