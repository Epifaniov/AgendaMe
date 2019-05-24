package com.upm.agendame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.upm.agendame.Adapters.ImgFriendsAdapter;
import com.upm.agendame.Entities.Eventos;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.Entities.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoEventoFijo extends AppCompatActivity{
    private Eventos evento;
    private ArrayList<Usuario> usuarios;
    private JsonObjectRequest jsonObjectRequest;
    private TextView nombreEvento, fechaEvento;
    private Usuario usr;
    private RecyclerView friendsRecy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_evento_fijo_recycler);
        evento = new Eventos();
        evento=(Eventos)getIntent().getExtras().getSerializable("evento");
        Toast.makeText(getApplicationContext(),evento.getNotas(),Toast.LENGTH_SHORT).show();
        usuarios= new ArrayList<>();
        friendsRecy=(RecyclerView)findViewById(R.id.recyFriendsEvent);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        friendsRecy.setLayoutManager(layoutManager);
        cargarUsuarios(evento.getId());


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
                startActivity(launcherActivityEditarEvento);
            }
        });
    }

    private void cargarUsuarios(String id){
        String url=getString(R.string.ip)+"GetUsuariosEvento.php?id="+id;
        jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json = response.optJSONArray("usuario");

                try {
                        for (int i = 0; i < json.length(); i++) {
                        usr = new Usuario();
                        final JSONObject jsonObject = json.getJSONObject(i);
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

    /*private void getImgFromURL(String path){
        String url=getString(R.string.ip)+path;
        url=url.replace(" ","%20");
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                response.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                byte[] byteArray = bStream.toByteArray();

                usr.setImagen(byteArray);
                usuarios.add(usr);

                ImgFriendsAdapter adapter=new ImgFriendsAdapter(usuarios,getApplicationContext());
                friendsRecy.setAdapter(adapter);
                Log.d("IMG_REQ: ",usr.getNombre());
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(imageRequest);
    }
*/
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
