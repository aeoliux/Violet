package com.github.aeoliux.violet.app.appState

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

var taskToBePerformed: suspend () -> Unit = {}

class BackgroundJob: Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.Default).launch {
            taskToBePerformed()
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

actual var runBackgroundTask: (task: suspend () -> Unit) -> Unit = {}