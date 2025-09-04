package com.github.aeoliux.violet.app.appState

import com.github.aeoliux.violet.Keychain
import com.github.aeoliux.violet.api.ApiClient
import com.github.aeoliux.violet.api.scraping.messages.getMessages
import com.github.aeoliux.violet.app.storage.AboutUserRepository
import com.github.aeoliux.violet.app.storage.AgendaRepository
import com.github.aeoliux.violet.app.storage.AttendanceRepository
import com.github.aeoliux.violet.app.storage.GradesRepository
import com.github.aeoliux.violet.app.storage.LuckyNumberRepository
import com.github.aeoliux.violet.app.storage.MessageLabelsRepository
import com.github.aeoliux.violet.app.storage.MessagesRepository
import com.github.aeoliux.violet.app.storage.SchoolNoticesRepository
import com.github.aeoliux.violet.app.storage.TimetableRepository

object Model {
    suspend fun logIn(
        client: ApiClient,
        keychain: Keychain
    ) {
        val usernameAndPass = keychain.getPass()
            ?: return
        val indexOfSpace = usernameAndPass.indexOfFirst { it == ' ' }
        val login = usernameAndPass.slice(0..<indexOfSpace)
        val password = usernameAndPass.slice(indexOfSpace + 1..<usernameAndPass.length)

        client.proceedLogin(login, password)
    }

    suspend fun getData(
        client: ApiClient,
        aboutUserRepository: AboutUserRepository,
        agendaRepository: AgendaRepository,
        attendanceRepository: AttendanceRepository,
        gradesRepository: GradesRepository,
        luckyNumberRepository: LuckyNumberRepository,
        messageLabelsRepository: MessageLabelsRepository,
        schoolNoticesRepository: SchoolNoticesRepository,
        timetableRepository: TimetableRepository
    ) {
        client.me().let {
            aboutUserRepository.insertMe(it)
        }

        client.classInfo().let {
            aboutUserRepository.insertClassInfo(it)
        }

        client.agenda().let {
            agendaRepository.deleteAgenda()
            agendaRepository.insertAgenda(it)
        }

        client.attendance().let {
            attendanceRepository.deleteAttendance()
            attendanceRepository.insertAttendance(it)
        }

        client.grades().let {
            gradesRepository.deleteGrades()
            gradesRepository.insertGrades(it)
        }

        client.luckyNumber().let {
            luckyNumberRepository.insertLuckyNumber(it.first)
        }

        client.getMessages().let {
            messageLabelsRepository.deleteMessageLabels()
            messageLabelsRepository.insertMessageLabels(it)
        }

        client.schoolNotices().let {
            schoolNoticesRepository.deleteSchoolNotices()
            schoolNoticesRepository.insertSchoolNotices(it)
        }

        client.timetable().let {
            timetableRepository.deleteTimetable()
            timetableRepository.insertTimetable(it)
        }
    }

    suspend fun deleteData(
        keychain: Keychain,
        aboutUserRepository: AboutUserRepository,
        agendaRepository: AgendaRepository,
        attendanceRepository: AttendanceRepository,
        gradesRepository: GradesRepository,
        luckyNumberRepository: LuckyNumberRepository,
        messageLabelsRepository: MessageLabelsRepository,
        schoolNoticesRepository: SchoolNoticesRepository,
        timetableRepository: TimetableRepository,
        messagesRepository: MessagesRepository
    ) {
        aboutUserRepository.deleteMe()
        aboutUserRepository.deleteClassInfo()
        agendaRepository.deleteAgenda()
        attendanceRepository.deleteAttendance()
        gradesRepository.deleteGrades()
        luckyNumberRepository.deleteLuckyNumber()
        messageLabelsRepository.deleteMessageLabels()
        schoolNoticesRepository.deleteSchoolNotices()
        timetableRepository.deleteTimetable()
        messagesRepository.clearStorage()

        keychain.deletePass()
    }
}