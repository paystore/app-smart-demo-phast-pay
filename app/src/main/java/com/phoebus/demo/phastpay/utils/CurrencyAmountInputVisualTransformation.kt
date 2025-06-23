package com.phoebus.demo.phastpay.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.lang.Integer.max
import java.text.DecimalFormat

// Classe para transformação visual de entrada de texto para moeda
class CurrencyAmountInputVisualTransformation(
    private val fixedCursorAtTheEnd: Boolean = true, // Determina se o cursor deve permanecer fixo no final do texto
    private val numberOfDecimals: Int = 2 // Número de casas decimais para serem exibidas
) : VisualTransformation {

    // Obtemos os símbolos de formatação de números padrão
    private val symbols = DecimalFormat().decimalFormatSymbols

    // Método para aplicar a transformação visual
    override fun filter(text: AnnotatedString): TransformedText {
        // Obtemos os símbolos de separador de milhar e decimal
        val thousandsSeparator = symbols.groupingSeparator
        val decimalSeparator = symbols.decimalSeparator
        val zero = symbols.zeroDigit

        // Texto de entrada
        val inputText = text.text

        // Parte inteira do número formatada com separadores de milhar
        val intPart = inputText
            .dropLast(numberOfDecimals)
            .reversed()
            .chunked(3)
            .joinToString(thousandsSeparator.toString())
            .reversed()
            .ifEmpty {
                zero.toString()
            }

        // Parte decimal do número, preenchida com zeros se necessário
        val fractionPart = inputText.takeLast(numberOfDecimals).let {
            if (it.length != numberOfDecimals) {
                List(numberOfDecimals - it.length) {
                    zero
                }.joinToString("") + it
            } else {
                it
            }
        }

        // Número completo formatado
        val formattedNumber = intPart + decimalSeparator + fractionPart

        // Criamos uma nova AnnotatedString com o texto formatado
        val newText = AnnotatedString(
            text = formattedNumber,
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles
        )

        // Decide qual OffsetMapping usar com base na configuração
        val offsetMapping = if (fixedCursorAtTheEnd) {
            FixedCursorOffsetMapping(
                contentLength = inputText.length,
                formattedContentLength = formattedNumber.length
            )
        } else {
            MovableCursorOffsetMapping(
                unmaskedText = text.toString(),
                maskedText = newText.toString(),
                decimalDigits = numberOfDecimals
            )
        }

        return TransformedText(newText, offsetMapping)
    }

    // Classe para mapeamento de cursor quando ele deve permanecer fixo no final do texto formatado
    private class FixedCursorOffsetMapping(
        private val contentLength: Int,
        private val formattedContentLength: Int,
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = formattedContentLength
        override fun transformedToOriginal(offset: Int): Int = contentLength
    }

    // Classe para mapeamento de cursor quando ele pode ser movido durante a formatação
    private class MovableCursorOffsetMapping(
        private val unmaskedText: String,
        private val maskedText: String,
        private val decimalDigits: Int
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int =
            when {
                unmaskedText.length <= decimalDigits -> {
                    maskedText.length - (unmaskedText.length - offset)
                }
                else -> {
                    offset + offsetMaskCount(offset, maskedText)
                }
            }

        override fun transformedToOriginal(offset: Int): Int =
            when {
                unmaskedText.length <= decimalDigits -> {
                    max(unmaskedText.length - (maskedText.length - offset), 0)
                }
                else -> {
                    offset - maskedText.take(offset).count { !it.isDigit() }
                }
            }

        // Calcula a quantidade de caracteres de máscara antes de uma determinada posição do cursor
        private fun offsetMaskCount(offset: Int, maskedText: String): Int {
            var maskOffsetCount = 0
            var dataCount = 0
            for (maskChar in maskedText) {
                if (!maskChar.isDigit()) {
                    maskOffsetCount++
                } else if (++dataCount > offset) {
                    break
                }
            }
            return maskOffsetCount
        }
    }
}
