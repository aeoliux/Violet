package com.github.aeoliux.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class RefreshableViewModel: ViewModel() {
    private var _isRefreshing = MutableStateFlow(false)
    val isRefreshing get() = _isRefreshing.asStateFlow()

    fun task(closure: suspend () -> Unit) {
        viewModelScope.launch {
            _isRefreshing.update { true }
            closure()
            _isRefreshing.update { false }
        }
    }
}