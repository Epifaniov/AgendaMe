package com.upm.agendame.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.PerfilAmigo;
import com.upm.agendame.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUsrEventFijoAdapter extends RecyclerView.Adapter<SearchUsrEventFijoAdapter.SearchEventFijoHolder> {

    private ArrayList<Usuario> invitados;
    private Context context;
    private Usuario usrO;
    public SearchUsrEventFijoAdapter(ArrayList<Usuario> invitados,Context context,Usuario usrO){
        this.invitados=invitados;
        this.context=context;
        this.usrO=usrO;
    }
    @NonNull
    @Override
    public SearchEventFijoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.img_profile,null,false);
        return new SearchEventFijoHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchEventFijoHolder searchEventFijoHolder, int i) {
        Glide.with(context).load(context.getString(R.string.ip)+invitados.get(i).getRuta_img())
                .into(searchEventFijoHolder.img_profEvF);
    }

    @Override
    public int getItemCount() {
        return invitados.size();
    }

    public class SearchEventFijoHolder extends RecyclerView.ViewHolder {
        CircleImageView img_profEvF;
        public SearchEventFijoHolder(@NonNull final View itemView) {
            super(itemView);
            img_profEvF=(CircleImageView)itemView.findViewById(R.id.img_profEvF);
            img_profEvF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PerfilAmigo.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("usuarioF",invitados.get(itemView.getVerticalScrollbarPosition()));
                    intent.putExtra("usuario",usrO);
                    context.startActivity(intent);

                }
            });
        }
    }
}
