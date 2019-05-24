package com.upm.agendame;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.Entities.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAmigo extends AppCompatActivity {

    private Usuario usrF,usrO;
    private CircleImageView img;
    private TextView nombreUsr,numero_amigos,correo_electronico_perfil,numero_telefono;
    private JsonObjectRequest jsonObjectRequest;
    private Button requestButton;
    private String estado;
    private StringRequest stringRequest;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_amigo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        usrF=(Usuario)getIntent().getExtras().getSerializable("usuarioF");
        usrO=(Usuario)getIntent().getExtras().getSerializable("usuario");
        estado="";
        requestButton=(Button)findViewById(R.id.requestButton);
        nombreUsr=(TextView)findViewById(R.id.nombreUsr);
        numero_amigos=(TextView)findViewById(R.id.numero_amigos);
        correo_electronico_perfil=(TextView)findViewById(R.id.correo_electronico_perfil);
        numero_telefono=(TextView)findViewById(R.id.numero_telefono);
        img = (CircleImageView)findViewById(R.id.imagen_perfil);
        Glide.with(getApplicationContext()).load(getString(R.string.ip)+usrF.getRuta_img())
                .into(img);
        nombreUsr.setText(usrF.getNombre());
        numero_amigos.setText(usrF.getNum_amigos());
        correo_electronico_perfil.setText(usrF.getEmail());
        numero_telefono.setText(usrF.getTelefono());
        requestButton.setText("...");
        setTextButton();

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=getString(R.string.ip);

                if(estado.equals("F")){//Retirar de la agenda
                    url+="DeleteFriend.php?";
                    stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("respuestaAmigo",response);
                            requestButton.setText("Enviar solicitud de amistad");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("respuestaAmigo",error.getMessage());
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> parametros = new HashMap<>();
                            parametros.put("id1",usrO.getId());
                            parametros.put("id2",usrF.getId());
                            return parametros;
                        }
                    };
                    VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);
                }
                if(estado.equals("R")){
                    Toast.makeText(getApplicationContext(),"Solicitud de amistad ya enviada",Toast.LENGTH_SHORT).show();
                }

                if(estado.equals("E")){
                    url+="RequestFriend.php?";
                    stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("respuestaAmigo",response);
                            estado="R";
                            requestButton.setText("Solicitud de amistad enviada");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("respuestaAmigo",error.getMessage());
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> parametros = new HashMap<>();
                            parametros.put("id1",usrO.getId());
                            parametros.put("id2",usrF.getId());
                            parametros.put("estado","R");
                            return parametros;
                        }
                    };
                    VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);
                }
            }
        });

    }

    private void setTextButton(){
        String url=getString(R.string.ip)+"GetRelFriend.php?id1="+usrO.getId()+"&&id2="+usrF.getId();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray json=response.optJSONArray("amigo");
                try {
                    JSONObject jsonObject = json.getJSONObject(0);
                    String textoBoton="";
                    Log.d("ESTADO",jsonObject.optString("ESTADO"));
                    switch (jsonObject.optString("ESTADO")){
                        case "F":{
                            textoBoton="Retirar de agenda";
                            estado="F";
                            break;
                        }
                        case "R":{
                            textoBoton="Solicitud de amistad enviada";
                            estado="R";
                            break;
                        }
                        default:{
                            textoBoton="Enviar solicitud de amistad";
                            estado="E";
                            break;
                        }
                    }
                    requestButton.setText(textoBoton);

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
