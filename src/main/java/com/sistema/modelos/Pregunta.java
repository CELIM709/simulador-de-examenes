package com.sistema.modelos;

import java.util.List;

public class Pregunta {

    private String enunciado;
    private List<String> opciones;
    private int indiceCorrecto;
    private String tema;
    private NivelDificultad nivel;
    private String retroalimentacion;

    public Pregunta(String enunciado, List<String> opciones, int indiceCorrecto, String tema, NivelDificultad nivel, String retro) {
        this.enunciado = enunciado;
        this.opciones = opciones;
        this.indiceCorrecto = indiceCorrecto;
        this.tema = tema;
        this.nivel = nivel;
        this.retroalimentacion = retro;
    }

    // Getters
    public String getEnunciado() {
        return enunciado;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public boolean esCorrecta(int indice) {
        return indice == indiceCorrecto;
    }

    public String getRetroalimentacion() {
        return retroalimentacion;
    }

    public String getTema() {
        return tema;
    }

    public NivelDificultad getNivel() {
        return nivel;
    }

    public int getIndiceCorrecto() {
        return indiceCorrecto;
    }
}
