package top.mikoto.sangnam.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import top.mikoto.sangnam.Activities.AddAlarmActivity;
import top.mikoto.sangnam.Activities.MainActivity;
import top.mikoto.sangnam.Models.AlarmModel;
import top.mikoto.sangnam.R;
import top.mikoto.sangnam.Utils.AlarmManager;

import static top.mikoto.sangnam.Utils.AlarmManager.parseDays;
import static top.mikoto.sangnam.Utils.AlarmManager.parseTime;

public class AlarmListViewAdapter extends ArrayAdapter<AlarmModel> {
    private ArrayList<AlarmModel> itemArrayList = new ArrayList<>();
    private Context context;
    private int rsrc;

    public AlarmListViewAdapter(@NonNull Context context, int resource, @NonNull List<AlarmModel> objects) {
        super(context, resource, objects);
        itemArrayList = (ArrayList<AlarmModel>) objects;
        this.context = context;
        rsrc = resource;
    }

    @Override
    public int getCount() {
        Log.d("MisakaMOE","size : "+itemArrayList.size());
        return itemArrayList.size();
    }

    @Override
    public AlarmModel getItem(int i) {

        return itemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null)
        {
            LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(rsrc, null);
        }
        Log.d("MisakaMOE","getView called");
        Objects.requireNonNull(v).setLongClickable(true);

        TextView txtTime = v.findViewById(R.id.txtTime);
        TextView txtDay = v.findViewById(R.id.txtDay);
        Switch sw = v.findViewById(R.id.sw);

        AlarmModel item = itemArrayList.get(i);
        if(item != null)
        {
            AlarmManager alarmManager = new AlarmManager(context);

            txtTime.setText(parseTime(item.getTime()));
            txtDay.setText(Html.fromHtml(parseDays(item.getDays())));
            sw.setChecked((item.getRun()!=0)); //if run was 0, set false.

            sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked)
                {
                    item.setRun(1);
                }
                else
                {
                    item.setRun(0);
                }
                alarmManager.updateAlarm(item);
            });

            v.setOnClickListener(vi -> {
                Intent intent = new Intent(vi.getContext(), AddAlarmActivity.class);
                intent.putExtra("_id", item.get_id());
                ((MainActivity) context).startActivityForResult(intent,200);
            });

        }

        return v;
    }

    public void addItem(String time, String day) {
        AlarmModel item = new AlarmModel();
        item.setTime(time);
        item.setDays(day);
        item.setRun(1);

        itemArrayList.add(item);
    }



}
