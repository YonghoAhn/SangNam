package top.mikoto.sangnam.utils.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import top.mikoto.sangnam.domain.models.dto.AlarmModel
import java.util.*

class DBHelper(context: Context, name: String?, factory: CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        val sb = " CREATE TABLE ALARM ( " +
                " _ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " TIME TEXT, " +
                " DAYS TEXT, " +
                " RUN INTEGER ) "
        db.execSQL(sb)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun addAlarm(alarm: AlarmModel): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put("TIME", alarm.getTime())
        values.put("DAYS", alarm.getDays())
        values.put("RUN", alarm.getRun())
        val result = db.insert("ALARM", null, values)
        db.close()
        return result.toInt()
    }

    val allAlarms: ArrayList<Any>
        get() {
            val db = readableDatabase
            val cursor = db.rawQuery(" SELECT _ID, TIME, DAYS, RUN FROM ALARM ", null)
            val alarms: ArrayList<AlarmModel> = ArrayList<AlarmModel>()
            var alarm: AlarmModel
            while (cursor.moveToNext()) {
                alarm = AlarmModel()
                alarm.time = cursor.getInt(0).toString()
                alarm.setTime(cursor.getString(1))
                alarm.setDays(cursor.getString(2))
                alarm.setRun(cursor.getInt(3))
                alarms.add(alarm)
            }
            cursor.close()
            db.close()
            return alarms
        }

    fun getAlarmById(_id: Int): AlarmModel? {
        val db = readableDatabase
        val cursor = db.rawQuery(" SELECT TIME, DAYS, RUN FROM ALARM WHERE _ID = ? ", arrayOf(_id.toString() + ""))
        var alarm: AlarmModel? = null
        if (cursor.moveToNext()) {
            alarm = AlarmModel(_id, cursor.getString(0), cursor.getString(1), cursor.getInt(2))
        }
        cursor.close()
        db.close()
        return alarm
    }

    fun updateAlarm(alarm: AlarmModel) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put("TIME", alarm.time)
        cv.put("DAYS", alarm.days)
        cv.put("RUN", alarm.run)
        db.update("ALARM", cv, "_ID=?", arrayOf(java.lang.String.valueOf(alarm.id)))
        db.close()
    }

    fun removeAlarm(_id: Int) {
        val db = writableDatabase
        db.delete("ALARM", "_ID=?", arrayOf(_id.toString()))
        db.close()
    }

    init {
        val context1 = context
    }
}