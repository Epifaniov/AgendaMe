package com.upm.agendame.Entities;

import java.util.Date;

public class NotificacionEvento {
    private String nombreUsr;
    private String Evento;
    private Date fechaEvento;


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

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }


}
