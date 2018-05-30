package top.mikoto.sangnam.Utils.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import top.mikoto.sangnam.Models.AlarmModel;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;



    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE ALARM ( ");
        sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" TIME TEXT, ");
        sb.append(" DAYS TEXT, ");
        sb.append(" RUN INTEGER ) ");

        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addAlarm(AlarmModel alarm)
    {
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO ALARM ( ");
        sb.append(" TIME, DAYS, RUN ) ");
        sb.append(" VALUES ( ?, ?, ? ) ");

        db.execSQL(sb.toString(),new Object[] {
                alarm.getTime(),
                alarm.getDays(),
                alarm.getRun()
        });
    }

    public List getAllAlrams()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, TIME, DAYS, RUN FROM ALARM ");

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(sb.toString(),null);
        List alarms = new ArrayList();
        AlarmModel alarm = null;

        while(cursor.moveToNext())
        {
            alarm = new AlarmModel();
            alarm.set_id(cursor.getInt(0));
            alarm.setTime(cursor.getString(1));
            alarm.setDays(cursor.getString(2));
            alarm.setRun(cursor.getInt(3));
            alarms.add(alarm);
        }

        return alarms;
    }

    public AlarmModel getAlarmById(int _id)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT TIME, DAYS, RUN FROM ALARM WHERE _ID = ? ");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(),new String[]{_id+""});
        AlarmModel alarm = null;
        if(cursor.moveToNext())
        {
            alarm = new AlarmModel(_id, cursor.getString(0),cursor.getString(1),cursor.getInt(2));
        }
        return alarm;
    }
}
