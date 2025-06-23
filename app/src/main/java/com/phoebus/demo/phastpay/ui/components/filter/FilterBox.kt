package com.phoebus.demo.phastpay.ui.components.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.phoebus.demo.phastpay.utils.getScreenWidth
import com.phoebus.demo.phastpay.utils.pxToSp
import com.phoebus.demo.phastpay.ui.theme.AppSmartDemoPhastPayTheme


data class FilterItem(
    val label: String,
    val value: Any,
)

@Composable
fun FilterBox(
    filterItems: List<FilterItem>,
    onSelectedItem: (FilterItem) -> Unit,
    selectedItem: FilterItem?,
    modifier: Modifier,
    label: String? = null,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        label?.let {
            Text(
                label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 15.dp)
            )
        }
        Row(
            modifier = modifier
                .shadow(elevation = 1.dp, shape = RoundedCornerShape(50.dp))
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.spacedBy((-5).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            filterItems.forEach { filterItem ->
                SelectionBoxItem(
                    item = filterItem,
                    selectedItem = selectedItem == filterItem,
                    onSelectedItem
                )
            }

        }

    }
}


@Composable
fun SelectionBoxItem(
    item: FilterItem,
    selectedItem: Boolean,
    onSelectedItem: (FilterItem) -> Unit,
) {
    SelectionBox(
        content = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .clip(RoundedCornerShape(50.dp)),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 2.dp, vertical = 5.dp)
                        .width(getScreenWidth() * 0.2f),
                    text = item.label,
                    fontSize = pxToSp(32f, LocalContext.current).sp,
                    textAlign = TextAlign.Center,
                    color = if (it) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        },
        onClickAction = { onSelectedItem(item) },
        selected = selectedItem,
        modifier = Modifier.zIndex(if (selectedItem) 1f else 0f)
    )
}


@Preview(showBackground = true)
@Composable
private fun SelectionBoxPreview() {
    AppSmartDemoPhastPayTheme {
        var selectedItem by remember { mutableStateOf<FilterItem?>(listItemsPreview.get(1)) }
        // Função que é chamada quando um item é selecionado
        val onSelectedItem: (FilterItem) -> Unit = { item ->
            selectedItem = item
        }

        FilterBox(
            listItemsPreview, onSelectedItem, selectedItem,
            Modifier
        )
    };
}

private var listItemsPreview = listOf(
    FilterItem(label = "Item 1", value = "value1"),
    FilterItem(label = "Item 2", value = "value2"),
);