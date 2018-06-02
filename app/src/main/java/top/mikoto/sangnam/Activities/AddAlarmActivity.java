package top.mikoto.sangnam.Activities;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

import top.mikoto.sangnam.Models.AlarmModel;
import top.mikoto.sangnam.R;
import top.mikoto.sangnam.Utils.AlarmManager;
import top.mikoto.sangnam.databinding.ActivityAddAlarmBinding;

public class AddAlarmActivity extends AppCompatActivity {

    TimePicker picker;
    ActivityAddAlarmBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_alarm);

        picker = findViewById(R.id.timePicker);
        picker.setIs24HourView(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setElevation(0);
            actionBar.setTitle("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.getItem(0).setIcon(R.drawable.baseline_check_white_18);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.btnConfirm :
                //Save alarm from here.
                AlarmManager alarmManager = AlarmManager.getInstance(getApplicationContext());
                AlarmModel alarmModel = new AlarmModel();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.addAlarm(picker.getHour(),picker.getMinute(),getDaysOfWeek());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getDaysOfWeek()
    {
        char[] result = new char[7];
        result[0] = parseDay(binding.btnSun.isChecked());
        result[1] = parseDay(binding.btnMon.isChecked());
        result[2] = parseDay(binding.btnTue.isChecked());
        result[3] = parseDay(binding.btnWed.isChecked());
        result[4] = parseDay(binding.btnThu.isChecked());
        result[5] = parseDay(binding.btnFri.isChecked());
        result[6] = parseDay(binding.btnSat.isChecked());
        return  result.toString();
    }

    private char parseDay(boolean checked)
    {
        return checked ? '1' : '0';
    }

}
