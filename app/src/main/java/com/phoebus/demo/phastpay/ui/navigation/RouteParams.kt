package com.phoebus.demo.phastpay.ui.navigation

import android.net.Uri
import com.google.gson.Gson
import com.phoebus.demo.phastpay.data.enums.ReportType
import com.phoebus.demo.phastpay.data.enums.ServiceType
import com.phoebus.demo.phastpay.data.enums.TransactionStatus

sealed interface RouteParams {
    data class GetPayments(
        val startData: String,
        val endData: String,
        val status: List<TransactionStatus>,
        val value: String?,
    ) : RouteParams {
        fun toJson(): String {
            return Uri.encode(Gson().toJson(this))
        }
    }

    data class GetTransactions(
        val startData: String,
        val endData: String,
    ) : RouteParams {
        fun toJson(): String {
            return Uri.encode(Gson().toJson(this))
        }
    }

    data class GetPaymentsToRefund(
        val startData: String,
        val endData: String,
    ) : RouteParams {
        fun toJson(): String {
            return Uri.encode(Gson().toJson(this))
        }
    }

    data class GetReports(
        val startData: String,
        val endData: String,
        val reportType: ReportType,
        val service: ServiceType
    ) : RouteParams {
        fun toJson(): String {
            return Uri.encode(Gson().toJson(this))
        }
    }
}