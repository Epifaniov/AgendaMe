package com.upm.agendame.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.PerfilAmigo;
import com.upm.agendame.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUsersAdapter extends RecyclerView.Adapter<SearchUsersAdapter.SearchViewHolder> {

    private ArrayList<Usuario> users;
    private Context context;
    private Usuario usrO;
    public SearchUsersAdapter(ArrayList<Usuario> users, Context context,Usuario usrO ){
        this.users = users;
        this.context = context;
        this.usrO=usrO;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_contactos,null,false);
        return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        searchViewHolder.nombre.setText(users.get(i).getNombre());
        Glide.with(context).load(context.getString(R.string.ip)+users.get(i).getRuta_img())
                .into(searchViewHolder.img);
        searchViewHolder.usr=users.get(i);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        CircleImageView img;
        Usuario usr;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.nombreThumb);
            img=(CircleImageView)itemView.findViewById(R.id.thumbnail);
            usr = new Usuario();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PerfilAmigo.class);
                    intent.putExtra("usuarioF",usr);
                    intent.putExtra("usuario",usrO);
                    context.startActivity(intent);
                }
            });
        }
    }
}
