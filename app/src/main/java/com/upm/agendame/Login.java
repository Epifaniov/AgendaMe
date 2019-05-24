package com.upm.agendame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.upm.agendame.Entities.Usuario;
import com.upm.agendame.Entities.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class Login extends AppCompatActivity {
    private StringRequest stringRequest;
    private EditText email,password;
    private Usuario usr;
    private JsonObjectRequest jsonObjectRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        email=(EditText) findViewById(R.id.nombreDeUsuario) ;
        password=(EditText) findViewById(R.id.currentPass);
        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO auth
                Intent launcherActivityRegistro = new Intent(getApplicationContext(), Registro.class);
                startActivity(launcherActivityRegistro);
            }
        });

        Button login = findViewById(R.id.ingresar);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                accessToApp(email.getText().toString(),password.getText().toString());

            }
        });

    }


    private void accessToApp(String email, String pass){
        usr = new Usuario();
        String pass1=encryptPass(pass);
        try {
            //url=java.net.URLDecoder.encode(url, StandardCharsets.UTF_8.name());
            pass1=java.net.URLEncoder.encode(pass1,StandardCharsets.UTF_8.name());
            Log.d("PassTODAY: ",pass1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url=getString(R.string.ip)+"GetUsr.php?";
        url+="email="+email+"&&password="+pass1;

        url=url.replace(" ","%20");
        Log.d("URL_DIR: ",url);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json = response.optJSONArray("usuario");
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject=json.getJSONObject(0);
                    if(jsonObject.optString("nombre").equals("[\"NO REGISTRO\"]")){
                        Toast.makeText(getApplicationContext(),"Contraseña incorrecta",Toast.LENGTH_SHORT).show();
                    }else {
                        usr.setId(jsonObject.optString("ID"));
                        usr.setNombre(jsonObject.optString("NOMBRE"));
                        usr.setPassword(jsonObject.optString("PASSWORD"));
                        usr.setEmail(jsonObject.optString("EMAIL"));
                        usr.setTelefono(jsonObject.optString("TELEFONO"));
                        usr.setNum_amigos(jsonObject.optString("NUM_AMIGOS"));
                        usr.setNum_citas(jsonObject.optString("NUM_CITAS"));
                        usr.setRuta_img(jsonObject.optString("RUTA_IMAGEN"));
                        Log.d("USR", usr.getNombre());
                        Intent launcherActivityEditarRegistro = new Intent(getApplicationContext(), PantallaPrincipal.class);
                        launcherActivityEditarRegistro.putExtra("usuario", usr);
                        startActivity(launcherActivityEditarRegistro);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"ERROR: "+error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }

   /* private void getImgFromURL(String path){
        String url=getString(R.string.ip)+path;
        url=url.replace(" ","%20");
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                response.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                byte[] byteArray = bStream.toByteArray();

                usr.setImagen(byteArray);
                Intent launcherActivityEditarRegistro = new Intent(getApplicationContext(), PantallaPrincipal.class);
                launcherActivityEditarRegistro.putExtra("usuario", usr);
                startActivity(launcherActivityEditarRegistro);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(imageRequest);

    }*/


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



}
