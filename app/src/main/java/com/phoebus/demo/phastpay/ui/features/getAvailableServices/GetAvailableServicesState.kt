package com.phoebus.demo.phastpay.ui.features.getAvailableServices

import com.phoebus.demo.phastpay.data.dto.PhastPayGetAvailableServicesResponse

data class GetAvailableServicesState (
    val loading: Boolean = true,
    val services: PhastPayGetAvailableServicesResponse? = null
)