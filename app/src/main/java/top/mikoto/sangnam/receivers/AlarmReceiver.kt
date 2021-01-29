package top.mikoto.sangnam.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import top.mikoto.sangnam.presentation.view.RingAlarmActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // an Intent broadcast.
        //Receive Alarm.
        if (Objects.requireNonNull(intent.action) == "top.mikoto.sangnam.ALARM") {
            Log.d("MisakaMOE", "Alarm!")
            val intent1 = Intent(context, RingAlarmActivity::class.java)
            intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent1)
        }
    }
}