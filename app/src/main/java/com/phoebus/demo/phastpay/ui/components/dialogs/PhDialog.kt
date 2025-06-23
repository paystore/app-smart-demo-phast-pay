package com.phoebus.demo.phastpay.ui.components.dialogs

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PhDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    title: String = "",
    message: String = ""
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = title) },
        text = {
            SelectionContainer {
                Text(text = message)
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text("OK")
            }
        }
    )

}