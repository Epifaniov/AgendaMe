package com.upm.agendame.Entities;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton instanciaVolley;
    private RequestQueue request;
    private static Context context;

    private VolleySingleton(Context context) {
        this.context= context;
        this.request=getRequest();
    }

    public static synchronized VolleySingleton getInstanciaVolley(Context context) {
        if (instanciaVolley==null){
            instanciaVolley=new VolleySingleton(context);
        }
        return instanciaVolley;
    }

    public RequestQueue getRequest() {
        if(request==null){
            request= Volley.newRequestQueue(context.getApplicationContext());
        }
        return request;
    }

    public <T> void addToRequestQueue(Request<T> request){
        getRequest().add(request);
    }
}
