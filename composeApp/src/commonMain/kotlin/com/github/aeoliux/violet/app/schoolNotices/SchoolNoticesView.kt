package com.github.aeoliux.violet.app.schoolNotices

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.aeoliux.violet.app.appState.LocalAppState
import com.github.aeoliux.violet.app.appState.formatDate
import com.github.aeoliux.violet.app.appState.formatDateTime
import com.github.aeoliux.violet.app.components.ExpandableList
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SchoolNoticesView(vm: SchoolNoticesViewModel = koinViewModel<SchoolNoticesViewModel>()) {
    val appState = LocalAppState.current
    val listOfSchoolNotices by vm.listOfSchoolNotices.collectAsState()

    LaunchedEffect(appState.databaseUpdated) {
        vm.launchedEffect()
    }

    listOfSchoolNotices.forEach {
        Card(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 2.dp)
        ) {
            ExpandableList(
                header = {
                    Column(Modifier.padding(10.dp)) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            text = it.subject
                        )
                        Text(
                            fontSize = 15.sp,
                            text = "By ${appState.safe("human (?)", it.addedBy)} at ${it.createdAt.formatDateTime()}"
                        )
                    }
                }
            ) {
                val schoolNotice = it

                Column(Modifier.padding(10.dp)) {
                    Text("Starts at: ${schoolNotice.startDate.formatDate()}")
                    Text("Ends at: ${schoolNotice.endDate.formatDate()}\n\n")

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Justify,
                        text = schoolNotice.content
                    )
                }
            }
        }
    }
}