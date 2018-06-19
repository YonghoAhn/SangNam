package top.mikoto.sangnam.Activities;


import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dshantanu.androidsquareslib.AndroidSquares;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import top.mikoto.sangnam.Models.SongModel;
import top.mikoto.sangnam.R;
import top.mikoto.sangnam.Utils.MP3MusicPlayer;

public class RingAlarmActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_ring_alarm);

        MP3MusicPlayer mp3MusicPlayer = MP3MusicPlayer.getInstance(getApplicationContext());

        List<SongModel> list = mp3MusicPlayer.scanDeviceForMp3Files();


        AudioManager audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        Objects.requireNonNull(audioManager).setMode(AudioManager.STREAM_MUSIC);
        audioManager.setSpeakerphoneOn(true);
        while(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) < audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
            //Up volume until reach max
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        }
        //play mp3 randomly


        TextView textView = findViewById(R.id.txtTitle);
        AndroidSquares squares = findViewById(R.id.albumCover);
        String filename = "";

        if(list.size() > 0)
        {
            int index = new Random().nextInt(list.size());

            String file = "file://";
            SongModel song = list.get(index);
            filename = song.getPath();
            file+=filename;

            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(file));
            textView.setText(song.getTitle());

        }
        else
        {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.mac420);
            textView.setText("MACINTOSH PLUS - 420");
        }

        BitmapDrawable albumCover = new BitmapDrawable(mp3MusicPlayer.getAlbumArt(filename));
        squares.setBackground(albumCover);
        ImageView image = findViewById(R.id.layoutAlarm);
        image.setImageDrawable(albumCover);
        image.getDrawable().setColorFilter(0xff999999, PorterDuff.Mode.MULTIPLY);

        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }
}
