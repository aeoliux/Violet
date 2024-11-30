package com.github.aeoliux.violet

import androidx.compose.ui.window.ComposeUIViewController
import com.github.aeoliux.violet.app.storage.Database
import com.github.aeoliux.violet.app.storage.DatabaseDriverFactory
import platform.UIKit.UIViewController

fun MainViewController(
    savePassFunc: (password: String) -> Unit,
    getPassFunc: () -> String?,
    deletePassFunc: () -> Unit
): UIViewController {
    Database.open(DatabaseDriverFactory().createDriver())

    return ComposeUIViewController {
        App(
            Keychain(savePassFunc, getPassFunc, deletePassFunc)
        )
    }
}
