package com.github.aeoliux.violet.storage

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.github.aeoliux.violet.api.types.GradeType
import comgithubaeoliuxvioletstorage.AppDatabaseQueries
import comgithubaeoliuxvioletstorage.ClassInfo
import comgithubaeoliuxvioletstorage.Grades

object Database {
    private lateinit var db: AppDatabase
    lateinit var dbQuery: AppDatabaseQueries

    fun open(sqlDriver: SqlDriver) {
        db = AppDatabase(
            sqlDriver,
            ClassInfo.Adapter(
                classTutorsAdapter = listOfStringsAdapter
            ),
            Grades.Adapter(
                gradeTypeAdapter = gradeTypeAdapter
            )
        )

        dbQuery = db.appDatabaseQueries
    }

    private val gradeTypeAdapter = object : ColumnAdapter<GradeType, Long> {
        val values = listOf(
            GradeType.Constituent,
            GradeType.Semester,
            GradeType.SemesterProposition,
            GradeType.Final,
            GradeType.FinalProposition
        )

        override fun decode(databaseValue: Long): GradeType =
            values[databaseValue.toInt()]

        override fun encode(value: GradeType): Long =
            values.indexOfFirst { it == value }.toLong()
    }

    private val listOfStringsAdapter = object : ColumnAdapter<List<String>, String> {
        override fun decode(databaseValue: String) =
            if (databaseValue.isEmpty()) {
                listOf()
            } else {
                databaseValue.split(",")
            }
        override fun encode(value: List<String>) = value.joinToString(separator = ",")
    }
}