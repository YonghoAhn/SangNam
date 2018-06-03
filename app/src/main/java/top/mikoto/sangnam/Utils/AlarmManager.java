package top.mikoto.sangnam.Utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import top.mikoto.sangnam.Models.AlarmModel;
import top.mikoto.sangnam.Receivers.AlarmReceiver;
import top.mikoto.sangnam.Utils.DB.DBHelper;

public class AlarmManager {

    private static AlarmManager instance = null;
    private Context context;

    public static AlarmManager getInstance(Context context) {
        if(instance!=null)
            return instance;
        else
            return instance = new AlarmManager(context.getApplicationContext());
    }

    public AlarmManager(Context context)
    {
        this.context = context;
    }


   //
   //
   //
   //

    /**
     * @param hour HOUR_OF_DAY, 24h format
     * @param minute MINUTE
     * @param dayOfWeek SPECIFIC DAY : ex) Calendar.Monday
     * @param _id PENDING INTENT id
     */
    private void addAlarm(int hour, int minute, int dayOfWeek, int _id)
    {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Log.d("MisakaMOE",String.valueOf(alarmManager!=null));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);

        Intent mAlarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                _id,
                mAlarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );


        if(calendar.getTimeInMillis() < System.currentTimeMillis())
        {
            calendar.add(Calendar.DAY_OF_YEAR,new GregorianCalendar().get(Calendar.DAY_OF_WEEK)-1);
        }
        alarmManager.set(android.app.AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        //alarmManager.setRepeating(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), android.app.AlarmManager.INTERVAL_DAY * 7, pendingIntent);

    }

    public void addAlarm(int hour, int minute, String days_of_week)
    {
        //Check Alarm
        //parse alarm
        boolean[] days = new boolean[7];
        int tmp = 0;

        for(char s : days_of_week.toCharArray())
        {
            days[tmp] = s != '0';
            tmp++;
        }

        AlarmModel alarmModel = new AlarmModel();
        alarmModel.setDays(days_of_week);
        alarmModel.setTime("" + hour + "|" + minute);
        alarmModel.setRun(1);

        DBHelper dbHelper = new DBHelper(context,"ALARM",null,1);
        int _id = dbHelper.addAlarm(alarmModel);
        Log.d("MisakaMOE",String.valueOf(_id));

        for(int i = 0; i< 7; i++) //0 : sunday
        {
            if(days[i])
            {
                addAlarm(hour,minute,i+1, _id); //1 = sunday
            }
        }

        dbHelper.close();
    }

}
