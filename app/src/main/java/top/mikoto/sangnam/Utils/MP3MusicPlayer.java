package top.mikoto.sangnam.Utils;

import android.content.Context;

public class MP3MusicPlayer {
    static MP3MusicPlayer instance = null;

    public static MP3MusicPlayer getInstance(Context applicationContext) {
        if(instance!=null)
            return instance;
        else
            return (instance = new MP3MusicPlayer(applicationContext));
    }

    public MP3MusicPlayer(Context context) {

    }
}
