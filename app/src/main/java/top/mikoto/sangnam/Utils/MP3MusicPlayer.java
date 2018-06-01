package top.mikoto.sangnam.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import top.mikoto.sangnam.Models.SongModel;
import top.mikoto.sangnam.R;

public class MP3MusicPlayer {
    private static MP3MusicPlayer instance = null;
    private final Context context;
    public static MP3MusicPlayer getInstance(Context applicationContext) {
        if(instance!=null)
            return instance;
        else
            return (instance = new MP3MusicPlayer(applicationContext.getApplicationContext()));
    }

    private MP3MusicPlayer(Context context) {
        this.context = context;
    }

    public Bitmap getAlbumArt(String path)
    {
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);

        byte [] data = mmr.getEmbeddedPicture();
        // convert the byte array to a bitmap
        if(data != null)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            return bitmap;
        }
        else
        {
            return BitmapFactory.decodeResource(context.getResources(),R.drawable.Album);
        }
    }

    public List<String> scanDeviceForMp3Files(){
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";
        List<String> mp3Files = new ArrayList<>();

        Cursor cursor = null;
        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = context.getContentResolver().query(uri, projection, selection, null, sortOrder);
            if( cursor != null){
                cursor.moveToFirst();

                while( !cursor.isAfterLast() ){
                    SongModel song = new SongModel();
                    song.setTitle(cursor.getString(0));
                    song.setArtist(cursor.getString(1));
                    song.setPath(cursor.getString(2));
                    song.setDisplayName(cursor.getString(3));
                    song.setDuration(cursor.getString(4));
                    cursor.moveToNext();
                    if(song.getPath()!=null && song.getPath().endsWith(".mp3")) {
                        mp3Files.add(song.getPath());
                    }
                }

            }

            // print to see list of mp3 files
            for( String file : mp3Files) {
                Log.i("MisakaMOE", file);
            }

        } catch (Exception e) {
            Log.e("MisakaMOE", e.toString());
        }finally{
            if( cursor != null){
                cursor.close();
            }
        }
        return mp3Files;
    }


}
