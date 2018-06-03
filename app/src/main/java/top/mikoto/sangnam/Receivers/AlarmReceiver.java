package top.mikoto.sangnam.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import top.mikoto.sangnam.Activities.RingAlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        //Receive Alarm.
        Log.d("MisakaMOE","Alarm!");
        context.startActivity(new Intent(context, RingAlarmActivity.class));
    }
}
