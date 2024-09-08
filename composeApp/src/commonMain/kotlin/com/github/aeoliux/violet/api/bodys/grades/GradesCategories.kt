package com.github.aeoliux.violet.api.bodys.grades

import com.github.aeoliux.violet.api.bodys.IdAndUrl
import kotlinx.serialization.Serializable

@Serializable
data class GradeCategory(
    val Id: UInt,
    val Color: IdAndUrl,
    val Name: String,
    val CountToTheAverage: Boolean,
    val Weight: UInt = 0u
)

@Serializable
data class GradesCategories(val Categories: List<GradeCategory>) {
    fun getCategoryById(id: UInt): GradeCategory? {
        return this.Categories.firstOrNull { it.Id == id }
    }
}