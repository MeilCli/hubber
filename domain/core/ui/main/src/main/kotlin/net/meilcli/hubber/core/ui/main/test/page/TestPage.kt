package net.meilcli.hubber.core.ui.main.test.page

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TestPage(pageViewModel: TestPageViewModel = viewModel()) {
    TestStatelessPage(pageViewModel = pageViewModel)
}

@Composable
fun TestStatelessPage(pageViewModel: TestPageViewModel) {
    Text(text = pageViewModel.message)
}
