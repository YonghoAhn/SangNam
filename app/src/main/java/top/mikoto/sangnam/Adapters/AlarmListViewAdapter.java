package top.mikoto.sangnam.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import top.mikoto.sangnam.Models.AlarmListViewItem;
import top.mikoto.sangnam.R;

public class AlarmListViewAdapter extends BaseAdapter {
    private ArrayList<AlarmListViewItem> itemArrayList = new ArrayList<>();

    public AlarmListViewAdapter() {
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
        final int pos = i;
        final Context context = viewGroup.getContext();

        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.alarm_item,viewGroup,false);
        }

        TextView txtTime = view.findViewById(R.id.txtTime);
        TextView txtDay = view.findViewById(R.id.txtDay);
        Switch sw = view.findViewById(R.id.sw);

        AlarmListViewItem item = itemArrayList.get(i);

        txtTime.setText(item.getTime());
        txtDay.setText(item.getDayOfTheWeek());
        sw.setChecked(item.isRun());

        return view;
    }

    public void addItem(String time, String day) {
        AlarmListViewItem item = new AlarmListViewItem();
        item.setTime(time);
        item.setDayOfTheWeek(day);
        item.setRun(true);

        itemArrayList.add(item);
    }

}
