package com.phoebus.demo.phastpay.data.models

import kotlinx.serialization.Serializable

@Serializable
data class TipInformation(
    var percentages: List<Integer>? = emptyList(),
    var amounts: List<Integer>? = emptyList()
)

