package com.github.aeoliux.violet.api.bodys

import com.github.aeoliux.violet.api.types.Me
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val Id: UInt,
    val UserId: UInt,
    val FirstName: String,
    val LastName: String,
    val Email: String,
    val Login: String
)
@Serializable
data class SubMe(val Account: Account)
@Serializable
data class Me(val Me: SubMe) {
    fun toMeData(): Me {
        return Me(
            id = Me.Account.Id,
            firstName = Me.Account.FirstName,
            lastName = Me.Account.LastName,
            email = Me.Account.Email,
            login = Me.Account.Login
        )
    }
}