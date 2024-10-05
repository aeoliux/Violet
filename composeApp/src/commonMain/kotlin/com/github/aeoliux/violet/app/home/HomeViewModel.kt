package com.github.aeoliux.violet.app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.api.types.ClassInfo
import com.github.aeoliux.violet.api.types.Me
import com.github.aeoliux.violet.storage.Database
import com.github.aeoliux.violet.storage.selectAboutMe
import com.github.aeoliux.violet.storage.selectClassInfo
import com.github.aeoliux.violet.storage.selectLuckyNumber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private var _isLoaded = MutableStateFlow(false)
    val isLoaded get() = _isLoaded.asStateFlow()

    private var _classInfo = MutableStateFlow<ClassInfo?>(null)
    val classInfo get() = _classInfo.asStateFlow()

    private var _me = MutableStateFlow<Me?>(null)
    val me get() = _me.asStateFlow()

    private var _luckyNumber = MutableStateFlow(0)
    val luckyNumber get() = _luckyNumber.asStateFlow()

    fun launchedEffect() {
        viewModelScope.launch {
            _me.update { Database.selectAboutMe() }
            _classInfo.update { Database.selectClassInfo() }
            _luckyNumber.update { Database.selectLuckyNumber()?.first?.toInt()?: 0 }

            _isLoaded.update { true }
        }
    }
}