package com.phoebus.demo.phastpay.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun getScreenHeight(): Dp {//altura da tela
    val configuration = LocalConfiguration.current
    return configuration.screenHeightDp.dp
}

@Composable
fun getScreenWidth(): Dp {//largura da tela
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp.dp
}

fun pxToDp(px: Float, context: Context): Float {
    val density = context.resources.displayMetrics.density
    return px / density
}

fun pxToSp(px: Float, context: Context): Float {
    val scaledDensity = context.resources.displayMetrics.scaledDensity
    return px / scaledDensity
}