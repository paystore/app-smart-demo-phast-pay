package com.phoebus.demo.phastpay.ui.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.phoebus.demo.phastpay.ui.components.qrcode.QRCodeWithLogo

@Composable
fun PhDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    title: String = "",
    message: String = "",
    qrCodeBase64: String? = null
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = title) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SelectionContainer {
                    Text(text = message)
                }

                if (qrCodeBase64 != null) {
                    QRCodeWithLogo(
                        logoBase64 = qrCodeBase64,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text("OK")
            }
        }
    )

}