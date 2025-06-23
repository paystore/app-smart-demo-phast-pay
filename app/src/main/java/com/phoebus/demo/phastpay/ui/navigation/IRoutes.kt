package com.phoebus.demo.phastpay.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

interface IRoutes {
    fun navigate(navController: NavController, builder: NavOptionsBuilder.() -> Unit = {})
}