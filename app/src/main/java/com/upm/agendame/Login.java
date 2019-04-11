package com.upm.agendame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launcherActivityRegistro = new Intent(getApplicationContext(), Registro.class);
                startActivity(launcherActivityRegistro);
            }
        });

        Button login = findViewById(R.id.ingresar);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent launcherActivityEditarRegistro = new Intent(getApplicationContext(), PantallaPrincipal.class);
                startActivity(launcherActivityEditarRegistro);
            }
        });
    }
}
