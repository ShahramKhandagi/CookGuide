package ir.shahramkhandagi.cookguide.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson


class SharedPreferencesManager(context: Context) {

    companion object {
        private const val PREF_NAME = "my_preferences"
        private const val KEY_IS_PREMIUM = "is_premium_user"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setPremiumUser(isPremium: Boolean) {
        prefs.edit().putBoolean(KEY_IS_PREMIUM, isPremium).apply()
    }

    fun isPremiumUser(): Boolean {
        return prefs.getBoolean(KEY_IS_PREMIUM, false)
    }
}