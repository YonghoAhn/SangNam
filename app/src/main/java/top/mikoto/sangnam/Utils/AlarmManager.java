package top.mikoto.sangnam.Utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

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

    public void updateAlarm(int _id, int hour, int minute)
    {

    }

    public void updateAlarm(int _id, String days)
    {

    }

    public void updateAlarm(int _id, boolean run)
    {

    }

    public AlarmModel getAlarmById(int _id)
    {
        DBHelper dbHelper = new DBHelper(context,"ALARM",null,1);
        return dbHelper.getAlarmById(_id);
    }

    /**
     * @param hour HOUR_OF_DAY, 24h format
     * @param minute MINUTE
     * @param dayOfWeek SPECIFIC DAY : ex) Calendar.Monday
     * @param pendingIntent PENDING INTENT id
     */
    public void addAlarm(int hour, int minute, int dayOfWeek, PendingIntent pendingIntent)
    {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);

        if(calendar.getTimeInMillis() < System.currentTimeMillis())
        {
            calendar.add(Calendar.DAY_OF_YEAR,new GregorianCalendar().get(Calendar.DAY_OF_WEEK)-1);
        }

        alarmManager.setRepeating(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), android.app.AlarmManager.INTERVAL_DAY * 7, pendingIntent);

    }

    public void removeAlarm(int _id)
    {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        DBHelper dbHelper = new DBHelper(context,"ALARM",null,1);
        dbHelper.removeAlarm(_id);

        Intent mAlarmIntent = new Intent(context, AlarmReceiver.class);
        mAlarmIntent.setData(Uri.parse("mikoto://" + _id));
        mAlarmIntent.putExtra("id", _id);
        mAlarmIntent.setAction("top.mikoto.sangnam.ALARM");
        for(int i = 0; i< 6; i++) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    _id+i,
                    mAlarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
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

        Intent mAlarmIntent = new Intent(context, AlarmReceiver.class);
        mAlarmIntent.setData(Uri.parse("mikoto://"+_id));
        mAlarmIntent.putExtra("id",_id);
        mAlarmIntent.setAction("top.mikoto.sangnam.ALARM");

        for(int i = 0; i< 7; i++) //0 : sunday
        {
            if(days[i])
            {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        _id+i,
                        mAlarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
                addAlarm(hour,minute,i+1, pendingIntent);
            }
        }

        dbHelper.close();
    }

    public static String parseTime(String time)
    {
        String[] temp = time.split("\\|");
        //temp[0] = hour, temp[1] = minute
        String result = "";

        if(temp[0].length() == 1)
            result += "0"+temp[0];
        else
            result += temp[0];

        result += " : " + temp[1];

        return result;
    }


    public static String parseDays(String days)
    {
        char[] temp = days.toCharArray();
        String result = "";
        result += temp[0] == '1' ? "<font color='#ef0000'>일 </font>" : "";
        result += temp[1] == '1' ? "월 " : "";
        result += temp[2] == '1' ? "화 " : "";
        result += temp[3] == '1' ? "수 " : "";
        result += temp[4] == '1' ? "목 " : "";
        result += temp[5] == '1' ? "금 " : "";
        result += temp[6] == '1' ? "<font color='#006bef'>토 </font>" : "";
        return result;
    }

}
