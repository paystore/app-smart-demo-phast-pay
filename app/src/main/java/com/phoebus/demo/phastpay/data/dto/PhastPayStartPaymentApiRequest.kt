package com.phoebus.demo.phastpay.data.dto

import com.phoebus.demo.phastpay.data.enums.AdditionalType
import com.phoebus.demo.phastpay.data.enums.Service
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PhastPayStartPaymentApiRequest(
    @SerialName("app_client_id")
    val appClientId: String? = null,

    @SerialName("application_id")
    val applicationId: String? = null,

    @SerialName("application_name")
    val applicationName: String? = null,

    @SerialName("value")
    val value: String? = null,

    @SerialName("additional_value")
    var additionalValue: String? = null,

    @SerialName("additional_type")
    var additionalType: AdditionalType? = null,

    @SerialName("service")
    val service: Service? = null,

    @SerialName("currency")
    val currency: String? = null,

    @SerialName("provider_id")
    val providerId: String? = null
) {
}
