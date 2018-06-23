package top.mikoto.sangnam.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

import top.mikoto.sangnam.Activities.RingAlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        //Receive Alarm.
        if((intent!=null) && (Objects.requireNonNull(intent.getAction()).equals("top.mikoto.sangnam.ALARM"))) {
            Log.d("MisakaMOE", "Alarm!");
            Intent intent1 = new Intent(context, RingAlarmActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}
