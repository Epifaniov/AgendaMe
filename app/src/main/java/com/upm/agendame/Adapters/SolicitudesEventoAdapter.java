package com.upm.agendame.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.upm.agendame.Entities.NotificacionEvento;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.Entities.VolleySingleton;
import com.upm.agendame.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SolicitudesEventoAdapter extends RecyclerView.Adapter<SolicitudesEventoAdapter.SolicitudesViewHolder> {
    private ArrayList<NotificacionEvento> notificaciones;
    private Context context;
    private Usuario usrO;

    public SolicitudesEventoAdapter(ArrayList<NotificacionEvento> notificaciones, Usuario usrO, Context context){
        this.notificaciones=notificaciones;
        this.context=context;
        this.usrO = usrO;
    }

    @Override
    public SolicitudesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_solicitudes_eventos,null,false);
        return new SolicitudesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudesViewHolder solicitudesViewHolder, int i) {
        Glide.with(context).load(context.getString(R.string.ip)+notificaciones.get(i).getImg_usr())
                .into(solicitudesViewHolder.img);
        solicitudesViewHolder.nombreUsr.setText(notificaciones.get(i).getNombreUsr());
        solicitudesViewHolder.titulo.setText(notificaciones.get(i).getEvento());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String fechaInicio = dateFormat.format(notificaciones.get(i).getFechaInicio());
        String fechaFin = dateFormat.format(notificaciones.get(i).getFechaFin());
        solicitudesViewHolder.subtitulo.setText(fechaInicio +" - "+fechaFin);
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    public class SolicitudesViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img;
        TextView nombreUsr,titulo, subtitulo;
        ImageButton accept,delete;

        public SolicitudesViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView)itemView.findViewById(R.id.imgReqFriend);
            nombreUsr = (TextView)itemView.findViewById(R.id.nombreUsr);
            titulo = (TextView)itemView.findViewById(R.id.titulo_evento);
            subtitulo = (TextView)itemView.findViewById(R.id.subtitulo_evento);
            accept = (ImageButton) itemView.findViewById(R.id.aceptar_solicitud);
            delete = (ImageButton)itemView.findViewById(R.id.eliminar_solicitud);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    String url = context.getString(R.string.ip)+"UpdateEventoToShared.php?";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("SolAceptado",response);
                            if(response.equals("Evento_add")){
                                notificaciones.remove(getAdapterPosition());
                                notifyDataSetChanged();
                                //notifyItemRemoved(v.getVerticalScrollbarPosition());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> parametros = new HashMap<>();
                            parametros.put("id_usr",usrO.getId());
                            parametros.put("id_evento",notificaciones.get(getAdapterPosition()).getId_evento());
                            return parametros;
                        }
                    };
                    VolleySingleton.getInstanciaVolley(context).addToRequestQueue(stringRequest);
                }
            });


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = context.getString(R.string.ip)+"DeleteSolicitudEvento.php?";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("SolEliminado",response);
                            if(response.equals("Eliminado")){
                                notificaciones.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> parametros = new HashMap<>();
                            parametros.put("id_usr",usrO.getId());
                            parametros.put("id_evento",notificaciones.get(getAdapterPosition()).getId_evento());
                            return parametros;
                        }
                    };
                    VolleySingleton.getInstanciaVolley(context).addToRequestQueue(stringRequest);
                }
            });
        }



    }
}
