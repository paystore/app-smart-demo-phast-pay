package com.phoebus.demo.phastpay.data.dto

import com.phoebus.demo.phastpay.data.enums.Service
import com.phoebus.demo.phastpay.data.models.DisplayMode
import com.phoebus.demo.phastpay.data.models.TipInformation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhastPayGetAvailableServicesResponse (
    val services: List<Service>,
    @SerialName("service_provider_resources")
    val serviceProviderResources: List<ResourceServiceProvider>? = emptyList()
)

@Serializable
data class ServiceDetail(
    val service: Service? = null,
    @SerialName("display_mode")
    val displayMode: DisplayMode? = DisplayMode.BUTTON,
    val cobExpiration: Long?,
)

@Serializable
data class ResourceServiceProvider(
    @SerialName("provider_name")
    val providerName: String? = null,
    @SerialName("provider_id")
    val providerId: String? = null,
    @SerialName("tip_information")
    val tipInformation: TipInformation? = null,
    val services: List<ServiceDetail>? = emptyList()
)