package com.upm.agendame;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.upm.agendame.Adapters.SearchUsrEventFijoAdapter;
import com.upm.agendame.Entities.Eventos;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.Entities.VolleySingleton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventoFijoActivity extends AppCompatActivity {

    private EditText nomFijoEd,ubicacionEd,notaEdd;
    private TextView hInicioEd,hFinEd,datePickerFijo;
    private TextView btGuardar;
    private StringRequest stringRequest;
    private Usuario usrO;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timDialHinicio;
    private TimePickerDialog timDialHfin;
    private String day,mon,yy,hour1,minute1,hour2,minute2;
    private RecyclerView recyFriendEvFijo;
    private FloatingActionButton agregar_amigos;
    private final int FR_CODE=2;
    private ArrayList<Usuario> users;
    private SearchUsrEventFijoAdapter adapter;
    private Eventos evento;
    private ArrayList<Usuario> amigos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evento_fijo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Evento Fijo");
        usrO=(Usuario) getIntent().getExtras().getSerializable("usr");


        users = new ArrayList<>();
        recyFriendEvFijo=(RecyclerView)findViewById(R.id.recyFriendEvFijo);
        recyFriendEvFijo.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        adapter =new SearchUsrEventFijoAdapter(users,getApplicationContext(),usrO);
        recyFriendEvFijo.setAdapter(adapter);

        agregar_amigos=(FloatingActionButton)findViewById(R.id.agregar_amigos);
        nomFijoEd=(EditText)findViewById(R.id.nomFijoEd);
        ubicacionEd=(EditText)findViewById(R.id.ubicacionEd);
        notaEdd=(EditText)findViewById(R.id.notaEd);
        hInicioEd=(TextView)findViewById(R.id.hInicioEd);
        hFinEd=(TextView)findViewById(R.id.hFinEd);
        btGuardar=(TextView)findViewById(R.id.btGuardarFijo);
        datePickerFijo=(TextView)findViewById(R.id.datePickerFijo);
        Calendar now = Calendar.getInstance();
        day=String.valueOf(now.get(Calendar.DAY_OF_MONTH));
        mon=String.valueOf(now.get(Calendar.MONTH)+1);
        yy=String.valueOf(now.get(Calendar.YEAR));
        hour1=String.valueOf(now.get(Calendar.HOUR_OF_DAY));
        minute1=String.valueOf(now.get(Calendar.MINUTE));
        hour2=String.valueOf(now.get(Calendar.HOUR_OF_DAY));
        minute2=String.valueOf(now.get(Calendar.MINUTE));
        hInicioEd.setText(hour1+":"+minute1);
        hFinEd.setText(hour2+":"+minute2);
        DateFormat dayNamForm= new SimpleDateFormat("E");
        DateFormat dayForm= new SimpleDateFormat("dd");
        DateFormat montNamForm = new SimpleDateFormat("MMMM");
        DateFormat yearForm= new SimpleDateFormat("yyyy");
        try {
            Date daux = new SimpleDateFormat("dd/MM/yyyy")
                    .parse(String.valueOf(day)+"/"+String.valueOf(mon)+"/"+String.valueOf(yy));

            datePickerFijo.setText(dayNamForm.format(daux)+", "
                    +dayForm.format(daux)+" de "+montNamForm.format(daux)
                    +" de "+yearForm.format(daux));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if((Eventos)getIntent().getExtras().getSerializable("evento") != null){
            evento=new Eventos();
            amigos=new ArrayList<>();
            evento=(Eventos)getIntent().getExtras().getSerializable("evento");
            amigos=(ArrayList<Usuario>)getIntent().getExtras().getSerializable("amigos");
            cargarDatosEvento();
        }
        agregar_amigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchFriend.class);
                intent.putExtra("usrO",usrO);
                startActivityForResult(intent,FR_CODE);
            }
        });

        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        day=String.valueOf(dayOfMonth);
                        mon=String.valueOf(month+1);
                        yy=String.valueOf(year);
                        String datS=day+"/"+
                                mon+"/"+yy;
                        try {
                            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(datS);
                            Log.d("fechaDialog",datS);
                            DateFormat dayNamForm= new SimpleDateFormat("E");
                            DateFormat dayForm= new SimpleDateFormat("dd");
                            DateFormat montNamForm = new SimpleDateFormat("MMMM");
                            DateFormat yearForm= new SimpleDateFormat("yyyy");
                            datePickerFijo.setText(dayNamForm.format(date)+", "
                                    +dayForm.format(date)+" de "+montNamForm.format(date)
                                    +" de "+yearForm.format(date));


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        datePickerFijo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        timDialHinicio = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
               if(hourOfDay<10)hour1="0"+String.valueOf(hourOfDay);
               else hour1=String.valueOf(hourOfDay);
               if(minute<10) minute1="0"+String.valueOf(minute);
               else minute1=String.valueOf(minute);

               hInicioEd.setText(hour1+":"+minute1);
            }
        },24,0,true);

        hInicioEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timDialHinicio.show();
            }
        });
        timDialHfin = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay<10)hour2="0"+String.valueOf(hourOfDay);
                else hour2=String.valueOf(hourOfDay);
                if(minute<10) minute2="0"+String.valueOf(minute);
                else minute2=String.valueOf(minute);

                hFinEd.setText(hour2+":"+minute2);
            }
        },24,0,true);
        hFinEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timDialHfin.show();
            }
        });


        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(evento!=null){
                    //TODO update evento

                }else {
                    insertEvento();
                }
            }
        });

    }


    private void cargarDatosEvento(){

        DateFormat dayNamForm= new SimpleDateFormat("E");
        DateFormat dayForm= new SimpleDateFormat("dd");
        DateFormat montNamForm = new SimpleDateFormat("MMMM");
        DateFormat yearForm= new SimpleDateFormat("yyyy");

        datePickerFijo.setText(dayNamForm.format(evento.getFechaInicio())+", "
                +dayForm.format(evento.getFechaInicio())+" de "+montNamForm.format(evento.getFechaInicio())
                +" de "+yearForm.format(evento.getFechaInicio()));

        nomFijoEd.setText(evento.getNombre());
        ubicacionEd.setText(evento.getUbicacion());


        DateFormat hourOfDayF= new SimpleDateFormat("HH");
        DateFormat minuteF= new SimpleDateFormat("mm");
        int hourOfDay = Integer.valueOf(hourOfDayF.format(evento.getFechaInicio()));
        int minute = Integer.valueOf(minuteF.format(evento.getFechaInicio()));
        Log.d("Fecha1",String.valueOf(hourOfDay));
        if(hourOfDay<10)hour2="0"+String.valueOf(hourOfDay);
        else hour2=String.valueOf(hourOfDay);
        if(minute<10) minute2="0"+String.valueOf(minute);
        else minute2=String.valueOf(minute);
        hInicioEd.setText(hour2+":"+minute2);

        Log.d("Fecha2",evento.getFechaFin().toString());
        int hourOfDay2 = Integer.valueOf(hourOfDayF.format(evento.getFechaFin()));
        int minuteF2 = Integer.valueOf(minuteF.format(evento.getFechaFin()));
        if(hourOfDay2<10)hour2="0"+String.valueOf(hourOfDay2);
        else hour2=String.valueOf(hourOfDay);
        if(minuteF2<10) minute2="0"+String.valueOf(minuteF2);
        else minute2=String.valueOf(minuteF2);
        hFinEd.setText(hour2+":"+minute2);

        adapter =new SearchUsrEventFijoAdapter(amigos,getApplicationContext(),usrO);
        recyFriendEvFijo.setAdapter(adapter);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FR_CODE && resultCode==RESULT_OK){
            Usuario usr = new Usuario();
            usr= (Usuario) data.getExtras().get("friend");
            Log.d("FriendExtra",usr.getNombre());
            users.add(usr);
            adapter.notifyItemInserted(users.size()-1);
        }

    }
    private void insertEvento(){
        String url = getString(R.string.ip)+"InsertEvento.php?";
        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            Log.d("RespuestaIDMsg",response);
            //TODO response posee el id del mensaje insertado
            postSolicitudesTOUsers(response);


            finish();
                /*if(response.equals("eventos insertadorel_evento insertado")){
                    Toast.makeText(getApplicationContext(),"Evento creado",Toast.LENGTH_SHORT).show();
                    //TODO hacer el insert en la base de datos de los amigos a los que invitamos
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("resMala",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("id_usr",usrO.getId());
                parametros.put("nombre",nomFijoEd.getText().toString());
                parametros.put("notas",notaEdd.getText().toString());
                parametros.put("ubicacion",ubicacionEd.getText().toString());
                String fecha1=day+"/"+mon+"/"+yy+" "+hour1+":"+minute1;
                String fecha2=day+"/"+mon+"/"+yy+" "+hour2+":"+minute2;
                parametros.put("fecha_inicio",fecha1);
                parametros.put("fecha_fin",fecha2);
                parametros.put("tipo_evento","F");
                return parametros;
            }
        };
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);

    }


    private void postSolicitudesTOUsers(final String idEvento){
        String url = getString(R.string.ip)+"InsertSolicitudesUsr.php";

        for(final Usuario usr : users){

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("ErrorSolicitud",response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ErrorSolicitud",error.toString());
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros = new HashMap<>();
                    parametros.put("id_usr",usr.getId());
                    parametros.put("id_evento",idEvento);
                    return parametros;
                }
            };
            VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);

        }


    }

    @Override
    public void finish(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("updateAdapter","updateAdapter");
        setResult(RESULT_OK,returnIntent);
        super.finish();
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
