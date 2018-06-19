package top.mikoto.sangnam.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import top.mikoto.sangnam.Adapters.AlarmListViewAdapter;
import top.mikoto.sangnam.Models.AlarmModel;
import top.mikoto.sangnam.R;
import top.mikoto.sangnam.Utils.AlarmManager;
import top.mikoto.sangnam.Utils.DB.DBHelper;

public class MainActivity extends AppCompatActivity {
    private ArrayList<AlarmModel> alarmModels = new ArrayList<>();
    private ListView listView;
    private AlarmListViewAdapter adapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Request External Storage Access Permission.
        //This is necessary for mp3 parser.
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //permission already denied. :(
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        200);
            }
        }

        //FAB
        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> startActivityForResult(new Intent(getApplicationContext(),AddAlarmActivity.class),200));

        dbHelper = new DBHelper(this,"ALARM",null,1);

        listView = findViewById(R.id.listview);
        alarmModels = dbHelper.getAllAlarms();
        adapter = new AlarmListViewAdapter(this,R.layout.alarm_item,alarmModels);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        dbHelper.close();
    }

    private void refresh()
    {
        Log.d("MisakaMOE", "refresh");
        alarmModels.clear();
        adapter.notifyDataSetChanged();
        alarmModels.addAll(dbHelper.getAllAlarms());
        for (AlarmModel item : alarmModels) {
            Log.d("MisakaMOE", item.getTime());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        getMenuInflater().inflate(R.menu.rmenu,menu);

        super.onCreateContextMenu(menu,v,menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index = info.position;
        AlarmManager alarmManager = new AlarmManager(getApplicationContext());
        alarmManager.removeAlarm(alarmModels.get(index).get_id());
        refresh();
        //Remove item using index
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refresh();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
