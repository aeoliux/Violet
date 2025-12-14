package com.github.aeoliux.api.bodys.grades

import com.github.aeoliux.api.bodys.IdAndUrl
import kotlinx.serialization.Serializable

@Serializable
data class GradeCategory(
    val Id: Int,
    val Color: IdAndUrl,
    val Name: String,
    val CountToTheAverage: Boolean,
    val Weight: Int = 0
)

@Serializable
data class GradesCategories(val Categories: List<GradeCategory>) {
    fun getCategoryById(id: Int): GradeCategory? {
        return this.Categories.firstOrNull { it.Id == id }
    }
}