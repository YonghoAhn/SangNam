package top.mikoto.sangnam.presentation.view

import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.dshantanu.androidsquareslib.AndroidSquares
import top.mikoto.sangnam.R
import top.mikoto.sangnam.utils.MP3MusicPlayer
import java.util.*

class RingAlarmActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        setContentView(R.layout.activity_ring_alarm)
        val mp3MusicPlayer = MP3MusicPlayer.getInstance(applicationContext)
        val list = mp3MusicPlayer.scanDeviceForMp3Files()
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        Objects.requireNonNull(audioManager).mode = AudioManager.STREAM_MUSIC
        audioManager.isSpeakerphoneOn = true
        while (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) < audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
            //Up volume until reach max
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
        }
        //play mp3 randomly
        val textView = findViewById<TextView>(R.id.txtTitle)
        val squares = findViewById<AndroidSquares>(R.id.albumCover)
        var filename = ""
        if (list.isNotEmpty()) {
            val index = Random().nextInt(list.size)
            var file = "file://"
            val song = list[index]
            filename = song.path
            file += filename
            mediaPlayer = MediaPlayer.create(applicationContext, Uri.parse(file))
            textView.text = song.title
        } else {
            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.mac420)
            textView.text = "MACINTOSH PLUS - 420"
        }
        val albumCover = BitmapDrawable(mp3MusicPlayer.getAlbumArt(filename))
        squares.background = albumCover
        val image = findViewById<ImageView>(R.id.layoutAlarm)
        image.setImageDrawable(albumCover)
        image.drawable.setColorFilter(-0x666667, PorterDuff.Mode.MULTIPLY)
        mediaPlayer.setLooping(true)
        mediaPlayer.start()
    }

    override fun onDestroy() {
        mediaPlayer!!.stop()
        super.onDestroy()
    }
}