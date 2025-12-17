package com.github.aeoliux.violet.api.bodys

import com.github.aeoliux.violet.api.types.ClassInfo
import com.github.aeoliux.violet.api.types.User
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
data class ClassItem(
    val Id: Int,
    val Number: Int,
    val Symbol: String,
    val BeginSchoolYear: String,
    val EndFirstSemester: String,
    val EndSchoolYear: String,

    val ClassTutor: IdAndUrl,
    val ClassTutors: List<IdAndUrl>
)

@Serializable
data class Class(val Class: ClassItem) {
    @OptIn(ExperimentalTime::class)
    public fun toClassInfo(users: LinkedHashMap<Int, User>): ClassInfo {
        val classTutors: List<String> = emptyList<IdAndUrl>().plus(Class.ClassTutor).plus(Class.ClassTutors)
            .map {
                users[it.Id]?.firstName + " " + users[it.Id]?.lastName
            }

        val endOfFirstSemester = LocalDate.parse(Class.EndFirstSemester).toEpochDays()
        val now = Clock.System.now().epochSeconds / 60 / 60 / 24

        return ClassInfo(
            Class.Number,
            Class.Symbol,
            classTutors,
            if (endOfFirstSemester - now > 0) 1 else 2
        )
    }
}