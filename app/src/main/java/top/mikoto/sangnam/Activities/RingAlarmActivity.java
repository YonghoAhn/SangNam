package top.mikoto.sangnam.Activities;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Random;

import top.mikoto.sangnam.R;
import top.mikoto.sangnam.Utils.MP3MusicPlayer;

public class RingAlarmActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_alarm);

        MP3MusicPlayer mp3MusicPlayer = MP3MusicPlayer.getInstance(getApplicationContext());

        List<String> list = mp3MusicPlayer.scanDeviceForMp3Files();


        AudioManager audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        while(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) < audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
            //Up volume until reach max
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        }
        //play mp3 randomly
        String file = "file://"+list.get(new Random().nextInt(list.size()));
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(file));
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }
}
