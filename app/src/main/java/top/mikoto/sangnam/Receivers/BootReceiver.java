package top.mikoto.sangnam.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import top.mikoto.sangnam.Models.AlarmModel;
import top.mikoto.sangnam.Utils.AlarmManager;
import top.mikoto.sangnam.Utils.DB.DBHelper;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //
        DBHelper dbHelper = new DBHelper(context,"ALARM",null,1);
        ArrayList<AlarmModel> alarmModels = dbHelper.getAllAlarms();
        AlarmManager alarmManager = new AlarmManager(context);
        for(AlarmModel alarmModel : alarmModels)
        {
            alarmManager.registerAlarm(alarmModel);
        }
        dbHelper.close();
    }
}
