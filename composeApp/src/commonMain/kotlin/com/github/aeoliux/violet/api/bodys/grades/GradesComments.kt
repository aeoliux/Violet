package com.github.aeoliux.violet.api.bodys.grades

import com.github.aeoliux.violet.api.bodys.IdAndUrl
import kotlinx.serialization.Serializable

@Serializable
data class GradeComment(
    val Id: UInt,
    val AddedBy: IdAndUrl,
    val Grade: IdAndUrl,
    var Text: String
)

@Serializable
data class GradesComments(val Comments: List<GradeComment>) {
    fun getCommentByGradeId(id: UInt): String? {
        return this.Comments.firstOrNull { it.Grade.Id == id }?.Text
    }
}