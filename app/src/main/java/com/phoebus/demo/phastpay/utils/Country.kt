package com.phoebus.demo.phastpay.utils

data class Country(
    val code: String,
    val name: String,
    val dialCode: String
)

object PhoneNumberConstants {
    const val DDI_55 = "+55"
    const val DDI_39 = "+39"
    const val DDI_34 = "+34"
    const val DDI_351 = "+351"
    const val DDI_49 = "+49"
    const val DDI_44 = "+44"
    const val DDI_33 = "+33"
    const val DDI_32 = "+32"

    const val COUNTRY_BR = "BR"
    const val COUNTRY_IT = "IT"
    const val COUNTRY_ES = "ES"
    const val COUNTRY_PT = "PT"
    const val COUNTRY_DE = "DE"
    const val COUNTRY_GB = "GB"
    const val COUNTRY_FR = "FR"
    const val COUNTRY_BE = "BE"

    val countries = listOf(
        Country(COUNTRY_BR, "Brasil", DDI_55),
        Country(COUNTRY_IT, "Itália", DDI_39),
        Country(COUNTRY_ES, "Espanha", DDI_34),
        Country(COUNTRY_PT, "Portugal", DDI_351),
        Country(COUNTRY_DE, "Alemanha", DDI_49),
        Country(COUNTRY_GB, "Reino Unido", DDI_44),
        Country(COUNTRY_FR, "França", DDI_33),
        Country(COUNTRY_BE, "Bélgica", DDI_32)
    )

    fun getExpectedPhoneNumberLength(code: String?): Int {
        return when (code) {
            COUNTRY_BR -> 11
            COUNTRY_IT -> 11
            COUNTRY_DE -> 11
            COUNTRY_GB -> 11
            COUNTRY_FR -> 10
            COUNTRY_BE -> 10
            else -> 9
        }
    }
}