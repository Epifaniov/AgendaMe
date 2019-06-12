package com.upm.agendame.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.upm.agendame.Entities.Eventos;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.EventoFijoActivity;
import com.upm.agendame.InfoEventoFijo;
import com.upm.agendame.PantallaPrincipal;
import com.upm.agendame.R;

import java.util.ArrayList;

public class EachEventAdapter extends RecyclerView.Adapter<EachEventAdapter.EachEVViewHolder> {

    private ArrayList<Eventos> list;
    private Context context;
    private Usuario usrO;
    public EachEventAdapter(ArrayList<Eventos> list, Usuario usrO, Context context){
        this.list=list;
        this.context=context;
        this.usrO=usrO;
    }
    @NonNull
    @Override
    public EachEVViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list,null,false);
        return new EachEVViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EachEVViewHolder eachEVViewHolder, int i) {
        eachEVViewHolder.nombreEvento.setText(list.get(i).getNombre());
        eachEVViewHolder.ev=list.get(i);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EachEVViewHolder extends RecyclerView.ViewHolder {
        TextView nombreEvento;
        Eventos ev;
        public EachEVViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreEvento=(TextView) itemView.findViewById(R.id.eventName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launcherActivityEditarRegistro = new Intent(context, InfoEventoFijo.class);
                    launcherActivityEditarRegistro.putExtra("usuario",usrO);
                    launcherActivityEditarRegistro.putExtra("evento", ev);
                    context.startActivity(launcherActivityEditarRegistro);
                }
            });

        }
    }
}
