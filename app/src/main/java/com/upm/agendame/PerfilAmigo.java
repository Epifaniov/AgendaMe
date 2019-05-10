package com.upm.agendame;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class PerfilAmigo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_amigo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Button deleteFriend = findViewById(R.id.eliminar_amigo);
        deleteFriend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //Build the notification
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext(), PantallaPrincipal.channelId)
                                .setSmallIcon(R.drawable.ic_amigos_evento)
                                .setContentTitle("AgendaMe")
                                .setContentText("La lista de amigos ha sido actualizada");
                //builder.setContentIntent(resultPendingIntent);
                // Gets an instance of the NotificationManager service//
                NotificationManager mNotificationManager =
                        (NotificationManager)
                                getSystemService(Context.NOTIFICATION_SERVICE);
                // rather than create a new one. In this example, the notification’s ID is 001//
                mNotificationManager.notify(1, mBuilder.build());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
