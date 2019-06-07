package com.upm.agendame.Entities;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Usuario implements Serializable {
private String nombre;
    private String password;
    private String email;
    private String telefono;
    private String num_amigos;
    private String num_citas;
    private String id;
    private String ruta_img;

    public String getRuta_img() {
        return ruta_img;
    }

    public void setRuta_img(String ruta_img) {
        this.ruta_img = ruta_img;
    }




    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    public String getNum_amigos() {
        return num_amigos;
    }

    public void setNum_amigos(String num_amigos) {
        this.num_amigos = num_amigos;
    }

    public String getNum_citas() {
        return num_citas;
    }

    public void setNum_citas(String num_citas) {
        this.num_citas = num_citas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
