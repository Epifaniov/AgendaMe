package com.upm.agendame.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upm.agendame.Entities.Eventos;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {

   private ArrayList<ArrayList<Eventos>>events;
   private Context context;
   private Usuario usrO;
   private Fragment fragment;

    public EventsAdapter(ArrayList<ArrayList<Eventos>> list, Usuario usrO, Context context,Fragment fragment){
        this.events=list;
        this.usrO=usrO;
        this.context=context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_event,null,false);
        return new EventsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder eventsViewHolder, int i) {
        DateFormat day = new SimpleDateFormat("dd");
        eventsViewHolder.dia.setText(day.format(events.get(i).get(0).getFechaInicio()));
        eventsViewHolder.adapter = new EachEventAdapter(events.get(i),usrO,context,fragment);
        eventsViewHolder.evListView.setAdapter(eventsViewHolder.adapter);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventsViewHolder extends RecyclerView.ViewHolder {
        TextView dia;
        RecyclerView evListView;
        EachEventAdapter adapter;
        public EventsViewHolder(@NonNull View itemView) {
            super(itemView);
            dia = (TextView) itemView.findViewById(R.id.dia_mes);
            evListView=(RecyclerView) itemView.findViewById(R.id.listVEvents);
            evListView.setLayoutManager(new LinearLayoutManager(context));


        }


    }
}
