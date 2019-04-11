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

public class PerfilFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_perfil, container, false);

        Button login = v.findViewById(R.id.editar_perfil);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent launcherActivityEditarRegistro = new Intent(getActivity().getApplicationContext(),
                        EditarPerfil.class);
                startActivity(launcherActivityEditarRegistro);
            }
        });

        Button solicitudes = v.findViewById(R.id.solicitudes);
        solicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launcherActivitySolicitudes = new Intent(getActivity().getApplicationContext(),
                        Solicitudes.class);
                startActivity(launcherActivitySolicitudes);
            }
        });

        //return inflater.inflate(R.layout.fragment_perfil, container, false);
        return v;
    }
}
