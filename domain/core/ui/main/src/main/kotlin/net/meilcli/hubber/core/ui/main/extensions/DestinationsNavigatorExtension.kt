package net.meilcli.hubber.core.ui.main.extensions

import androidx.activity.OnBackPressedDispatcher
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

fun DestinationsNavigator.popBackStackOrFinishActivity(onBackPressedDispatcher: OnBackPressedDispatcher?) {
    if (popBackStack().not()) {
        onBackPressedDispatcher?.onBackPressed()
    }
}
