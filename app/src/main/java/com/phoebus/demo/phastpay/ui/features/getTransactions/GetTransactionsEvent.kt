package com.phoebus.demo.phastpay.ui.features.getTransactions

import com.phoebus.demo.phastpay.data.dto.PhastPayGetTransactionsResponse
import com.phoebus.phastpay.sdk.client.PhastPayClient

sealed interface GetListTransactionsEvent {
    data class UpdateParams(
        val startDate: String,
        val endDate: String,
    ) : GetListTransactionsEvent
    data class UpdatePrintMerchantReceipt(val print: Boolean) : GetListTransactionsEvent
    data class UpdatePrintCustomerReceipt(val print: Boolean) : GetListTransactionsEvent
    data class StartGet(val phastPayClient: PhastPayClient) : GetListTransactionsEvent
    data class UpdateMessageError(val messageError: String?): GetListTransactionsEvent
    data class UpdateGetResult(val getTransactionsResult: PhastPayGetTransactionsResponse?): GetListTransactionsEvent

}

sealed interface GetTransactionsNavigationEvents {
    data object NavigateToHome :
        GetTransactionsNavigationEvents
}