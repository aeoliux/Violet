package com.github.aeoliux.violet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.aeoliux.violet.app.content.MainContentView
import com.github.aeoliux.violet.app.layout.LazyLayout
import com.github.aeoliux.violet.app.layout.SectionHeader
import com.github.aeoliux.violet.app.layout.SectionListItemComposable
import com.github.aeoliux.violet.app.login.LoginView
import com.github.aeoliux.violet.repositories.AlertState
import com.github.aeoliux.violet.repositories.ClientManager
import org.koin.compose.koinInject

@Composable
fun App(colorScheme: ColorScheme) {
    val clientManager: ClientManager = koinInject()
    val alertState: AlertState = koinInject()

    val logState by clientManager.logStateFlow.collectAsState(false)
    val alertShown by alertState.shown.collectAsState()

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        typography = Typography()
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .fillMaxSize()
        ) {
            when (logState) {
                true -> MainContentView()
                false -> LoginView()
            }
        }

        if (alertShown)
            ModalBottomSheet(
                sheetState = rememberModalBottomSheetState(),
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onDismissRequest = { alertState.close() }
            ) {
                SelectionContainer(Modifier.padding(20.dp)) {
                    LazyLayout {
                        item {
                            SectionHeader("Crash log")
                        }

                        item {
                            SectionListItemComposable(
                                index = 0,
                                lastIndex = 1,
                                header = {
                                    Text(
                                        text = alertState.message,
                                    )
                                },
                            )
                        }

                        item {
                            SectionListItemComposable(
                                index = 1,
                                lastIndex = 1,
                                header = {
                                    Text(
                                        color = MaterialTheme.colorScheme.primary,
                                        text = "Close"
                                    )
                                },
                                onClick = { alertState.close() }
                            )
                        }
                    }
                }
            }
    }
}