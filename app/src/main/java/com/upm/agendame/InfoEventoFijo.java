package com.upm.agendame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.upm.agendame.Adapters.ImgFriendsAdapter;
import com.upm.agendame.Entities.Eventos;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.Entities.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InfoEventoFijo extends AppCompatActivity{
    private Eventos evento;
    private ArrayList<Usuario> usuarios;
    private JsonObjectRequest jsonObjectRequest;
    private TextView nombreEvento, fechaEvento;
    private RecyclerView friendsRecy;
    private Usuario usrO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_evento_fijo_recycler);
        evento = new Eventos();
        usrO = new Usuario();
        usrO=(Usuario)getIntent().getExtras().getSerializable("usuario");
        evento=(Eventos)getIntent().getExtras().getSerializable("evento");
        usuarios= new ArrayList<>();
        friendsRecy=(RecyclerView)findViewById(R.id.recyFriendsEvent);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        friendsRecy.setLayoutManager(layoutManager);
        cargarUsuarios(evento.getId());

        ImageView eliminar_evento = (ImageView)findViewById(R.id.eliminar_evento);
        eliminar_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvento();
            }
        });

        nombreEvento=(TextView)findViewById(R.id.nombreEvento);
        fechaEvento=(TextView)findViewById(R.id.fechaEvento);
        nombreEvento.setText(evento.getNombre());

        DateFormat dtDay = new SimpleDateFormat("E");
        DateFormat dtHour = new SimpleDateFormat("HH:mm");
        String day = dtDay.format(evento.getFechaInicio());
        String h1 = dtHour.format(evento.getFechaInicio());
        String h2 = dtHour.format(evento.getFechaFin());
        fechaEvento.setText(day+", "+h1+" - "+h2);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Evento Fijo");

        ImageView edit = findViewById(R.id.editar_evento);
        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent launcherActivityEditarEvento = new Intent(getApplicationContext(), EventoFijoActivity.class);
                launcherActivityEditarEvento.putExtra("usr",usrO);
                launcherActivityEditarEvento.putExtra("evento",evento);
                launcherActivityEditarEvento.putExtra("amigos",usuarios);
                startActivity(launcherActivityEditarEvento);
            }
        });
    }


    private void deleteEvento(){
        String url=getString(R.string.ip)+"DeleteEvento.php?";
        Log.d("UrlElmEvento",url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("EventoEliminado",response);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ErrorDelete",error.getMessage());
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros = new HashMap<>();
                    parametros.put("id_usr",usrO.getId());
                    parametros.put("id_evento",evento.getId());
                    return parametros;
                }
            };
            VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);
        }


    private void cargarUsuarios(String id){
        String url=getString(R.string.ip)+"GetUsuariosEvento.php?id="+id;
        jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json = response.optJSONArray("usuario");

                try {
                        for (int i = 0; i < json.length(); i++) {

                            Usuario usr = new Usuario();
                        JSONObject jsonObject = json.getJSONObject(i);
                        usr.setId(jsonObject.optString("ID"));
                        usr.setNombre(jsonObject.optString("NOMBRE"));
                        usr.setPassword(jsonObject.optString("PASSWORD"));
                        usr.setEmail(jsonObject.optString("EMAIL"));
                        usr.setTelefono(jsonObject.optString("TELEFONO"));
                        usr.setNum_citas(jsonObject.optString("NUM_AMIGOS"));
                        usr.setNum_amigos(jsonObject.optString("NUM_CITAS"));
                        usr.setRuta_img(jsonObject.optString("RUTA_IMAGEN"));
                        usuarios.add(usr);
                    }
                    ImgFriendsAdapter adapter=new ImgFriendsAdapter(usuarios,getApplicationContext());
                    friendsRecy.setAdapter(adapter);


                } catch (JSONException e) {
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

    @Override
    public void finish(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("updateAdapter","updateAdapter");
        setResult(RESULT_OK,returnIntent);
        super.finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
