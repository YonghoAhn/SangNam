package top.mikoto.sangnam.Activities;


import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.dshantanu.androidsquareslib.AndroidSquares;

import java.util.List;
import java.util.Random;

import top.mikoto.sangnam.R;
import top.mikoto.sangnam.Utils.MP3MusicPlayer;

public class RingAlarmActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_ring_alarm);

        MP3MusicPlayer mp3MusicPlayer = MP3MusicPlayer.getInstance(getApplicationContext());

        List<String> list = mp3MusicPlayer.scanDeviceForMp3Files();


        AudioManager audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        audioManager.setMode(AudioManager.STREAM_RING);
        audioManager.setSpeakerphoneOn(true);
        while(audioManager.getStreamVolume(AudioManager.STREAM_RING) < audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)) {
            //Up volume until reach max
            audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        }
        //play mp3 randomly
        String file = "file://";
        String filename = list.get(new Random().nextInt(list.size()));
        file+=filename;

        AndroidSquares squares = findViewById(R.id.albumCover);
        squares.setBackground(new BitmapDrawable(mp3MusicPlayer.getAlbumArt(filename)));

        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(file));
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }
}
