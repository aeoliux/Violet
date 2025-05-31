package com.github.aeoliux.violet.app.grades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.api.types.Grade
import com.github.aeoliux.violet.app.storage.GradesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt

class GradesViewModel(private val repository: GradesRepository): ViewModel() {
    private var _isLoaded = MutableStateFlow(false)
    val isLoaded get() = _isLoaded.asStateFlow()

    private var _grades = MutableStateFlow(LinkedHashMap<String, List<Grade>>())
    val grades get() = _grades.asStateFlow()

    fun launchedEffect() {
        viewModelScope.launch {
            _grades.update { repository.getGrades() }
            _isLoaded.update { true }
        }
    }
}

fun List<Grade>.countAverage(): Float? {
    val grades = this.filter { it.weight > 0 && it.grade != "0" && it.grade != "+" && it.grade != "-" }
    val div = grades.fold(0.0f) { acc, grade ->
        acc + grade.weight.toFloat()
    }

    return when (div) {
        0.0f -> null
        else -> grades.fold(0.0f) { acc, grade ->
            acc + (grade.value() * grade.weight)
        } / div
    }
}

fun Float.roundToString(digits: Int): String {
    val mul = (10.0f).pow(digits)
    val cut = (this * mul).roundToInt().toFloat() / mul
    val str = "${cut}00"
    val dot = str.indexOfFirst { it == '.' || it == ',' }
    return str
        .slice(
            (
                    0..(
                        if (dot == -1) 0 else dot + digits
                        )
                    )
        )
}