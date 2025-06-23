package com.phoebus.demo.phastpay.ui.components.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClockButton(
    onClickAction: () -> Unit,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = { onClickAction() },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .width(120.dp)
                .height(30.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = text,
                maxLines = 1,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}