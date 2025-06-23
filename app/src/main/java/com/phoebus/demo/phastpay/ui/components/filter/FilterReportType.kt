package com.phoebus.demo.phastpay.ui.components.filter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.data.enums.FilterType
import com.phoebus.demo.phastpay.data.enums.ReportType
import com.phoebus.demo.phastpay.utils.getScreenWidth
import com.phoebus.demo.phastpay.utils.pxToDp
import com.phoebus.demo.phastpay.utils.pxToSp
import com.phoebus.demo.phastpay.ui.theme.AppSmartDemoPhastPayTheme


@Composable
fun FilterReportType(
    filterType: FilterType,
    reportSelected: ReportType,
    onReportSelected: (ReportType) -> Unit,
) {
    val context = LocalContext.current
    val containerWidth = getScreenWidth() * 0.6f
    val boxWidth = getScreenWidth() * 0.28f
    val fontSize = pxToSp(24f, context).sp

    AnimatedVisibility(
        visible = filterType == FilterType.REPORT,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(), // Slide de cima para baixo
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut() // Slide de baixo para cima
    ) {
        Row(
            modifier = Modifier
                .width(containerWidth)
                .height(pxToDp(72f, context).dp)
                .clip(RoundedCornerShape(50.dp)),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ReportSelectionBox(
                text = stringResource(R.string.report_summary),
                isSelected = reportSelected == ReportType.SUMMARY,
                onClick = { onReportSelected(ReportType.SUMMARY) },
                width = boxWidth,
                fontSize = fontSize
            )

            ReportSelectionBox(
                text = stringResource(R.string.report_detailed),
                isSelected = reportSelected == ReportType.DETAILED,
                onClick = { onReportSelected(ReportType.DETAILED) },
                width = boxWidth,
                fontSize = fontSize
            )
        }
    }
}

@Composable
private fun ReportSelectionBox(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    width: Dp,
    fontSize: TextUnit,
) {
    SelectionBox(
        content = {
            Column(
                modifier = Modifier
                    .width(width)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = text,
                    fontSize = fontSize,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                )
            }
        },
        onClickAction = onClick,
        selected = isSelected
    )
}


@Preview(showBackground = true)
@Composable
private fun FilterReportTypePreview() {
    AppSmartDemoPhastPayTheme{
        FilterReportType(
            FilterType.REPORT,
            ReportType.SUMMARY
        ) { }
    }
}
