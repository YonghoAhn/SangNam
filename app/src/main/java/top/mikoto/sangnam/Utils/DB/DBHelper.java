package top.mikoto.sangnam.Utils.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


import top.mikoto.sangnam.Models.AlarmModel;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Context context1 = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sb = " CREATE TABLE ALARM ( " +
                " _ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " TIME TEXT, " +
                " DAYS TEXT, " +
                " RUN INTEGER ) ";

        db.execSQL(sb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int addAlarm(AlarmModel alarm)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("TIME", alarm.getTime());
        values.put("DAYS", alarm.getDays());
        values.put("RUN",alarm.getRun());

        long result = db.insert("ALARM",null,values);
        db.close();
        return (int)result;
    }

    public ArrayList<AlarmModel> getAllAlarms()
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(" SELECT _ID, TIME, DAYS, RUN FROM ALARM ",null);
        ArrayList<AlarmModel> alarms = new ArrayList<>();
        AlarmModel alarm;

        while(cursor.moveToNext())
        {
            alarm = new AlarmModel();
            alarm.set_id(cursor.getInt(0));
            alarm.setTime(cursor.getString(1));
            alarm.setDays(cursor.getString(2));
            alarm.setRun(cursor.getInt(3));
            alarms.add(alarm);
        }
        cursor.close();
        db.close();
        return alarms;
    }

    public AlarmModel getAlarmById(int _id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT TIME, DAYS, RUN FROM ALARM WHERE _ID = ? ",new String[]{_id+""});
        AlarmModel alarm = null;
        if(cursor.moveToNext())
        {
            alarm = new AlarmModel(_id, cursor.getString(0),cursor.getString(1),cursor.getInt(2));
        }
        cursor.close();
        db.close();
        return alarm;
    }

    public void updateAlarm(AlarmModel alarm)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TIME", alarm.getTime());
        cv.put("DAYS", alarm.getDays());
        cv.put("RUN",alarm.getRun());

        db.update("ALARM", cv ,"_ID=?",new String[] {String.valueOf(alarm.get_id())});
        db.close();
    }

    public void removeAlarm(int _id)
    {
        SQLiteDatabase db =getWritableDatabase();
        db.delete("ALARM","_ID=?",new String[]{String.valueOf(_id)});
        db.close();
    }

}
