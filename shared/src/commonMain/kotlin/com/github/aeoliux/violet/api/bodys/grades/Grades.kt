package com.github.aeoliux.violet.api.bodys.grades

import com.github.aeoliux.violet.api.types.Grade
import com.github.aeoliux.violet.api.types.GradeType
import com.github.aeoliux.violet.api.bodys.IdAndUrl
import com.github.aeoliux.violet.api.types.User
import com.github.aeoliux.violet.api.localDateTimeFormat
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class GradeData(
    val Id: Int,
    val Lesson: IdAndUrl,
    val Subject: IdAndUrl,
    val Student: IdAndUrl,
    val Category: IdAndUrl,
    val AddedBy: IdAndUrl,
    val Grade: String,
    val AddDate: String,
    val Semester: Int,
    val IsConstituent: Boolean,
    val IsSemester: Boolean,
    val IsSemesterProposition: Boolean,
    val IsFinal: Boolean,
    val IsFinalProposition: Boolean,
)

@Serializable
data class Grades(val Grades: List<GradeData>) {
    fun toGrades(
        categories: GradesCategories,
        comments: GradesComments,
        users: LinkedHashMap<Int, User>,
        subjects: LinkedHashMap<Int, String>,
        colors: LinkedHashMap<Int, String>
    ): LinkedHashMap<String, List<Grade>> {
        return this.Grades.fold(LinkedHashMap()) { acc, gradeData ->
            val category = categories.getCategoryById(gradeData.Category.Id)
            if (category == null)
                acc

            val comment = comments.getCommentByGradeId(gradeData.Id)

            val gradeType = if (gradeData.IsSemester)
                GradeType.Semester
            else if (gradeData.IsSemesterProposition)
                GradeType.SemesterProposition
            else if (gradeData.IsFinal)
                GradeType.Final
            else if (gradeData.IsFinalProposition)
                GradeType.FinalProposition
            else
                GradeType.Constituent

            val teacher = users[gradeData.AddedBy.Id]?.firstName + " " + users[gradeData.AddedBy.Id]?.lastName

            val grade = Grade(
                gradeData.Id,
                gradeData.Grade,
                LocalDateTime.parse(gradeData.AddDate, localDateTimeFormat),
                colors[category!!.Color.Id]?: "000000",
                gradeType,
                category.Name,
                teacher,
                if (category.CountToTheAverage) category.Weight else 0,
                gradeData.Semester,
                comment
            )

            val subject = subjects[gradeData.Subject.Id]
            if (subject != null)
                acc[subject] = acc[subject]?.plus(grade)?: listOf(grade)

            acc
        }
    }
}