package com.upm.agendame.Adapters;

import android.app.Activity;
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
import com.upm.agendame.R;
import com.upm.agendame.SearchFriend;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchInvitFriendAdapter extends RecyclerView.Adapter<SearchInvitFriendAdapter.SearchFriendHolder> {


    private ArrayList<Usuario>friends;
    private Context context;
    private Usuario usrO;
    public SearchInvitFriendAdapter(ArrayList<Usuario> friends, Context context, Usuario usrO){
        this.friends=friends;
        this.context=context;
        this.usrO=usrO;

    }

    @NonNull
    @Override
    public SearchFriendHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_contactos,null,false);
        return new SearchFriendHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFriendHolder searchFriendHolder, int i) {
    searchFriendHolder.nombreThumb.setText(friends.get(i).getNombre());
        Glide.with(context).load(context.getString(R.string.ip)+friends.get(i).getRuta_img())
                .into(searchFriendHolder.thumbnail);
        searchFriendHolder.usrF=friends.get(i);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class SearchFriendHolder extends RecyclerView.ViewHolder {
        CircleImageView thumbnail;
        TextView nombreThumb;
        Usuario usrF;
        public SearchFriendHolder(@NonNull final View itemView) {
            super(itemView);
            thumbnail=(CircleImageView)itemView.findViewById(R.id.thumbnail);
            nombreThumb=(TextView)itemView.findViewById(R.id.nombreThumb);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = ((SearchFriend)itemView.getContext()).getIntent();
                    i.putExtra("friend",usrF);
                    ((SearchFriend)itemView.getContext()).setResult(Activity.RESULT_OK,i);
                    ((SearchFriend)itemView.getContext()).finish();
                }
            });
        }
    }
}
