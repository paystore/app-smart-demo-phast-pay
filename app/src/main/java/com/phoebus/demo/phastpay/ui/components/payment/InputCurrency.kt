package com.phoebus.demo.phastpay.ui.components.payment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.phoebus.demo.phastpay.utils.CurrencyType

@Composable
fun InputCurrency(
    currencyType: CurrencyType,
    onCurrencyChange : (CurrencyType) -> Unit = {}
) {
    Column {
        var expanded by remember { mutableStateOf(false) }
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(text = currencyType.name)
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                CurrencyType.entries.forEach { type ->
                    DropdownMenuItem(
                        onClick = {
                            onCurrencyChange(type)
                            expanded = false
                        },
                        text = { Text(type.name) }
                    )
                }
            }
        }
    }

}