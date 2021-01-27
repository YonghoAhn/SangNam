package top.mikoto.sangnam.adapters

import android.content.Context
import android.content.Intent
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import top.mikoto.sangnam.R
import top.mikoto.sangnam.activities.AddAlarmActivity
import top.mikoto.sangnam.activities.MainActivity
import top.mikoto.sangnam.models.AlarmModel
import top.mikoto.sangnam.utils.AlarmManager
import java.util.*

class AlarmListViewAdapter(context: Context, resource: Int, objects: List<AlarmModel>) : ArrayAdapter<AlarmModel>(context, resource, objects) {
    private val itemArrayList: ArrayList<AlarmModel> = objects as ArrayList<AlarmModel>
    override fun getCount(): Int {
        Log.d("MisakaMOE", "size : " + itemArrayList.size)
        return itemArrayList.size
    }

    override fun getItem(i: Int): AlarmModel {
        return itemArrayList[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View, viewGroup: ViewGroup): View {
        Log.d("MisakaMOE", "getView called")
        Objects.requireNonNull(view).isLongClickable = true
        val txtTime = view.findViewById<TextView>(R.id.txtTime)
        val txtDay = view.findViewById<TextView>(R.id.txtDay)
        val sw = view.findViewById<Switch>(R.id.sw)
        val item = itemArrayList[i]
        val alarmManager = AlarmManager(context)
        txtTime.text = AlarmManager.parseTime(item.time)
        txtDay.text = Html.fromHtml(AlarmManager.parseDays(item.days))
        sw.isChecked = item.run != 0 //if run was 0, set false.
        sw.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                item.run = 1
            } else {
                item.run = 0
            }
            alarmManager.updateAlarm(item)
        }
        view.setOnClickListener { vi: View ->
            val intent = Intent(vi.context, AddAlarmActivity::class.java)
            intent.putExtra("_id", item._id)
            (context as MainActivity).startActivityForResult(intent, 200)
        }
        return view
    }

    fun addItem(time: String?, day: String?) {
        val item = AlarmModel()
        item.time = time
        item.days = day
        item.run = 1
        itemArrayList.add(item)
    }

}