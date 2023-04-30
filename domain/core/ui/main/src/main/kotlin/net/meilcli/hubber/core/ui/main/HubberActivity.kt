package net.meilcli.hubber.core.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import net.meilcli.hubber.core.ui.main.test.page.TestPage

abstract class HubberActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestPage()
        }
    }
}
