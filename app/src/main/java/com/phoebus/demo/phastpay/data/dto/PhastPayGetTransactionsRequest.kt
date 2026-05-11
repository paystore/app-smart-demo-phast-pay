package com.phoebus.demo.phastpay.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhastPayGetTransactionsRequest (
    @SerialName("print_customer_receipt")
    val printCustomerReceipt: Boolean = true,

    @SerialName("print_merchant_receipt")
    val printMerchantReceipt: Boolean = true,

    @SerialName("preview_customer_receipt")
    val previewCustomerReceipt: Boolean = true,

    @SerialName("preview_merchant_receipt")
    val previewMerchantReceipt: Boolean = true,

    @SerialName("start_date")
    val startDate: String = "",

    @SerialName("end_date")
    val endDate: String = "",
){
}