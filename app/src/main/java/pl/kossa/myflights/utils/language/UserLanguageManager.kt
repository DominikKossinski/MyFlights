package pl.kossa.myflights.utils.language

import java.util.*

class UserLanguageManager {

    fun getCurrentLanguage(): String {
        return when (Locale.getDefault().language) {
            "pl" -> "pl_PL"
            "de" -> "de"
            else -> "en"
        }
    }
}