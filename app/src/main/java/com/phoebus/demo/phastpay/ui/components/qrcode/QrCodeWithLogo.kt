package com.phoebus.demo.phastpay.ui.components.qrcode

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp


@Composable
fun QRCodeWithLogo(
    logoBase64: String?,
    modifier: Modifier = Modifier
) {
    // Gerar o bitmap do QR Code puro
    val qrBitmap = remember(logoBase64) {
        base64ToBitmap(logoBase64)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // O QR Code de fundo
        qrBitmap.takeIf { it != null }?.let {
            Image(
                bitmap = it,
                contentDescription = "QR Code",
                modifier = Modifier.size(250.dp)
            )
        }

    }
}

fun base64ToBitmap(logoBase64: String?): ImageBitmap? {
    if (logoBase64.isNullOrBlank()) return null
    return try {
        val cleanBase64 = logoBase64.substringAfter(",")
        val imageBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()
    } catch (_: Exception) {
        null
    }
}