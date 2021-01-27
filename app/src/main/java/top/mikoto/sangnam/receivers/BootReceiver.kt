package top.mikoto.sangnam.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import top.mikoto.sangnam.models.AlarmModel
import top.mikoto.sangnam.utils.AlarmManager
import top.mikoto.sangnam.utils.db.DBHelper
import java.util.*

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //
        val dbHelper = DBHelper(context, "ALARM", null, 1)
        val alarmModels: ArrayList<AlarmModel> = dbHelper.allAlarms
        val alarmManager = AlarmManager(context)
        for (alarmModel in alarmModels) {
            alarmManager.registerAlarm(alarmModel)
        }
        dbHelper.close()
    }
}