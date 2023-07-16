package net.meilcli.hubber.core.ui.main.test.page

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.meilcli.hubber.core.data.main.ITestDataSource
import javax.inject.Inject

@HiltViewModel
class TestPageViewModel @Inject constructor(
    private val testDataSource: ITestDataSource
) : ViewModel() {

    var message: String by mutableStateOf("")
        private set

    suspend fun loadMessage() {
        viewModelScope.launch {
            message = testDataSource.getMessage()
        }
    }

    suspend fun updateMessage(message: String) {
        viewModelScope.launch {
            testDataSource.updateMessage(message)
            this@TestPageViewModel.message = testDataSource.getMessage()
        }
    }
}
