package com.phoebus.demo.phastpay.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.Locale

class PhoneNumberTransformation(
    private val countryCode: String
) : VisualTransformation {

    private val formatter by lazy {
        PhoneNumberUtil.getInstance().getAsYouTypeFormatter(
            countryCode.uppercase(Locale.ROOT)
        )
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text
        val formatted = formatNumber(original)

        return TransformedText(
            AnnotatedString(formatted),
            PhoneNumberOffsetMapping(original, formatted)
        )
    }

    private fun formatNumber(input: String): String {
        formatter.clear()
        var formatted = ""
        input.forEach { char ->
            if (char.isDigit()) {
                formatted = formatter.inputDigit(char)
            }
        }
        return formatted
    }

    private inner class PhoneNumberOffsetMapping(
        private val original: String,
        private val formatted: String
    ) : OffsetMapping {

        private val separatorIndices = formatted.mapIndexedNotNull { index, c ->
            index.takeIf { !c.isDigit() }
        }

        override fun originalToTransformed(offset: Int): Int {
            val digitsBefore = original.take(offset).count { it.isDigit() }

            var newOffset = 0
            var digitsCount = 0
            while (digitsCount < digitsBefore && newOffset < formatted.length) {
                if (formatted[newOffset].isDigit()) digitsCount++
                newOffset++
            }
            return newOffset
        }

        override fun transformedToOriginal(offset: Int): Int {
            val adjustedOffset = offset.coerceAtMost(formatted.length)
            return formatted.take(adjustedOffset).count { it.isDigit() }
        }
    }
}
