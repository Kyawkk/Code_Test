package com.kyawzinlinn.codetest.utils

import java.util.Locale

object ValidationUtil {
    fun validateEmail(email: String): Boolean{
        val regex = Regex("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")
        return regex.matches(email)
    }

    fun getAllValidCountryCodes(): List<String>{
        val countries = Locale.getISOCountries()
        val countryCodes = mutableListOf<String>()

        for (country in countries){
            val locale = Locale("",country)
            println(locale.country)
            countryCodes.add(locale.country)
        }
        return countryCodes
    }
}

fun main() {
    ValidationUtil.getAllValidCountryCodes()
}