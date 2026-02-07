package ru.sicampus.bootcamp2026.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneVisualTransformation(val mask: String, val maskNumber: Char) : VisualTransformation {

    private val maxLength = mask.count { it == maskNumber }

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length > maxLength) text.text.substring(0, maxLength) else text.text

        val annotatedString = AnnotatedString.Builder().run {
            for (i in trimmed.indices) {
                append(trimmed[i])
            }
            toAnnotatedString()
        }

        val out = StringBuilder()
        var textIndex = 0
        mask.forEach { char ->
            if (textIndex < trimmed.length) {
                if (char == maskNumber) {
                    out.append(trimmed[textIndex++])
                } else {
                    out.append(char)
                }
            }
        }

        val phoneNumberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var noneDigitCount = 0
                var i = 0
                while (i < offset + noneDigitCount) {
                    if (i < mask.length && mask[i] != maskNumber) noneDigitCount++
                    i++
                }
                return offset + noneDigitCount
            }

            override fun transformedToOriginal(offset: Int): Int {
                var noneDigitCount = 0
                var i = 0
                while (i < offset) {
                    if (i < out.length && mask[i] != maskNumber) noneDigitCount++
                    i++
                }
                return offset - noneDigitCount
            }
        }

        return TransformedText(AnnotatedString(out.toString()), phoneNumberOffsetTranslator)
    }
}