package com.phoebus.demo.phastpay.ui.features.main

sealed interface MainEvent {
    data object CheckStartRoute: MainEvent
}