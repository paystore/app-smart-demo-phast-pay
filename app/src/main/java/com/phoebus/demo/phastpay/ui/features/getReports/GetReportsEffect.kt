package com.phoebus.demo.phastpay.ui.features.getReports

sealed class GetReportsEffect {
    object Success : GetReportsEffect()
    data class Error(val message: String) : GetReportsEffect()
}
