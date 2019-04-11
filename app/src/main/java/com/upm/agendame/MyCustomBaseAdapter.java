package com.upm.agendame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCustomBaseAdapter extends BaseAdapter {
    private static ArrayList<ContenidoDia> diasArrayList;

    private LayoutInflater mInflater;

    public MyCustomBaseAdapter(Context context, ArrayList<ContenidoDia> dias) {
        diasArrayList = dias;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return diasArrayList.size();
    }

    public Object getItem(int position) {
        return diasArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lista_dia, null);
            holder = new ViewHolder();
            holder.txtNumberDay = (TextView) convertView.findViewById(R.id.dia_mes);
            holder.txtWeekDay = (TextView) convertView.findViewById(R.id.dia_semana);
            holder.txtEvent_1 = (TextView) convertView.findViewById(R.id.event_1);
            holder.txtEvent_2 = (TextView) convertView.findViewById(R.id.event_2);
            holder.txtEvent_3 = (TextView) convertView.findViewById(R.id.event_3);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtNumberDay.setText(diasArrayList.get(position).getNumberDay());
        holder.txtWeekDay.setText(diasArrayList.get(position).getWeekDay());
        holder.txtEvent_1.setText(diasArrayList.get(position).getEvent_1());
        holder.txtEvent_2.setText(diasArrayList.get(position).getEvent_2());
        holder.txtEvent_3.setText(diasArrayList.get(position).getEvent_3());

        return convertView;
    }

    static class ViewHolder {
        TextView txtNumberDay;
        TextView txtWeekDay;
        TextView txtEvent_1;
        TextView txtEvent_2;
        TextView txtEvent_3;
    }
}