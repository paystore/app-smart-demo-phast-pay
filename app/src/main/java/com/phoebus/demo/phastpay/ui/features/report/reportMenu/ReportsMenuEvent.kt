package com.phoebus.demo.phastpay.ui.features.report.reportMenu

sealed interface ReportsMenuEvent {
    data object FilterReports : ReportsMenuEvent
    data object ClearMessages : ReportsMenuEvent
}
