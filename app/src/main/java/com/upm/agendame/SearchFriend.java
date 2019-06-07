package com.upm.agendame;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.upm.agendame.Adapters.SearchInvitFriendAdapter;
import com.upm.agendame.Adapters.SearchUsersAdapter;
import com.upm.agendame.Adapters.SearchUsrEventFijoAdapter;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.Entities.VolleySingleton;
import com.upm.agendame.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFriend extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private ArrayList<Usuario> friends;
    private Usuario usrO;
    private JsonObjectRequest jsonObjectRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView=(SearchView)findViewById(R.id.searchBar);
        recyclerView=(RecyclerView)findViewById(R.id.recyContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        usrO=(Usuario)getIntent().getExtras().getSerializable("usrO");
        friends=new ArrayList<>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String url = getString(R.string.ip) + "GetFriends.php?id="+String.valueOf(usrO.getId())+"&&nombre="+s;
                url.replace(" ", "%20");
                Log.d("URL",url);
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray json = response.optJSONArray("amigos");

                        try {
                            for (int i = 0; i < json.length(); i++) {
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
                                Log.d("Usuario",usr.getNombre());
                                friends.add(usr);
                            }
                            SearchInvitFriendAdapter adapter = new SearchInvitFriendAdapter(friends,getApplicationContext(),usrO);
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
                return false;
            }
        });



    }

}
