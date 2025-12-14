package com.github.aeoliux.api.bodys

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val Id: Int,
    val AccountId: String,
    val FirstName: String = "",
    val LastName: String = "",
    val IsSchoolAdministrator: Boolean = false,
    val IsEmployee: Boolean,
    val GroupId: Int
)

@Serializable
data class Users(val Users: List<User>) {
    fun toUserMap(): LinkedHashMap<Int, com.github.aeoliux.api.types.User> {
        return this.Users.fold(LinkedHashMap()) { acc, user ->
            acc[user.Id] = com.github.aeoliux.api.types.User(
                id = user.Id,
                firstName = user.FirstName,
                lastName = user.LastName,
                isSchoolAdministrator = user.IsSchoolAdministrator,
                isEmployee = user.IsEmployee
            )

            acc
        }
    }
}