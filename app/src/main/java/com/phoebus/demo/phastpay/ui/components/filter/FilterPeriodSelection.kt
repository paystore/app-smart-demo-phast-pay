package com.phoebus.demo.phastpay.ui.components.filter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.data.enums.DatePeriod
import com.phoebus.demo.phastpay.utils.getScreenHeight
import com.phoebus.demo.phastpay.utils.getScreenWidth
import com.phoebus.demo.phastpay.utils.pxToSp


data class DatePeriodItem(
    @StringRes var titleBox: Int,
    @DrawableRes var icon: Int,
    var datePeriod: DatePeriod,
)

private var periodItems = listOf(
    DatePeriodItem(
        titleBox = R.string.filter_current_day,
        icon = R.drawable.ic_calendar_today,
        datePeriod = DatePeriod.TODAY
    ),
    DatePeriodItem(
        titleBox = R.string.filter_current_month,
        icon = R.drawable.ic_calendar_month,
        datePeriod = DatePeriod.THIS_MONTH
    ),
    DatePeriodItem(
        titleBox = R.string.filter_another_period,
        icon = R.drawable.ic_calendar_other_period,
        datePeriod = DatePeriod.OTHER_PERIOD
    )
)

@Composable
fun FilterPeriodSelection(
    periodSelected: DatePeriod,
    onDatePeriodSelected: (DatePeriod) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("txt_filter_period"),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = stringResource(R.string.current_period).uppercase(),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 5.dp)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(getScreenHeight() * 0.11f)
            .clip(RoundedCornerShape(50.dp))
            .testTag("btn_filter_period_list"),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        periodItems.forEach { periodItem ->
            FilterPeriodItem(
                title = periodItem.titleBox,
                icon = periodItem.icon,
                isPeriodSelected = periodSelected == periodItem.datePeriod,
                datePeriod = periodItem.datePeriod,
                onClick = { onDatePeriodSelected(periodItem.datePeriod) },
            )
        }
    }
}

@Composable
private fun FilterPeriodItem(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    datePeriod: DatePeriod,
    isPeriodSelected: Boolean,
    onClick: (DatePeriod) -> Unit
) {
    SelectionBox(
        content = {
            Column(
                modifier = Modifier
                    .width(getScreenWidth() * 0.30f)
                    .height(45.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .testTag("btn_filter_period_item_${datePeriod.name}"),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(25.dp)
                        .padding(top = 5.dp),
                    painter = painterResource(id = icon),
                    contentDescription = stringResource(title),
                    tint = if (it) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(title),
                    fontSize = pxToSp(24f, LocalContext.current).sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = if (it) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                )
            }
        },
        onClickAction = { onClick(datePeriod) },
        selected = isPeriodSelected
    )
}