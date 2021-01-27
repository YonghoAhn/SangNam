package top.mikoto.sangnam.utils

import android.content.Context

class Vibrator private constructor(context: Context) {
    private val vibrator: android.os.Vibrator
    fun vibrate(millisecond: Int) {
        vibrator.vibrate(millisecond.toLong())
    }

    companion object {
        private var instance: Vibrator? = null
        fun getInstance(context: Context): Vibrator? {
            return if (instance != null) {
                instance
            } else {
                Vibrator(context.applicationContext).also { instance = it }
            }
        }
    }

    init {
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
    }
}