package com.github.aeoliux.violet.api.bodys

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val Id: UInt,
    val AccountId: String,
    val FirstName: String = "",
    val LastName: String = "",
    val IsSchoolAdministrator: Boolean = false,
    val IsEmployee: Boolean,
    val GroupId: UInt
)

@Serializable
data class Users(val Users: List<User>) {
    fun toUserMap(): LinkedHashMap<UInt, com.github.aeoliux.violet.api.User> {
        return this.Users.fold(LinkedHashMap()) { acc, user ->
            acc[user.Id] = com.github.aeoliux.violet.api.User(
                firstName = user.FirstName,
                lastName = user.LastName,
                isSchoolAdministrator = user.IsSchoolAdministrator,
                isEmployee = user.IsEmployee
            )

            acc
        }
    }
}