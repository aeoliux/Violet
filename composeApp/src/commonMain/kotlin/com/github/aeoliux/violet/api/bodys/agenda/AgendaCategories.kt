package com.github.aeoliux.violet.api.bodys.agenda

import com.github.aeoliux.violet.api.bodys.IdAndUrl
import kotlinx.serialization.Serializable

@Serializable
data class HomeworkCategory(
    val Id: UInt,
    val Name: String,
    val Color: IdAndUrl
)

@Serializable
data class HomeworkCategories(val Categories: List<HomeworkCategory>) {
    fun toMap(colors: LinkedHashMap<UInt, String>): LinkedHashMap<UInt, Pair<String, String>> {
        return Categories.fold(LinkedHashMap()) { acc, category ->
            acc[category.Id] = Pair(category.Name, colors[category.Color.Id]?: "FFFFFF")
            acc
        }
    }
}