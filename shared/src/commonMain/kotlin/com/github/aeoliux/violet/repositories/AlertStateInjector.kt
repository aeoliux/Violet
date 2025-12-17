package com.github.aeoliux.violet.repositories

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AlertStateInjector: KoinComponent {
    val alertState: AlertState by inject()
}