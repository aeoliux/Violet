package com.github.aeoliux.violet.app.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.Keychain
import com.github.aeoliux.violet.app.storage.Database
import com.github.aeoliux.violet.app.storage.wipeData
import kotlinx.coroutines.launch

class SettingsViewModel(): ViewModel() {
    fun logOut(keychain: Keychain) {
        viewModelScope.launch {
            Database.wipeData()
            keychain.deletePass()


        }
    }
}