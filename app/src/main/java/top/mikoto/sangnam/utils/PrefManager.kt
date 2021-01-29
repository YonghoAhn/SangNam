package top.mikoto.sangnam.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class PrefManager(_context: Context) {
    private val pref: SharedPreferences
    private val editor: Editor

    companion object {
        // Shared preferences file name
        private const val PREF_NAME = "sangnam"
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
        private const val LATEST_ID = "LatestID"
    }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, 0)
        editor = pref.edit()
    }
}