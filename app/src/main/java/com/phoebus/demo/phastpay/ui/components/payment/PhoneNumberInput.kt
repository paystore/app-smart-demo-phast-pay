package com.phoebus.demo.phastpay.ui.components.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.phoebus.demo.phastpay.utils.Country
import com.phoebus.demo.phastpay.utils.PhoneNumberConstants
import com.phoebus.demo.phastpay.utils.PhoneNumberConstants.countries
import com.phoebus.demo.phastpay.utils.PhoneNumberTransformation
import java.util.Locale

@Composable
fun PhoneNumberInput(
    country: Country,
    updateCountry: (Country) -> Unit,
    onDdiChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DdiDropdown(
            selectedCountry = country,
            onCountrySelected = {
                updateCountry(it)
                onDdiChange(getCountryCallingCode(it.code))
            },
            modifier = Modifier
                .weight(1f)
                .height(TextFieldDefaults.MinHeight)
        )

        OutlinedTextField(
            modifier = Modifier
                .weight(3f)
                .height(TextFieldDefaults.MinHeight),
            value = phone,
            onValueChange = { newValue ->
                if (newValue.length <= PhoneNumberConstants.getExpectedPhoneNumberLength(country.code)) {
                    onPhoneChange(newValue.filter { it.isDigit() })
                }
            },
            visualTransformation = PhoneNumberTransformation(country.code),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            placeholder = {
                Text(getPhoneNumberPlaceholder(country.code))
            }
        )
    }
}

fun getCountryCallingCode(countryCode: String): String {
    val phoneUtil = PhoneNumberUtil.getInstance()
    return "+${phoneUtil.getCountryCodeForRegion(countryCode)}"
}

@Preview(showBackground = true, name = "PhoneNumberInput Preview")
@Composable
fun PhoneNumberInputPreview() {
    var selectedCountry by remember { mutableStateOf(countries.first()) }
    var phoneNumber by remember { mutableStateOf("123456789") }
    var ddi by remember { mutableStateOf("+55") }

    PhoneNumberInput(
        country = selectedCountry,
        updateCountry = { selectedCountry = it },
        onDdiChange = { ddi = it },
        phone = phoneNumber,
        onPhoneChange = { phoneNumber = it },
        modifier = Modifier
    )
}

fun getPhoneNumberPlaceholder(countryCode: String): String {
    val phoneUtil = PhoneNumberUtil.getInstance()
    return try {
        val exampleNumber = phoneUtil.getExampleNumber(countryCode.uppercase(Locale.ROOT))
        phoneUtil.format(exampleNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL)
    } catch (e: Exception) {
        "123 456 789" 
    }
}

