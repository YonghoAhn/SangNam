package top.mikoto.sangnam.Utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import java.util.Calendar;
import java.util.GregorianCalendar;

import top.mikoto.sangnam.Models.AlarmModel;
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
     * @param dayOfweek SPECIFIC DAY : ex) Calendar.Monday
     * @param pendingIntent PENDING INTENT
     */
    private void addAlarm(int hour, int minute, int dayOfweek, PendingIntent pendingIntent)
    {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfweek);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        if(calendar.getTimeInMillis() < System.currentTimeMillis())
        {
            calendar.add(Calendar.DAY_OF_YEAR,new GregorianCalendar().get(Calendar.DAY_OF_WEEK)-1);
        }



        alarmManager.setRepeating(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), android.app.AlarmManager.INTERVAL_DAY * 7, pendingIntent);

    }

    public void addAlarm(int hour, int miniute, String days_of_week)
    {
        //Check Alarm
        //parse alarm
        boolean[] days = new boolean[7];
        int tmp = 0;

        for(char s : days_of_week.toCharArray())
        {
            days[tmp++] = s != '0';
        }

        Calendar cal = Calendar.getInstance();
        int nWeek = cal.get(Calendar.DAY_OF_WEEK)-1; //match with days array index
        boolean isTodayRing = false;

        DBHelper dbHelper = new DBHelper(context,"ALARM",null,1);
        int _id = dbHelper.addAlarm();

        Intent mAlarmIntent = new Intent("top.mikoto.sangnam.ALARM");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                _id,
                mAlarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );


        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, miniute);

        //먼저 데이터베이스에 넣고 아이디를 리턴받아서, 그걸로 펜딩인텐트를 가져온다.
        //그래야 취소할때 같은 펜딩인텐트를 전달해서 받을 수 있음.
        AlarmModel alarmModel = new AlarmModel();


        //alarmManager.set(android.app.AlarmManager.RTC_WAKEUP,);

    }

}
