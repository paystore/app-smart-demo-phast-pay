package com.phoebus.demo.phastpay.ui.features.isPhastPayInstalled

data class IsPhastPayInstalledState(
    val loading:Boolean = true,
    val isAppInstalled: Boolean = false,
    val errorMessage: String? = null
)
