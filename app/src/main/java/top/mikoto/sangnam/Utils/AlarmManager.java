package top.mikoto.sangnam.Utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Calendar;
import java.util.Objects;

import top.mikoto.sangnam.Models.AlarmModel;
import top.mikoto.sangnam.Receivers.AlarmReceiver;
import top.mikoto.sangnam.Utils.DB.DBHelper;

public class AlarmManager {

    private static AlarmManager instance = null;
    private final Context context;

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

    public void cancelAlarm(int _id)
    {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

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

                Objects.requireNonNull(alarmManager).cancel(pendingIntent);

        }
    }

    public void updateAlarm(AlarmModel alarmModel)
    {
        DBHelper dbHelper = new DBHelper(context, "ALARM", null, 1);
        dbHelper.updateAlarm(alarmModel);

        if(alarmModel.getRun() == 1){
            registerAlarm(alarmModel);
        }
        else {
            cancelAlarm(alarmModel.get_id());
        }
    }


    public void updateAlarm(int _id, int hour, int minute, String days)
    {
        DBHelper dbHelper = new DBHelper(context,"ALARM",null,1);
        /*
         * first, we must remove past alarm.
         * second, re-register alarm.
         * in this situation, _id should not change at anytime.
         */
        cancelAlarm(_id);

        AlarmModel alarmModel = new AlarmModel();
        alarmModel.set_id(_id);
        alarmModel.setTime(""+hour+"|"+minute);
        alarmModel.setDays(days);
        alarmModel.setRun(1);
        dbHelper.updateAlarm(alarmModel);

        registerAlarm(alarmModel);

    }

    public void updateAlarm(int _id, int hour, int minute, String days, int run)
    {
        DBHelper dbHelper = new DBHelper(context,"ALARM",null,1);

        /*
         * first, we must remove past alarm.
         * second, re-register alarm.
         * in this situation, _id should not change at anytime.
         */
        cancelAlarm(_id);

        AlarmModel alarmModel = new AlarmModel();
        alarmModel.set_id(_id);
        alarmModel.setTime(""+hour+"|"+minute);
        alarmModel.setDays(days);
        alarmModel.setRun(run);

    }

    public AlarmModel getAlarmById(int _id)
    {
        DBHelper dbHelper = new DBHelper(context,"ALARM",null,1);
        return dbHelper.getAlarmById(_id);
    }

    public void removeAlarm(int _id)
    {
        DBHelper dbHelper = new DBHelper(context,"ALARM",null,1);
        dbHelper.removeAlarm(_id);

        cancelAlarm(_id);
    }

    public void addAlarm(int hour, int minute, String days_of_week)
    {
        //Check Alarm
        //parse alarm

        AlarmModel alarmModel = new AlarmModel();
        alarmModel.setDays(days_of_week);
        alarmModel.setTime("" + hour + "|" + minute);
        alarmModel.setRun(1);

        DBHelper dbHelper = new DBHelper(context,"ALARM",null,1);

        int _id = dbHelper.addAlarm(alarmModel);
        alarmModel.set_id(_id);
        registerAlarm(alarmModel);

        dbHelper.close();
    }

    public void registerAlarm(AlarmModel alarmModel)
    {
        int _id = alarmModel.get_id();
        String[] temp = alarmModel.getTime().split("\\|");
        int hour = Integer.parseInt(temp[0]);
        int minute = Integer.parseInt(temp[1]);

        boolean[] days = new boolean[7];
        int tmp = 0;

        for(char s : alarmModel.getDays().toCharArray())
        {
            days[tmp] = s != '0';
            tmp++;
        }

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
    }


    /**
     * @param hour HOUR_OF_DAY, 24h format
     * @param minute MINUTE
     * @param dayOfWeek SPECIFIC DAY : ex) Calendar.Monday
     * @param pendingIntent PENDING INTENT id
     */
    private void addAlarm(int hour, int minute, int dayOfWeek, PendingIntent pendingIntent)
    {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);

        if(calendar.getTimeInMillis() < System.currentTimeMillis()) //Set time is faster than System time : IF we set alarm, alarm will fire immediately.
        {
            calendar.add(Calendar.DAY_OF_YEAR,7);
        }

        Objects.requireNonNull(alarmManager).setRepeating(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), android.app.AlarmManager.INTERVAL_DAY * 7, pendingIntent);
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

        result += " : ";

        if(temp[1].length() == 1)
            result += "0" + temp[1];
        else
            result += temp[1];

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
