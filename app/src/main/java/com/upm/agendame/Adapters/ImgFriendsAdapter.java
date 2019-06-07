package com.upm.agendame.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.upm.agendame.Entities.Eventos;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImgFriendsAdapter extends RecyclerView.Adapter<ImgFriendsAdapter.ImgFrHolder> {
    private ArrayList<Usuario> list;
    private Context context;

    public ImgFriendsAdapter(ArrayList<Usuario> list, Context context){
        this.list=list;
        this.context=context;
    }
    @NonNull
    @Override
    public ImgFrHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.img_item_event,null,false);
        return new ImgFrHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgFrHolder imgFrHolder, int i) {
        //imgFrHolder.img.setImageBitmap(BitmapFactory.decodeByteArray(list.get(i).getImagen(), 0, list.get(i).getImagen().length));
        imgFrHolder.usr=list.get(i);
        Glide.with(context).load(context.getString(R.string.ip)+list.get(i).getRuta_img()).into(imgFrHolder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImgFrHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        Usuario usr;
        public ImgFrHolder(@NonNull View itemView) {
            super(itemView);
            usr= new Usuario();
            img=(CircleImageView) itemView.findViewById(R.id.imgFriendEvent);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,usr.getNombre(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
