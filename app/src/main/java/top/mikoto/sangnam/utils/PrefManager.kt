package top.mikoto.sangnam.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class PrefManager(private val _context: Context) {
    private val pref: SharedPreferences
    private val editor: Editor

    // shared pref mode
    private val PRIVATE_MODE = 0
    var isFirstTimeLaunch: Boolean
        get() = pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)
        set(isFirstTime) {
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
            editor.commit()
        }

    companion object {
        // Shared preferences file name
        private const val PREF_NAME = "sangnam"
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
        private const val LATEST_ID = "LatestID"
    }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}