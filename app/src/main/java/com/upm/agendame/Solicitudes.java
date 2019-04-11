package com.upm.agendame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Solicitudes extends AppCompatActivity {

    private String[] Names = { "Roy Furse", "Omar Farrier"};

    int[] usersPics = new int[]{
            R.drawable.userpic_roy,
            R.drawable.userpic_omar
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solicitudes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Solicitudes");

        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<2;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("name", Names[i]);
            hm.put("user", Integer.toString(usersPics[i]) );
            aList.add(hm);
        }
        String[] from = { "user","name" };

        int[] to = { R.id.list_imagen,R.id.list_texto};

        ListView list = (ListView) findViewById(R.id.list_solicitudes);
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.lista_solicitudes, from, to);
        list.setAdapter(adapter);
    }
}
