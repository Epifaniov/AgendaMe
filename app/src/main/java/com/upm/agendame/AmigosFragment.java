package com.upm.agendame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.upm.agendame.Adapters.SearchUsersAdapter;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.Entities.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AmigosFragment extends Fragment {

    private android.support.v7.widget.SearchView searchView;
    private JsonObjectRequest jsonObjectRequest;
    private RecyclerView recyclerView;
    private Usuario usrO;
    private List<Usuario> amigos;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_amigos, container,false);
        searchView=(android.support.v7.widget.SearchView) v.findViewById(R.id.searchBar);

        recyclerView=(RecyclerView) v.findViewById(R.id.recyContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usrO=(Usuario)getActivity().getIntent().getExtras().getSerializable("usuario");
        amigos = new ArrayList<>();
        cargarAmigos();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String url = getActivity().getString(R.string.ip) + "SearchUsers.php?id="+usrO.getId()+"&nombre=" + s;
                url.replace(" ", "%20");
                Log.d("URLSearch",url);
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray json = response.optJSONArray("users");
                        ArrayList<Usuario> users = new ArrayList<>();
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
                                users.add(usr);
                            }
                            SearchUsersAdapter adapter = new SearchUsersAdapter(users, getContext(),usrO);
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
                VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
                return false;
            }
        });
        return v;
    }


    private void cargarAmigos(){
        String url=getString(R.string.ip)+"GetFriends.php?id="+usrO.getId()+"&nombre=";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            JSONArray json = response.optJSONArray("amigos");
                try {
            for(int i=0; i<json.length();i++){
                Usuario usr = new Usuario();
                JSONObject jsonObject = json.getJSONObject(i);
                usr.setId(jsonObject.optString("ID"));
                usr.setNombre(jsonObject.optString("NOMBRE"));
                usr.setRuta_img(jsonObject.optString("RUTA_IMAGEN"));
                usr.setEmail(jsonObject.optString("EMAIL"));
                usr.setTelefono(jsonObject.optString("TELEFONO"));
                usr.setNum_citas(jsonObject.optString("NUM_CITAS"));
                usr.setNum_citas(jsonObject.optString("NUM_AMIGOS"));
                amigos.add(usr);

            }

                    SearchUsersAdapter adapter = new SearchUsersAdapter((ArrayList)amigos, getContext(),usrO);
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
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);

    }

}

