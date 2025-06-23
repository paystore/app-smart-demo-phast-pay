package com.phoebus.demo.phastpay.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun SelectionBox(
    content: @Composable (Boolean) -> Unit = {},
    onClickAction: () -> Unit = {},
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(1.dp)
            .clip(RoundedCornerShape(30.dp))
            .clickable { onClickAction() }
            .background(
                if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surface
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        content(selected)
    }
}