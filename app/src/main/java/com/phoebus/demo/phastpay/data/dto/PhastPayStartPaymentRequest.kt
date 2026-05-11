package com.phoebus.demo.phastpay.data.dto

import com.phoebus.demo.phastpay.data.enums.AdditionalType
import com.phoebus.demo.phastpay.data.enums.Service
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PhastPayStartPaymentRequest(
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

    @SerialName("print_customer_receipt")
    val printCustomerReceipt: Boolean? = null,

    @SerialName("print_merchant_receipt")
    val printMerchantReceipt: Boolean? = null,

    @SerialName("preview_customer_receipt")
    val previewCustomerReceipt: Boolean? = null,

    @SerialName("preview_merchant_receipt")
    val previewMerchantReceipt: Boolean? = null,

    @SerialName("phone_number")
    val phoneNumber: String? = null,

    @SerialName("county_code")
    val countyCode: String? = null,

    @SerialName("customer_name")
    val customerName: String? = null,

    @SerialName("customer_email")
    val customerEmail: String? = null
) {
}
