package com.phoebus.demo.phastpay.ui.features.printReceipt

sealed interface PrintReceiptEvent {
    data object Initialize : PrintReceiptEvent
    data object OnSubmit : PrintReceiptEvent
    data class UpdatePrintMerchantReceipt(val print : Boolean ) : PrintReceiptEvent
    data class UpdatePrintCustomerReceipt(val print : Boolean ) : PrintReceiptEvent
    data class UpdatePreviewMerchantReceipt(val preview : Boolean ) : PrintReceiptEvent
    data class UpdatePreviewCustomerReceipt(val preview : Boolean ) : PrintReceiptEvent
    data class UpdateRefundId(val refundId : String ) : PrintReceiptEvent
    data class UpdatePaymentId(val paymentId : String ) : PrintReceiptEvent
    data class UpdateClientId(val clientId : String ) : PrintReceiptEvent
}

sealed interface PrintReceiptNavigationEvents {
    data object NavigateToHome :
        PrintReceiptNavigationEvents
}

sealed class PrintReceiptSideEffect {
    data class ShowToast(val message: String) : PrintReceiptSideEffect()
}