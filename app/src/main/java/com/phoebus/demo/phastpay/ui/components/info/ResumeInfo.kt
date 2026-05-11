package com.phoebus.demo.phastpay.ui.components.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ResumeInfo(
    val title: String,
    val description: String,
    val showLine: Boolean = true,
    val modifier: Modifier = Modifier
)

@Composable
fun ResumeInfoList(
    data: List<ResumeInfo>,
    title: String? = null,
    modifier: Modifier = Modifier.padding(16.dp)
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (title != null) {
            Text(
                text = title.uppercase(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp
                ),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSurface
            )

        }
        val padding = if (title != null) 5.dp else 0.dp
        data.forEach { item ->
            ResumeInfoItem(
                item,
                Modifier.padding(horizontal = padding),
                modifierText = item.modifier
            )
            if (item.showLine) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                )
            }
        }
    }
}

@Composable
fun ResumeInfoItem(
    item: ResumeInfo,
    modifier: Modifier = Modifier,
    modifierText: Modifier = Modifier
) {
    var fontSize by remember { mutableStateOf(12.sp) }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${item.title}:",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(end = 2.dp),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
        Text(
            text = item.description,
            modifier = modifierText.weight(1f),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Normal,
            fontSize = fontSize,
            lineHeight = 16.sp,
            softWrap = false,
            onTextLayout = { textLayoutResult ->
                // Se o texto transbordou (hasVisualOverflow), diminui a fonte
                if (textLayoutResult.hasVisualOverflow && fontSize > 8.sp) {
                    fontSize *= 0.9f
                }
            }
        )
    }
}