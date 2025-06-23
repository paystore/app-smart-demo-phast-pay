package com.phoebus.demo.phastpay.ui.components.filter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.data.enums.FilterType
import com.phoebus.demo.phastpay.ui.features.startPayment.validateInput
import com.phoebus.demo.phastpay.utils.CurrencyAmountInputVisualTransformation

@Composable
fun FilterValue(filterType: FilterType, value: String = "", onChangeValue: (String) -> Unit) {
    AnimatedVisibility(
        visible = filterType == FilterType.GET_PAYMENTS,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(), // Slide de cima para baixo
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut() // Slide de baixo para cima
    ) {
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(R.string.filter_value_title).uppercase(),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
            Row {
                val isEmptyOrZero = value.isBlank() || value == "0.00"
                val fieldColor = if (isEmptyOrZero) {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) // visual de desabilitado
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp)
                        .focusRequester(focusRequester),
                    value = value,
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        },
                    ),
                    visualTransformation = CurrencyAmountInputVisualTransformation(
                        fixedCursorAtTheEnd = true
                    ),
                    onValueChange = {
                        if (it.length <= 9 && validateInput(it)) {
                            if (!it.startsWith("0")) {
                                onChangeValue(it)
                            }
                        } else {
                            value
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors().copy(
                        focusedTextColor = fieldColor,
                        unfocusedTextColor = fieldColor
                    ),
                    singleLine = true,
                    label = { Text(stringResource(R.string.filter_value_title)) }
                )
            }
        }
    }
}