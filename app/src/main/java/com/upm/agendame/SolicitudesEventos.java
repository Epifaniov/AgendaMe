package com.upm.agendame;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.upm.agendame.Entities.NotificacionEvento;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.Entities.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SolicitudesEventos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Usuario usrO;
    private JsonObjectRequest jsonObjectRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes_eventos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        usrO=(Usuario) getIntent().getExtras().getSerializable("usuario");

        recyclerView = (RecyclerView)findViewById(R.id.recySolEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        cargarSolicitudes();
    }

    private void cargarSolicitudes(){
        String url = getString(R.string.ip)+"GetNotificacionesEventos.php?id="+usrO.getId();
        Log.d("UrlNotEven",url);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json = response.optJSONArray("notificaciones");
                try {
                for(int i=0; i<json.length();i++){
                    JSONObject jsonObject = json.getJSONObject(i);
                    NotificacionEvento not = new NotificacionEvento();
                    not.setEvento(jsonObject.optString("evento"));
                    not.setNombreUsr(jsonObject.optString("usuario"));
                    Date fecha = new SimpleDateFormat("dd/MM/yyyy")
                            .parse(jsonObject.optString("fecha"));
                    not.setFechaEvento(fecha);

                }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }
}
