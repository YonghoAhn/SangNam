package top.mikoto.sangnam.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import top.mikoto.sangnam.Activities.AddAlarmActivity;
import top.mikoto.sangnam.Models.AlarmModel;
import top.mikoto.sangnam.R;

import static top.mikoto.sangnam.Utils.AlarmManager.parseDays;
import static top.mikoto.sangnam.Utils.AlarmManager.parseTime;

public class AlarmListViewAdapter extends BaseAdapter {
    private final ArrayList<AlarmModel> itemArrayList;

    public AlarmListViewAdapter(ArrayList<AlarmModel> alarms, Context context) {
        itemArrayList = alarms;
    }


    @Override
    public int getCount() {
        return itemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater != null ? inflater.inflate(R.layout.alarm_item, viewGroup, false) : null;
            view.setLongClickable(true);
        }

        TextView txtTime = view.findViewById(R.id.txtTime);
        TextView txtDay = view.findViewById(R.id.txtDay);
        Switch sw = view.findViewById(R.id.sw);

        final AlarmModel item = itemArrayList.get(i);

        txtTime.setText(parseTime(item.getTime()));
        txtDay.setText(Html.fromHtml(parseDays(item.getDays())));
        sw.setChecked((item.getRun()!=0)); //if run was 0, set false.

        view.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddAlarmActivity.class);
            intent.putExtra("_id", item.get_id());
            context.startActivity(intent);
        });
        return view;
    }

    public void addItem(String time, String day) {
        AlarmModel item = new AlarmModel();
        item.setTime(time);
        item.setDays(day);
        item.setRun(1);

        itemArrayList.add(item);
    }



}
