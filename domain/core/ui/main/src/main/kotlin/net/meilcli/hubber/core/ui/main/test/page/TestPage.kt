package net.meilcli.hubber.core.ui.main.test.page

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TestPage(pageViewModel: TestPageViewModel = hiltViewModel()) {
    LaunchedEffect(pageViewModel) {
        pageViewModel.loadMessage()
        launch {
            var counter = 0
            while (true) {
                delay(1000L)
                pageViewModel.updateMessage("counter: $counter")
                counter += 1
            }
        }
    }
    TestStatelessPage(pageViewModel = pageViewModel)
}

@Composable
fun TestStatelessPage(pageViewModel: TestPageViewModel) {
    Text(text = pageViewModel.message)
}
