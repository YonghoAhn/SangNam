package top.mikoto.sangnam.presentation.view

import androidx.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TimePicker
import android.widget.Toast
import top.mikoto.sangnam.R
import top.mikoto.sangnam.utils.AlarmManager
import top.mikoto.sangnam.databinding.ActivityAddAlarmBinding

class AddAlarmActivity() : AppCompatActivity() {
    init {

    }
    private lateinit var binding : ActivityAddAlarmBinding
    private var _id = -1
    private var isUpdateMode = false
    private lateinit var picker: TimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_alarm)
        val intent = intent
        _id = intent.getIntExtra("_id", -1)
        picker = findViewById<TimePicker>(R.id.timePicker)
        picker.setIs24HourView(true)

        if (_id != -1) {
            Log.d("MisakaMOE", _id.toString())
            val alarmManager = AlarmManager(applicationContext)
            val alarmModel = alarmManager.getAlarmById(_id)
            if (alarmModel != null) {
                val temp = alarmModel.time.split("\\|").toTypedArray()
                picker.currentHour = temp[0].toInt()
                picker.currentMinute = temp[1].toInt()
                isUpdateMode = true
            }
        }


        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
            actionBar.elevation = 0f
            actionBar.title = "알람 설정"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menu.getItem(0).setIcon(R.drawable.baseline_check_white_18)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btnConfirm) {
            //Save alarm from here.
            val alarmManager = AlarmManager.getInstance(applicationContext)
            if (isUpdateMode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) alarmManager.updateAlarm(_id, picker.hour, picker.minute, getDaysOfWeek()) else alarmManager.updateAlarm(_id, picker.currentHour, picker.currentMinute, getDaysOfWeek())
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) alarmManager.addAlarm(picker.hour, picker.minute, getDaysOfWeek()) else alarmManager.addAlarm(picker.currentHour, picker.currentMinute, getDaysOfWeek())
            }
            setResult(RESULT_OK)
            Toast.makeText(applicationContext, "Alarm set", Toast.LENGTH_SHORT).show()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getDaysOfWeek(): String? {
        val result = CharArray(7)
        result[0] = parseDay(binding.btnSun.isChecked)
        result[1] = parseDay(binding.btnMon.isChecked)
        result[2] = parseDay(binding.btnTue.isChecked)
        result[3] = parseDay(binding.btnWed.isChecked)
        result[4] = parseDay(binding.btnThu.isChecked)
        result[5] = parseDay(binding.btnFri.isChecked)
        result[6] = parseDay(binding.btnSat.isChecked)
        return String(result)
    }

    private fun parseDay(checked: Boolean): Char {
        return if (checked) '1' else '0'
    }
}