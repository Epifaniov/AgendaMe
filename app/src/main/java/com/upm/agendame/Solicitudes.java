package com.upm.agendame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.upm.agendame.Adapters.SolicitudesAmistadAdapter;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.Entities.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Solicitudes extends AppCompatActivity {

   private RecyclerView recyclerView;
   private ArrayList<Usuario> usuarios;
   private Usuario usrO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solicitudes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Solicitudes");
        usrO=(Usuario)getIntent().getExtras().getSerializable("usuario");
        usuarios=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.list_solicitudes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        cargarListaDeSolicitudes();
    }

    private void cargarListaDeSolicitudes(){
        String url = getString(R.string.ip)+"GetRequestFriends.php?id1="+usrO.getId();
        Log.d("urlGETReqs",url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json=response.optJSONArray("amigos");
                try {
                    for(int i=0; i<json.length();i++){
                        Usuario usr = new Usuario();
                        JSONObject jsonObject = json.getJSONObject(i);
                        usr.setId(jsonObject.optString("ID"));
                        usr.setNombre(jsonObject.optString("NOMBRE"));
                        usr.setPassword(jsonObject.optString("PASSWORD"));
                        usr.setEmail(jsonObject.optString("EMAIL"));
                        usr.setTelefono(jsonObject.optString("TELEFONO"));
                        usr.setNum_amigos(jsonObject.optString("NUM_AMIGOS"));
                        usr.setNum_citas(jsonObject.optString("NUM_CITAS"));
                        usr.setRuta_img(jsonObject.optString("RUTA_IMAGEN"));
                        usr.setNombre(jsonObject.optString("NOMBRE"));
                        usuarios.add(usr);
                    }
                    SolicitudesAmistadAdapter adapter = new SolicitudesAmistadAdapter(usuarios,getApplicationContext(),usrO);//,recyclerView);
                    recyclerView.setAdapter(adapter);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
