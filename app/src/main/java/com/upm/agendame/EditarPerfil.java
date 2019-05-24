package com.upm.agendame;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.Entities.VolleySingleton;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfil extends AppCompatActivity {

    private Usuario usr;

    private EditText nombreReal,emailEdit,telefonoEdit,currentPass,newPass1,newPass2;
    private TextView btnUpdate;
    private StringRequest stringRequest;
    private Bitmap bmp;
    private CircleImageView imgProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_perfil);
        usr = new Usuario();
        usr=(Usuario) getIntent().getExtras().getSerializable("usuario");
        btnUpdate=(TextView)findViewById(R.id.btnUpdate);
        nombreReal=(EditText)findViewById(R.id.nombreReal);
        emailEdit=(EditText)findViewById(R.id.emailEdit);
        telefonoEdit=(EditText)findViewById(R.id.telefonoEdit);
        currentPass=(EditText)findViewById(R.id.currentPass);
        newPass1=(EditText)findViewById(R.id.newPass1);
        newPass2=(EditText)findViewById(R.id.newPass2);
        imgProfile=(CircleImageView)findViewById(R.id.imgProfile);
        nombreReal.setText(usr.getNombre());
        emailEdit.setText(usr.getEmail());
        telefonoEdit.setText(usr.getTelefono());

        Glide.with(getApplicationContext()).load(getString(R.string.ip)+usr.getRuta_img())
                .into(imgProfile);

        Glide.with(getApplicationContext()).asBitmap()
                .load(getString(R.string.ip)+usr.getRuta_img())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bmp=resource;
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });




        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUsr();
            }
        });

        Toast.makeText(getApplicationContext(),usr.getNombre(),Toast.LENGTH_SHORT).show();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Editar Perfil");

    }

    private void updateUsr(){
        usr.setNombre(nombreReal.getText().toString());
        usr.setTelefono(telefonoEdit.getText().toString());
        usr.setEmail(emailEdit.getText().toString());
        if(encryptPass(currentPass.getText().toString()).equals(usr.getPassword())){
            String url = getString(R.string.ip)+"UpdateUsr.php?";
            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Update",response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Update",error.getMessage());
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    DateFormat dt = new SimpleDateFormat("HHmmss");
                    Map<String,String> parametros = new HashMap<>();
                    parametros.put("id",usr.getId());
                    parametros.put("nombre",usr.getNombre());
                    usr.setRuta_img(usr.getNombre()+dt.format(new Date()));
                    parametros.put("nombre_imagen",usr.getRuta_img());
                    parametros.put("password",encryptPass(newPass1.getText().toString()));
                    parametros.put("email",usr.getEmail());
                    parametros.put("telefono",usr.getTelefono());
                    parametros.put("imagen",getStringImagen(bmp));
                    parametros.put("numamigos",usr.getNum_amigos());
                    parametros.put("numcitas",usr.getNum_citas());
                    return parametros;
                }
            };
            VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);

        }else{
            Toast.makeText(getApplicationContext(),"Contraseña errónea",Toast.LENGTH_SHORT).show();
        }
    }

    private String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100,baos);
        byte[] imagenBytes = baos.toByteArray();
        String encodedImagen = Base64.encodeToString(imagenBytes,Base64.DEFAULT);
        return encodedImagen;
    }
    private String encryptPass(String pass){
        String base64EncryptedString = "";

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(pass.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainTextBytes = pass.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = org.apache.commons.codec.binary.Base64.encodeBase64(buf);
            base64EncryptedString = new String(base64Bytes);

        } catch (Exception ex) {
        }
        return base64EncryptedString;
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
