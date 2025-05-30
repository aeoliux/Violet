package com.github.aeoliux.violet.api.bodys.attendance

import com.github.aeoliux.violet.api.bodys.IdAndUrl
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceType(
    val Id: Int,
    val Name: String,
    val Short: String,
    val Standard: Boolean,
    val ColorRGB: String? = null,
    val Order: Int,
    val Color: IdAndUrl? = null
)

@Serializable
data class AttendancesTypes(val Types: List<AttendanceType>)