package com.upm.agendame.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.Entities.VolleySingleton;
import com.upm.agendame.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SolicitudesAdapter extends RecyclerView.Adapter<SolicitudesAdapter.SolicitudesViewHolder> {
    private ArrayList<Usuario> usuarios;
    private Context context;
    private Usuario usrO;
   // private RecyclerView recyclerView;

    public SolicitudesAdapter(ArrayList<Usuario> usuarios, Context context,Usuario usrO){//,RecyclerView recyclerView){
        this.usuarios=usuarios;
        this.context=context;
        this.usrO=usrO;
       // this.recyclerView=recyclerView;
    }
    @NonNull
    @Override
    public SolicitudesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_solicitudes,null,false);
        return new SolicitudesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudesViewHolder solicitudesViewHolder, int i) {
        solicitudesViewHolder.nombre.setText(usuarios.get(i).getNombre());
        Glide.with(context).load(context.getString(R.string.ip)+usuarios.get(i).getRuta_img())
                .into(solicitudesViewHolder.img);
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class SolicitudesViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView nombre;
        ImageButton accept,delete;
        public SolicitudesViewHolder(@NonNull final View itemView) {
            super(itemView);
            img=(CircleImageView) itemView.findViewById(R.id.imgReqFriend   );
            nombre=(TextView)itemView.findViewById(R.id.list_texto);
            accept=(ImageButton)itemView.findViewById(R.id.aceptar_solicitud);
            delete=(ImageButton)itemView.findViewById(R.id.eliminar_solicitud);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = context.getString(R.string.ip)+"InsertFriend.php?";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("SolAceptado",response);
                            if(response.equals("Amigo_Aceptado")){
                                usuarios.remove(itemView.getVerticalScrollbarPosition());
                                notifyItemRemoved(itemView.getVerticalScrollbarPosition());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ErrorAccp",error.getMessage());
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> parametros = new HashMap<>();
                            parametros.put("id1",usuarios.get(itemView.getVerticalScrollbarPosition()).getId());
                            parametros.put("id2",usrO.getId());
                            return parametros;
                        }
                    };

                    VolleySingleton.getInstanciaVolley(context).addToRequestQueue(stringRequest);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = context.getString(R.string.ip)+"DeleteFriend.php?";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("SolEliminado",response);
                            if(response.equals("Eliminado")){
                                usuarios.remove(itemView.getVerticalScrollbarPosition());
                                notifyItemRemoved(itemView.getVerticalScrollbarPosition());
                            }
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
                            parametros.put("id1",usuarios.get(itemView.getVerticalScrollbarPosition()).getId());
                            parametros.put("id2",usrO.getId());
                            return parametros;
                        }
                    };
                    VolleySingleton.getInstanciaVolley(context).addToRequestQueue(stringRequest);
                }
            });
        }

    }
}
