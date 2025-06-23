package com.phoebus.demo.phastpay.ui.components.payment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.features.startPayment.PaymentEvent
import com.phoebus.demo.phastpay.ui.features.startPayment.validateInput
import com.phoebus.demo.phastpay.utils.CurrencyAmountInputVisualTransformation
import com.phoebus.demo.phastpay.utils.CurrencyType

@Composable
fun InputValue(
    checked: Boolean,
    value: String,
    currencyType: CurrencyType = CurrencyType.EUR,
    showCurrency: Boolean = true,
    onCurrencyChange: (CurrencyType) -> Unit = {},
    onChangeValue: (value: String) -> Unit,
) {
    val format = getCurrencyFormat(currencyType)
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column {
        var expanded by remember { mutableStateOf(false) }
        if (showCurrency) {
            Box {
                OutlinedButton(onClick = { expanded = true }) {
                    Text(text = currencyType.name)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    CurrencyType.values().forEach { type ->
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


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .focusRequester(focusRequester),
            value = value,
            onValueChange = {
                if (it.length <= 9 && validateInput(it)) {
                    if (!it.startsWith("0")) {
                        onChangeValue(it)
                    }
                }
            },
            label = { Text(stringResource(R.string.value) + ":") },
            singleLine = true,
            enabled = checked,
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            visualTransformation = CurrencyAmountInputVisualTransformation(
                fixedCursorAtTheEnd = true
            ),
            prefix = if (format.isPrefix && showCurrency) {
                { Text(format.symbol) }
            } else null,
            suffix = if (!format.isPrefix && showCurrency) {
                { Text(format.symbol) }
            } else null
        )
    }
}

data class CurrencyFormat(val symbol: String, val isPrefix: Boolean)

fun getCurrencyFormat(type: CurrencyType): CurrencyFormat {
    return when (type) {
        CurrencyType.USD -> CurrencyFormat("$", true)
        CurrencyType.EUR -> CurrencyFormat("€", false)
        CurrencyType.BRL -> CurrencyFormat("R$", true)
        CurrencyType.GBP -> CurrencyFormat("£", true)
        CurrencyType.ARS -> CurrencyFormat("ARS$", false)
        CurrencyType.CNY -> CurrencyFormat("¥", true)
        CurrencyType.RUB -> CurrencyFormat("₽", false)
        CurrencyType.CHF -> CurrencyFormat("CHF", false)
        else -> CurrencyFormat(type.name, false)
    }
}
