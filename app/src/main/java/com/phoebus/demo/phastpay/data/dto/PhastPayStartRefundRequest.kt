package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PhastPayStartRefundRequest(
    @SerialName("application_id")
    val applicationId: String? = null,

    @SerialName("application_name")
    val applicationName: String? = null,

    @SerialName("payment_id")
    val paymentId: String? = null,

    @SerialName("value")
    val value: String? = null,

    @SerialName("print_customer_receipt")
    val printCustomerReceipt: Boolean? = null,

    @SerialName("print_merchant_receipt")
    val printMerchantReceipt: Boolean? = null,

    @SerialName("preview_customer_receipt")
    val previewCustomerReceipt: Boolean? = null,

    @SerialName("preview_merchant_receipt")
    val previewMerchantReceipt: Boolean? = null
) {
}
