package com.upm.agendame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.upm.agendame.Entities.Usuario;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {
    private Usuario usr;
    private CircleImageView profile_img;
    private TextView email,telefono,num_amg,num_citas,nombrePerfil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        usr = new Usuario();
        usr=(Usuario) getActivity().getIntent().getExtras().getSerializable("usuario");
        Toast.makeText(getContext(),usr.getNombre(),Toast.LENGTH_SHORT).show();
        //img_bitmap = BitmapFactory.decodeByteArray(usr.getImagen(), 0, usr.getImagen().length);
        nombrePerfil=(TextView)v.findViewById(R.id.nombrePerfil);
        profile_img=(CircleImageView)v.findViewById(R.id.profile_image);
        email=(TextView)v.findViewById(R.id.correo_electronico_perfil);
        telefono=(TextView)v.findViewById(R.id.numero_telefono);
        num_amg=(TextView)v.findViewById(R.id.numero_amigos);
        num_citas=(TextView)v.findViewById(R.id.numero_citas);

        nombrePerfil.setText(usr.getNombre());
        //profile_img.setImageBitmap(img_bitmap);


        Glide.with(getContext()).load(getString(R.string.ip)+usr.getRuta_img()).into(profile_img);
        email.setText(usr.getEmail());
        telefono.setText(usr.getTelefono());
        num_citas.setText(usr.getNum_citas());
        num_amg.setText(usr.getNum_amigos());
        Button editarPerfil = v.findViewById(R.id.editar_perfil);
        editarPerfil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent launcherActivityEditarRegistro = new Intent(getActivity().getApplicationContext(),
                        EditarPerfil.class);
                launcherActivityEditarRegistro.putExtra("usuario", usr);
                startActivity(launcherActivityEditarRegistro);
            }
        });

        Button cerrarSesion = v.findViewById(R.id.cerrar_sesion);
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launcherActivityCerrarSesion = new Intent(getActivity().getApplicationContext(),
                        Login.class);
                startActivity(launcherActivityCerrarSesion);
            }
        });

        Button solicitudes = v.findViewById(R.id.solicitudes);
        solicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launcherActivitySolicitudes = new Intent(getActivity().getApplicationContext(),
                        Solicitudes.class);
                launcherActivitySolicitudes.putExtra("usuario",usr);
                startActivity(launcherActivitySolicitudes);
            }
        });


        Button sol_inv = v.findViewById(R.id.sol_inv);
        sol_inv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launcherActivitySolicitudes = new Intent(getActivity().getApplicationContext(),
                        SolicitudesEventos.class);
                launcherActivitySolicitudes.putExtra("usuario",usr);
                startActivity(launcherActivitySolicitudes);
            }
        });

        //return inflater.inflate(R.layout.fragment_perfil, container, false);
        return v;
    }
}
