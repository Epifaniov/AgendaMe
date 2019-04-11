package com.upm.agendame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AmigosFragment extends Fragment {


    private String[] mNames = { "Angel Dillon", "Arthur Dikes", "Oscar Armer",
            "Peter Avis", "Mary Jenkins"};

    int[] usersPics = new int[]{
            R.drawable.userpic_angel,
            R.drawable.userpic_arthur,
            R.drawable.userpic_oscar,
            R.drawable.userpic_peter,
            R.drawable.imagen_perfil_amigo,
    };


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<5;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("name", mNames[i]);
            hm.put("user", Integer.toString(usersPics[i]) );
            aList.add(hm);
        }
        String[] from = { "user","name" };

        int[] to = { R.id.listimage,R.id.listtext};

        View v = inflater.inflate(R.layout.fragment_amigos, container,false);
        ListView list = (ListView)v.findViewById(R.id.listView1);
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.lista_contactos, from, to);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 4) {
                    Intent launcherActivityPerfilAmigo = new Intent(getActivity().getApplicationContext(),
                            PerfilAmigo.class);
                    startActivity(launcherActivityPerfilAmigo);
                }
            }
        });

        return v;
    }
}
/*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_amigos, container, false);
    }
}
*/
