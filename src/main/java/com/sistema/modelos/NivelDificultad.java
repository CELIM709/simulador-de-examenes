package com.sistema.modelos;

public enum NivelDificultad {

    BASICO("Básico"),
    INTERMEDIO("Intermedio"),
    AVANZADO("Avanzado");

    private final String nombre;

    NivelDificultad(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
