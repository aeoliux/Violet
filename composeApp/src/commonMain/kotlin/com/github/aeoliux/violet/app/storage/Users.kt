package com.github.aeoliux.violet.app.storage

//import com.github.aeoliux.violet.api.User
//
//fun Database.selectUser(id: UInt): User? {
//    try {
//        val user = dbQuery.selectUserById(id.toLong()).executeAsOne()
//
//        return User(
//            firstName = user.firstName,
//            lastName = user.lastName,
//            isSchoolAdministrator = user.isSchoolAdministrator,
//            isEmployee = user.isEmployee
//        )
//    } catch (_: NullPointerException) {
//        return null
//    }
//}
//
//fun Database.insertUsers(users: LinkedHashMap<UInt, User>) {
//    users.forEach {
//        dbQuery.transaction {
//            dbQuery.clearUsers()
//            dbQuery.insertUser(
//                id = it.key.toLong(),
//                firstName = it.value.firstName,
//                lastName = it.value.lastName,
//                isSchoolAdministrator = it.value.isSchoolAdministrator,
//                isEmployee = it.value.isEmployee
//            )
//        }
//    }
//}