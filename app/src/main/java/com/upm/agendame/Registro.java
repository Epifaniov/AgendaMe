package com.upm.agendame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.Entities.VolleySingleton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

public class Registro extends AppCompatActivity {

    private static final int REQUEST_CODE_CARGAR_IMAGEN = 1;
    private EditText nombre,email,telefono,pass1,pass2;
    private TextView registrarBt;
    private CircleImageView img_profile;
    private Bitmap img;
    private StringRequest stringRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Registrarse");
        nombre=(EditText)findViewById(R.id.nombreReal);
        email=(EditText)findViewById(R.id.nombreDeUsuarioRegistro);
        telefono=(EditText)findViewById(R.id.telefonoEdit);
        pass1=(EditText)findViewById(R.id.contraseñaRegistro);
        pass2=(EditText)findViewById(R.id.confirmarContraseñaRegistro);
        registrarBt=(TextView)findViewById(R.id.registrar);
        img_profile = (CircleImageView) findViewById(R.id.perfilFoto);


        img_profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_CODE_CARGAR_IMAGEN);
            }
        });

        registrarBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usr = new Usuario();
                if(pass1.getText().toString().equals(pass2.getText().toString())) {
                    usr.setNombre(nombre.getText().toString());
                    usr.setPassword(pass1.getText().toString());
                    usr.setEmail(email.getText().toString());
                    usr.setTelefono(telefono.getText().toString());
                    usr.setNum_amigos("0");
                    usr.setNum_citas("0");
                    insertUsr(usr);

                }else{
                    Toast.makeText(getApplicationContext(),"La contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                }




            }
        });

    }


    public void insertUsr(final Usuario usr){
        String url=getString(R.string.ip)+"Registro.php?";
        String pass_encrypt =encryptPass(usr.getPassword());
        usr.setPassword(pass_encrypt);
        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(),"MSG "+response,Toast.LENGTH_SHORT).show();
                Log.d("REPUESTA: ",response);
                if(response.trim().equalsIgnoreCase("Registra")){
                    Toast.makeText(getApplicationContext(),"Registrado con éxito",Toast.LENGTH_SHORT).show();
                    nombre.setText("");
                    email.setText("");
                    telefono.setText("");
                    pass1.setText("");
                    pass2.setText("");
                    img_profile.setImageResource(R.drawable.agregar_foto_perfil);
                }else {
                    Log.d("ERRORconN: ",response);
                    Toast.makeText(getApplicationContext(),"NO REGISTRADO: "+response,Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR_CONEXION: ",error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                DateFormat dt = new SimpleDateFormat("HHmmss");//Para el caching de la img
                Map<String,String> parametros = new HashMap<>();
                parametros.put("nombre",usr.getNombre());
                parametros.put("nombre_imagen",usr.getNombre()+dt.format(new Date()));
                parametros.put("password",usr.getPassword());
                parametros.put("email",usr.getEmail());
                parametros.put("telefono",usr.getTelefono());
                parametros.put("imagen",getStringImagen(img));
                parametros.put("numamigos",usr.getNum_amigos());
                parametros.put("numcitas",usr.getNum_citas());
                return parametros;
            }
        };

        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);

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

    private String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100,baos);
        byte[] imagenBytes = baos.toByteArray();
        String encodedImagen = Base64.encodeToString(imagenBytes,Base64.DEFAULT);
        return encodedImagen;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CODE_CARGAR_IMAGEN &&
                resultCode == Activity.RESULT_OK) {
            InputStream stream = null;

            try {
                stream = getContentResolver().openInputStream(data.getData());
                img = BitmapFactory.decodeStream(stream);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), img);
                img_profile.setImageBitmap(img);
                //img_profile.setBackground(bitmapDrawable);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try{
                        stream.close();
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
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
