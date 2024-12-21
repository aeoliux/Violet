package com.github.aeoliux.violet.app.storage

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import com.github.aeoliux.violet.api.bodys.Class
import com.github.aeoliux.violet.api.types.Me as MeFinal
import com.github.aeoliux.violet.api.types.ClassInfo as ClassInfoFinal

@Entity(tableName = "Me")
data class Me(
    @PrimaryKey val key: Int = 0,

    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val login: String,
)

@Entity(tableName = "ClassInfo")
data class ClassInfo(
    @PrimaryKey val key: Int = 0,

    val number: Int,
    val symbol: String,
    val classTutors: List<String>,
    val semester: Int
)

@Dao
interface AboutUserDao {
    @Upsert
    suspend fun insertMe(me: Me)

    @Query("DELETE FROM Me")
    suspend fun deleteMe()

    @Query("SELECT * FROM Me LIMIT 1")
    suspend fun getMe(): List<Me>

    @Upsert
    suspend fun insertClassInfo(classInfo: ClassInfo)

    @Query("DELETE FROM ClassInfo")
    suspend fun deleteClassInfo()

    @Query("SELECT * FROM ClassInfo LIMIT 1")
    suspend fun getClassInfo(): List<ClassInfo>
}

class AboutUserRepository(private val database: AppDatabase) {
    suspend fun deleteMe() = database.getAboutUserDao().deleteMe()
    suspend fun insertMe(me: MeFinal) = database.getAboutUserDao().insertMe(Me(
        id = me.id,
        firstName = me.firstName,
        lastName = me.lastName,
        email = me.email,
        login = me.login
    ))
    suspend fun getMe(): MeFinal? = database.getAboutUserDao().getMe().fold(emptyList<MeFinal>()) { acc, me ->
        acc.plus(MeFinal(
            id = me.id,
            firstName = me.firstName,
            lastName = me.lastName,
            email = me.email,
            login = me.login
        ))
    }.firstOrNull()

    suspend fun deleteClassInfo() = database.getAboutUserDao().deleteMe()
    suspend fun insertClassInfo(classInfo: ClassInfoFinal) = database.getAboutUserDao().insertClassInfo(ClassInfo(
        number = classInfo.number,
        symbol = classInfo.symbol,
        classTutors = classInfo.classTutors,
        semester = classInfo.semester
    ))

    suspend fun getClassInfo(): ClassInfo? = database.getAboutUserDao().getClassInfo().firstOrNull()
}