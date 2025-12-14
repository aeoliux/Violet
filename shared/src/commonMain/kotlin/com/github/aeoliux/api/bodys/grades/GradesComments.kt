package com.github.aeoliux.api.bodys.grades

import com.github.aeoliux.api.bodys.IdAndUrl
import kotlinx.serialization.Serializable

@Serializable
data class GradeComment(
    val Id: Int,
    val AddedBy: IdAndUrl,
    val Grade: IdAndUrl,
    var Text: String
)

@Serializable
data class GradesComments(val Comments: List<GradeComment>) {
    fun getCommentByGradeId(id: Int): String? {
        return this.Comments.firstOrNull { it.Grade.Id == id }?.Text
    }
}