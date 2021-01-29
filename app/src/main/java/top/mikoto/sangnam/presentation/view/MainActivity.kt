package top.mikoto.sangnam.presentation.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ListView
import top.mikoto.sangnam.R
import top.mikoto.sangnam.utils.adapters.AlarmListViewAdapter
import top.mikoto.sangnam.domain.models.dto.AlarmModel
import top.mikoto.sangnam.utils.AlarmManager
import top.mikoto.sangnam.utils.db.DBHelper
import java.util.*

class MainActivity : AppCompatActivity() {
    private var alarmModels = ArrayList<AlarmModel>()
    private lateinit var listView: ListView
    private var adapter: AlarmListViewAdapter? = null
    private var dbHelper: DBHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Request External Storage Access Permission.
        //This is necessary for mp3 parser.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //permission already denied. :(
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        200)
            }
        }

        //FAB
        val btnAdd = findViewById<FloatingActionButton>(R.id.btnAdd)
        btnAdd.setOnClickListener { v: View? -> startActivityForResult(Intent(applicationContext, AddAlarmActivity::class.java), 200) }
        dbHelper = DBHelper(this, "ALARM", null, 1)
        listView = findViewById(R.id.listview)
        alarmModels = dbHelper!!.allAlarms
        adapter = AlarmListViewAdapter(this, R.layout.alarm_item, alarmModels)
        listView.setAdapter(adapter)
        registerForContextMenu(listView)
        dbHelper!!.close()
    }

    private fun refresh() {
        Log.d("MisakaMOE", "refresh")
        alarmModels.clear()
        adapter!!.notifyDataSetChanged()
        alarmModels.addAll(dbHelper!!.allAlarms)
        for (item in alarmModels) {
            Log.d("MisakaMOE", item.time)
        }
        adapter!!.notifyDataSetChanged()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {
        menuInflater.inflate(R.menu.rmenu, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterContextMenuInfo
        val index = info.position
        val alarmManager = AlarmManager(applicationContext)
        alarmManager.removeAlarm(alarmModels[index]._id)
        refresh()
        //Remove item using index
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        refresh()
        super.onActivityResult(requestCode, resultCode, data)
    }
}