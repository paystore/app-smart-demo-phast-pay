package com.phoebus.demo.phastpay.ui.features.report.getReports

sealed class GetReportsEffect {
    object Success : GetReportsEffect()
    data class Error(val message: String) : GetReportsEffect()
}
