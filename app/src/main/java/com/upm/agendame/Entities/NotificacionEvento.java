package com.upm.agendame.Entities;

import java.util.Date;

public class NotificacionEvento {
    private String nombreUsr;
    private String img_usr;
    private String Evento;
    private Date fechaInicio;
    private Date fechaFin;
    private String id_evento;

    public String getId_evento() {
        return id_evento;
    }

    public void setId_evento(String id_evento) {
        this.id_evento = id_evento;
    }

    public String getImg_usr() {
        return img_usr;
    }

    public void setImg_usr(String img_usr) {
        this.img_usr = img_usr;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }



    public String getNombreUsr() {
        return nombreUsr;
    }

    public void setNombreUsr(String nombreUsr) {
        this.nombreUsr = nombreUsr;
    }

    public String getEvento() {
        return Evento;
    }

    public void setEvento(String evento) {
        Evento = evento;
    }



}
