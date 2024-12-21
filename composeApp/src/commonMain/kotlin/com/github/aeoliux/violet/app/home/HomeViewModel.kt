package com.github.aeoliux.violet.app.home

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.app.storage.ClassInfo
import com.github.aeoliux.violet.api.types.Me
import com.github.aeoliux.violet.app.storage.AboutUserRepository
import com.github.aeoliux.violet.app.storage.LuckyNumberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val aboutUserRepository: AboutUserRepository,
    private val luckyNumberRepository: LuckyNumberRepository
): ViewModel() {
    private var _isLoaded = MutableStateFlow(false)
    val isLoaded get() = _isLoaded.asStateFlow()

    private var _classInfo = MutableStateFlow<ClassInfo?>(null)
    val classInfo get() = _classInfo.asStateFlow()

    private var _me = MutableStateFlow<Me?>(null)
    val me get() = _me.asStateFlow()

    private var _luckyNumber = MutableStateFlow(0)
    val luckyNumber get() = _luckyNumber.asStateFlow()

    fun launchedEffect(semesterOutput: MutableState<Int>) {
        viewModelScope.launch {
            _me.update { aboutUserRepository.getMe() }
            _classInfo.update { aboutUserRepository.getClassInfo() }
            _luckyNumber.update { luckyNumberRepository.getLuckyNumber() }

            semesterOutput.value = _classInfo.value?.semester?:1

            _isLoaded.update { true }
        }
    }
}