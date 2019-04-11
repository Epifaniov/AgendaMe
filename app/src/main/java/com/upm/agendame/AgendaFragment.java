package com.upm.agendame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AgendaFragment extends Fragment {

    /*
    private String[] numberDays = { "17", "18", "19"};

    private String[] weekDays = { "Mar", "Mié", "Jue"};
    */

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<3;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("numberDay", numberDays[i]);
            hm.put("weekDay", weekDays[i]);
            aList.add(hm);
        }
        String[] from = { "numberDay","weekDay" };

        int[] to = { R.id.dia_mes,R.id.dia_semana};
        */

        ArrayList<ContenidoDia> losDias = GetDias();

        View v = inflater.inflate(R.layout.fragment_agenda, container, false);

        final ListView daysList = (ListView) v.findViewById(R.id.my_day);
        daysList.setAdapter(new MyCustomBaseAdapter(getActivity().getBaseContext(), losDias));

        daysList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent launcherActivityInfoEventoFijo = new Intent(getActivity().getApplicationContext(),
                            InfoEventoFijo.class);
                    startActivity(launcherActivityInfoEventoFijo);
                } else if(position == 1){
                    Intent launcherActivityInfoEventoDinamico = new Intent(getActivity().getApplicationContext(),
                            InfoEventoDinamico.class);
                    startActivity(launcherActivityInfoEventoDinamico);
                }
            }
        });

        final FloatingActionButton fabAgregarEvento = (FloatingActionButton) v.findViewById(R.id.agregar_evento);

        final LinearLayout mEventoFijoLayout = (LinearLayout) v.findViewById(R.id.evento_fijo);
        final LinearLayout mEventoDinamicoLayout = (LinearLayout) v.findViewById(R.id.evento_dinamico);

        final Animation mostrarFab = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.mostrar_fab_agenda);
        final Animation ocultarFab = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.ocultar_fab_agenda);

        final Animation mostrarMiniFab = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.mostrar_minifab_agenda);
        final Animation ocultarMiniFab = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.ocultar_minifab_agenda);

        FloatingActionButton floatingActionButton = (FloatingActionButton) v.findViewById(R.id.fab_evento_fijo);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventoFijoLayout.setVisibility(View.GONE);
                mEventoDinamicoLayout.setVisibility(View.GONE);
                mEventoFijoLayout.startAnimation(ocultarMiniFab);
                mEventoDinamicoLayout.startAnimation(ocultarMiniFab);
                fabAgregarEvento.startAnimation(ocultarFab);
                Intent launcherActivityEventoFijo = new Intent(getActivity().getApplicationContext(),
                        EventoFijo.class);
                startActivity(launcherActivityEventoFijo);
            }
        });

        FloatingActionButton floatingActionDinamico = (FloatingActionButton) v.findViewById(R.id.fab_evento_dinamico);
        floatingActionDinamico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventoFijoLayout.setVisibility(View.GONE);
                mEventoDinamicoLayout.setVisibility(View.GONE);
                mEventoFijoLayout.startAnimation(ocultarMiniFab);
                mEventoDinamicoLayout.startAnimation(ocultarMiniFab);
                fabAgregarEvento.startAnimation(ocultarFab);
                Intent launcherActivityEventoDinamico = new Intent(getActivity().getApplicationContext(),
                        EventoDinamico.class);
                startActivity(launcherActivityEventoDinamico);
            }
        });

        fabAgregarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEventoFijoLayout.getVisibility() == View.VISIBLE &&
                        mEventoDinamicoLayout.getVisibility() == View.VISIBLE){
                    mEventoFijoLayout.setVisibility(View.GONE);
                    mEventoDinamicoLayout.setVisibility(View.GONE);
                    mEventoFijoLayout.startAnimation(ocultarMiniFab);
                    mEventoDinamicoLayout.startAnimation(ocultarMiniFab);
                    fabAgregarEvento.startAnimation(ocultarFab);
                } else {
                    mEventoFijoLayout.setVisibility(View.VISIBLE);
                    mEventoDinamicoLayout.setVisibility(View.VISIBLE);
                    mEventoFijoLayout.startAnimation(mostrarMiniFab);
                    mEventoDinamicoLayout.startAnimation(mostrarMiniFab);
                    fabAgregarEvento.startAnimation(mostrarFab);
                }
            }
        });

        /*
        ListView list = (ListView)v.findViewById(R.id.my_day);
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.lista_dia, from, to);
        list.setAdapter(adapter);
        */
        return v;
    }

    private ArrayList<ContenidoDia> GetDias(){
        ArrayList<ContenidoDia> losDias = new ArrayList<ContenidoDia>();

        ContenidoDia cd1 = new ContenidoDia();
        cd1.setNumberDay("17");
        cd1.setWeekDay("Mar");
        cd1.setEvent_1("Chips at least\n5:30 - 6:30 PM");
        cd1.setEvent_2("The best\n6 - 7 PM");
        cd1.setEvent_3("Hi and welcome\nEntre 8 y 9 PM");
        losDias.add(cd1);

        cd1 = new ContenidoDia();
        cd1.setNumberDay("18");
        cd1.setWeekDay("Mie");
        cd1.setEvent_1("Marlon's\nEntre 4:30 y 6:30 PM");
        cd1.setEvent_2("Melissa's party\n7:30 - 10:30 PM");
        cd1.setEvent_3("Pass from Mary at the airport\n11 - 11:30 PM");
        losDias.add(cd1);

        cd1 = new ContenidoDia();
        cd1.setNumberDay("19");
        cd1.setWeekDay("Jue");
        cd1.setEvent_1("Lunch with the boss\n4:30 - 6:00 PM");
        cd1.setEvent_2("Mariana's wedding\n7 - 9 PM");
        cd1.setEvent_3("Mariana's reception\n9:30 PM");
        losDias.add(cd1);

        return losDias;
    }
}
/*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }
}
*/
