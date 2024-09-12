package com.github.aeoliux.violet.api.bodys

import com.github.aeoliux.violet.api.types.ClassInfo
import com.github.aeoliux.violet.api.types.User
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ClassItem(
    val Id: UInt,
    val Number: UInt,
    val Symbol: String,
    val BeginSchoolYear: String,
    val EndFirstSemester: String,
    val EndSchoolYear: String,

    val ClassTutor: IdAndUrl,
    val ClassTutors: List<IdAndUrl>
)

@Serializable
data class Class(val Class: ClassItem) {
    public fun toClassInfo(users: LinkedHashMap<UInt, User>): ClassInfo {
        val classTutors = arrayOf<String>()
        classTutors.plus(users[Class.ClassTutor.Id]?.firstName + " " + users[Class.ClassTutor.Id]?.lastName)
        Class.ClassTutors.forEach {
            classTutors.plus(users[it.Id]?.firstName + " " + users[it.Id]?.lastName)
        }

        val endOfFirstSemester = LocalDate.parse(Class.EndFirstSemester).toEpochDays()
        val now = Clock.System.now().epochSeconds / 60 / 60 / 24

        return ClassInfo(
            Class.Number,
            Class.Symbol,
            classTutors.toList(),
            if (endOfFirstSemester - now > 0) 1u else 2u
        )
    }
}