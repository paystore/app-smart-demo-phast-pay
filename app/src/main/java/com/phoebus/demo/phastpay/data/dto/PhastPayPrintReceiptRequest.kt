package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhastPayPrintReceiptRequest(
    @SerialName("app_client_id")
    val appClientId: String? = null,
    @SerialName("payment_id")
    val paymentId: String? = null,
    @SerialName("refund_id")
    val refundId: String? = null,
    @SerialName("application_id")
    val applicationId: String = "",
    @SerialName("application_name")
    val applicationName: String = "",
    @SerialName("print_customer_receipt")
    val printCustomerReceipt: Boolean = true,
    @SerialName("print_merchant_receipt")
    val printMerchantReceipt: Boolean = true,
    @SerialName("preview_customer_receipt")
    val previewCustomerReceipt: Boolean = true,
    @SerialName("preview_merchant_receipt")
    val previewMerchantReceipt: Boolean = true,
    @SerialName("last_transaction")
    val lastTransaction: Boolean = false,
)