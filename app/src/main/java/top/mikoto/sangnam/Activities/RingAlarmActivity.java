package top.mikoto.sangnam.Activities;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import top.mikoto.sangnam.R;
import top.mikoto.sangnam.Utils.MP3MusicPlayer;

public class RingAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_alarm);

        MP3MusicPlayer mp3MusicPlayer = MP3MusicPlayer.getInstance(getApplicationContext());

        List<String> list = mp3MusicPlayer.scanDeviceForMp3Files();
    }

}
