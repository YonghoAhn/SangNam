package top.mikoto.sangnam.utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.graphics.BitmapFactory
import top.mikoto.sangnam.R
import android.provider.MediaStore
import android.util.Log
import top.mikoto.sangnam.models.SongModel
import top.mikoto.sangnam.utils.MP3MusicPlayer
import java.lang.Exception
import java.util.ArrayList

class MP3MusicPlayer private constructor(private val context: Context) {
    fun getAlbumArt(path: String): Bitmap {
        val mmr = MediaMetadataRetriever()
        if (path == "") {
            return BitmapFactory.decodeResource(context.resources, R.drawable.album)
        }
        mmr.setDataSource(path)
        val data = mmr.embeddedPicture
        // convert the byte array to a bitmap
        return if (data != null) {
            BitmapFactory.decodeByteArray(data, 0, data.size)
        } else {
            BitmapFactory.decodeResource(context.resources, R.drawable.album)
        }
    }

    fun scanDeviceForMp3Files(): List<SongModel> {
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        )
        val sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC"
        val mp3Files: MutableList<SongModel> = ArrayList<SongModel>()
        var cursor: Cursor? = null
        try {
            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            cursor = context.contentResolver.query(uri, projection, selection, null, sortOrder)
            if (cursor != null) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val song = SongModel()
                    song.setTitle(cursor.getString(0))
                    song.setArtist(cursor.getString(1))
                    song.setPath(cursor.getString(2))
                    song.setDisplayName(cursor.getString(3))
                    song.setDuration(cursor.getString(4))
                    cursor.moveToNext()
                    if (song.getPath() != null && song.getPath().endsWith(".mp3")) {
                        mp3Files.add(song)
                    }
                }
            }

            // print to see list of mp3 files
        } catch (e: Exception) {
            Log.e("MisakaMOE", e.toString())
        } finally {
            cursor?.close()
        }
        return mp3Files
    }

    companion object {
        private var instance: MP3MusicPlayer? = null
        fun getInstance(applicationContext: Context): MP3MusicPlayer? {
            return if (instance != null) instance else MP3MusicPlayer(applicationContext.applicationContext).also { instance = it }
        }
    }
}