package com.sistema.modelos;

import java.time.LocalDateTime;
import java.util.List;

public class Resultado {

    private Usuario usuario;
    public int aciertos;
    public int totalPreguntas;
    public double puntaje;
    public LocalDateTime fecha;
    private List<RegistroRespuesta> detalles;

    public Resultado(Usuario usuario, int aciertos, int total, double puntaje, List<RegistroRespuesta> detalles) {
        this.usuario = usuario;
        this.aciertos = aciertos;
        this.totalPreguntas = total;
        this.puntaje = puntaje;
        this.detalles = detalles;
        this.fecha = LocalDateTime.now();
    }

    public List<RegistroRespuesta> getDetalleRespuestas() {
        return detalles;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public double getPuntaje() {
        return puntaje;
    }

    public int getAciertos() {
        return aciertos;
    }

    public int getTotalPreguntas() {
        return totalPreguntas;
    }

    public List<RegistroRespuesta> getDetalles() {
        return detalles;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
