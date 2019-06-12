package com.upm.agendame;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.upm.agendame.Adapters.EventsAdapter;
import com.upm.agendame.Entities.Eventos;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.Entities.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AgendaFragment extends Fragment {

    /*
    private String[] numberDays = { "17", "18", "19"};

    private String[] weekDays = { "Mar", "Mié", "Jue"};
    */


    private ArrayList<ArrayList<Eventos>> allEvents;
    private RecyclerView recyclerView;
    private Usuario usr;
    private JsonObjectRequest jsonObjectRequest;
    private static int RES_COD_FIJO=1;
    private static int RES_COD_DIN=2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_agenda_recycler, container, false);
        usr=new Usuario();
        usr=(Usuario)getActivity().getIntent().getExtras().getSerializable("usuario");
        recyclerView=(RecyclerView) v.findViewById(R.id.recy_event);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allEvents=new ArrayList<>();
        //String ds="14/05/2019";
        Date fecha = new Date(System.currentTimeMillis());
        Log.d("LOG: ","No hay problema de parse "+fecha.toString());
        cargarDatos(usr,fecha);


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
                        EventoFijoActivity.class);
                launcherActivityEventoFijo.putExtra("usr",usr);
                startActivityForResult(launcherActivityEventoFijo,RES_COD_FIJO);
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


        return v;
    }

    private void cargarDatos(Usuario usr,Date fecha){
        Log.d("LOG: ","Entrada en el método de carga "+fecha.toString());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(fecha);
        String passOK="";
        try {
            //url=java.net.URLDecoder.encode(url, StandardCharsets.UTF_8.name());
            passOK=java.net.URLEncoder.encode(usr.getPassword(), StandardCharsets.UTF_8.name());
            Log.d("Pass: ",passOK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url=getString(R.string.ip)+"GetEvento.php?";
        url=url+"email="+usr.getEmail()+"&&password="+passOK+"&&fecha="+strDate;
        Log.d("LOG: ","URL: "+url);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,    new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json = response.optJSONArray("evento");
                ArrayList<Eventos>eventos =new ArrayList<>();
                try {

                    for (int i = 0; i < json.length(); i++) {
                        Eventos ev = new Eventos();
                        JSONObject jsonObject = json.getJSONObject(i);
                        ev.setId(jsonObject.optString("ID"));
                        ev.setNombre(jsonObject.optString("NOMBRE"));
                        ev.setNotas(jsonObject.optString("NOTAS"));
                        ev.setUbicacion(jsonObject.optString("UBICACION"));
                        ev.setFechaInicio(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.optString("FECHA_INICIO")));
                        ev.setFechaFin(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.optString("FECHA_FIN")));
                        eventos.add(ev);
                        Log.d("LOG: ","Evento cargado: "+eventos.get(i).getFechaInicio());
                    }
                    cargarListaDeListas(eventos);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void cargarListaDeListas(ArrayList<Eventos> lista){
        //ArrayList<ArrayList<Eventos>> res = new ArrayList<>();
        DateFormat dt = new SimpleDateFormat("dd/MM/yyyy");

        for(int i=0;i<lista.size();i++){
            String fechaExample = dt.format(new Date());
            Log.d("FechaEjemplo",fechaExample);
            String fecha1=dt.format(lista.get(i).getFechaInicio());
            Log.d("fechaComp1",lista.get(i).getFechaInicio().toString());
            ArrayList<Eventos> list2 = new ArrayList<>();
            for(int j=0;j<lista.size();j++){
                String fecha2=dt.format(lista.get(j).getFechaInicio());
                Log.d("fechaComp2",fecha2);
                if(fecha1.equals(fecha2)){
                    list2.add(lista.get(j));
                }
            }
            allEvents.add(list2);
           }

        for(int i=0;i<allEvents.size();i++){
            for (int j=0;j<allEvents.size();j++){
                if(i!=j){
                    if(dt.format(allEvents.get(i).get(0).getFechaInicio()).equals(dt.format(allEvents.get(j).get(0).getFechaInicio()))){
                        allEvents.remove(i);
                    }

                }
            }
        }

        EventsAdapter adapter = new EventsAdapter(allEvents,usr,getContext());
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RES_COD_FIJO && resultCode == getActivity().RESULT_OK ){
            //String ds="14/05/2019";
            SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
            Date fecha = new Date(System.currentTimeMillis());
            Log.d("LOG: ","No hay problema de parse "+format.format(fecha));
            cargarDatos(usr,fecha);
        }
    }

}

