package com.github.aeoliux.violet.app.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.violet.AppContext
import com.github.aeoliux.violet.api.types.ClassInfo
import com.github.aeoliux.violet.api.types.Me
import com.github.aeoliux.violet.app.components.LoadingIndicator
import com.github.aeoliux.violet.storage.Database
import com.github.aeoliux.violet.storage.selectAboutMe
import com.github.aeoliux.violet.storage.selectClassInfo
import com.github.aeoliux.violet.storage.selectLuckyNumber
import kotlinx.datetime.LocalDate

@Composable
fun HomeView() {
    var me by remember { mutableStateOf<Me?>(null) }
    var classInfo by remember { mutableStateOf<ClassInfo?>(null) }
    var isLoaded by remember { mutableStateOf(false) }
    var luckyNumber by remember { mutableStateOf<Pair<UInt, LocalDate>?>(null) }

    LaunchedEffect(AppContext.databaseUpdated.value) {
        me = Database.selectAboutMe()
        classInfo = Database.selectClassInfo()
        luckyNumber = Database.selectLuckyNumber()
        AppContext.semester.value = classInfo?.semester ?: 1u

        isLoaded = true
    }

    if (isLoaded) {
        Card(Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            Column(
                Modifier.fillMaxWidth().wrapContentHeight().padding(10.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(text = "Hi, ${me?.firstName}", fontSize = 40.sp, fontWeight = FontWeight.Bold)
                Text(text = "Lucky number is/will be ${luckyNumber?.first}", fontSize = 20.sp)
            }
        }
    } else {
        LoadingIndicator()
    }
}