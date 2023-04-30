package net.meilcli.hubber.core.ui.main.test.page

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import net.meilcli.hubber.core.data.main.ITestDataSource
import javax.inject.Inject

@HiltViewModel
class TestPageViewModel @Inject constructor(
    private val testDataSource: ITestDataSource
) : ViewModel() {

    var message by mutableStateOf<String>("")
        private set

    init {
        message = testDataSource.hello()
    }
}
