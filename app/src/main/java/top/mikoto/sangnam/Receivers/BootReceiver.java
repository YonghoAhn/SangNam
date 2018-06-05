package top.mikoto.sangnam.Receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import top.mikoto.sangnam.Models.AlarmModel;
import top.mikoto.sangnam.Utils.AlarmManager;
import top.mikoto.sangnam.Utils.DB.DBHelper;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //
        int _id = intent.getIntExtra("_id",-1);
        if(_id != -1)
        {
            DBHelper dbHelper = new DBHelper(context,"ALARM",null,1);
            AlarmModel alarmModel = dbHelper.getAlarmById(_id);
            AlarmManager alarmManager = AlarmManager.getInstance(context);

            String[] temp = alarmModel.getTime().split("\\|");

            Intent mAlarmIntent = new Intent(context, AlarmReceiver.class);

            mAlarmIntent.putExtra("_id", _id);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    _id,
                    mAlarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            Calendar calendar = Calendar.getInstance();

            alarmManager.addAlarm(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),calendar.get(Calendar.DAY_OF_WEEK),pendingIntent);
            dbHelper.close();
        }
    }
}
