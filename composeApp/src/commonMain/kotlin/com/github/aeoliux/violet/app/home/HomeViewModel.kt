package com.github.aeoliux.violet.app.home

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.api.types.Lesson
import com.github.aeoliux.violet.app.storage.ClassInfo
import com.github.aeoliux.violet.api.types.Me
import com.github.aeoliux.violet.app.storage.AboutUserRepository
import com.github.aeoliux.violet.app.storage.LuckyNumberRepository
import com.github.aeoliux.violet.app.storage.TimetableRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HomeViewModel(
    private val aboutUserRepository: AboutUserRepository,
    private val luckyNumberRepository: LuckyNumberRepository,
    private val timetableRepository: TimetableRepository
): ViewModel() {
    private var _isLoaded = MutableStateFlow(false)
    val isLoaded get() = _isLoaded.asStateFlow()

    private var _classInfo = MutableStateFlow<ClassInfo?>(null)
    val classInfo get() = _classInfo.asStateFlow()

    private var _me = MutableStateFlow<Me?>(null)
    val me get() = _me.asStateFlow()

    private var _luckyNumber = MutableStateFlow(0)
    val luckyNumber get() = _luckyNumber.asStateFlow()

    private var _nextLessons = MutableStateFlow<Pair<LocalDate?, LinkedHashMap<LocalTime, List<Lesson>>?>>(
        Pair(null, null)
    )
    val nextLessons get() = _nextLessons.asStateFlow()

    fun launchedEffect(semesterOutput: MutableState<Int>) {
        viewModelScope.launch {
            _me.update { aboutUserRepository.getMe() }
            _classInfo.update { aboutUserRepository.getClassInfo() }
            _luckyNumber.update { luckyNumberRepository.getLuckyNumber() }

            val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val timetable = timetableRepository.getTimetable()
            // Check just for next 10 days
            (0..7).forEach {
                if (_nextLessons.value.first != null)
                    return@forEach

                val date = LocalDate.fromEpochDays(currentDateTime.date.toEpochDays() + it)
                timetable[date]?.let { timetable ->
                    val hours = timetable.keys.sorted()
                    timetable[hours.getOrNull(hours.size - 1)]?.forEach {
                        if (it.timeTo > currentDateTime.time || date > currentDateTime.date) {
                            _nextLessons.update { Pair(date, timetable) }
                            return@forEach
                        }
                    }
                }
            }

            semesterOutput.value = _classInfo.value?.semester?:1

            _isLoaded.update { true }
        }
    }
}