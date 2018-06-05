package top.mikoto.sangnam.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import top.mikoto.sangnam.Adapters.AlarmListViewAdapter;
import top.mikoto.sangnam.R;
import top.mikoto.sangnam.Utils.DB.DBHelper;
import top.mikoto.sangnam.Utils.MP3MusicPlayer;

public class MainActivity extends AppCompatActivity {

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
        btnAdd.setOnClickListener(v -> {
           MP3MusicPlayer.getInstance(getApplicationContext()).scanDeviceForMp3Files();
            startActivityForResult(new Intent(getApplicationContext(),AddAlarmActivity.class),200);
        });

        DBHelper dbHelper = new DBHelper(this,"ALARM",null,1);

        ListView listView = findViewById(R.id.listview);
        AlarmListViewAdapter adapter = new AlarmListViewAdapter(dbHelper.getAllAlarms(),getApplicationContext());
        listView.setAdapter(adapter);

        dbHelper.close();
    }
}
