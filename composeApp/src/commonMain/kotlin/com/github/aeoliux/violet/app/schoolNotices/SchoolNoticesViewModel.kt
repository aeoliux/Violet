package com.github.aeoliux.violet.app.schoolNotices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.api.types.SchoolNotice
import com.github.aeoliux.violet.app.storage.Database
import com.github.aeoliux.violet.app.storage.selectListOfSchoolNotices
import com.github.aeoliux.violet.app.storage.selectSchoolNotice
import comgithubaeoliuxvioletstorage.SelectListOfSchoolNotices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SchoolNoticesViewModel: ViewModel() {
    private var _listOfSchoolNotices: MutableStateFlow<List<SelectListOfSchoolNotices>> = MutableStateFlow(emptyList())
    val listOfSchoolNotices get() = _listOfSchoolNotices.asStateFlow()

    fun launchedEffect() {
        viewModelScope.launch {
            _listOfSchoolNotices.update { Database.selectListOfSchoolNotices()?: emptyList() }
        }
    }
}