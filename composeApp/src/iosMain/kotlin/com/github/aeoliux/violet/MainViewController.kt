package com.github.aeoliux.violet

import androidx.compose.ui.window.ComposeUIViewController
import com.github.aeoliux.violet.app.appModules
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import platform.UIKit.UIViewController

fun MainViewController(
    savePassFunc: (password: String) -> Unit,
    getPassFunc: () -> String?,
    deletePassFunc: () -> Unit
): UIViewController {
    val keychain = Keychain(savePassFunc, getPassFunc, deletePassFunc)
    val koinModules = appModules + module {
        single { keychain } bind Keychain::class
    }

    startKoin {
        modules(koinModules)
    }

    return ComposeUIViewController {
        App()
    }
}
