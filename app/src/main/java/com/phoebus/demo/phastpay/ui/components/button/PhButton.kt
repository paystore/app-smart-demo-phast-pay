package com.phoebus.demo.phastpay.ui.components.button

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun PhButton(
    title: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.elevatedButtonColors(),
    onClick: () -> Unit
) {
    ElevatedButton(
        modifier = modifier,
        onClick = {
            onClick()
        },
        colors = colors,
        enabled = enabled
    ) {
        Text(text = title)
    }
}