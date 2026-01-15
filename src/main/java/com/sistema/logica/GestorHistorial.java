package com.sistema.logica;

import com.sistema.modelos.Resultado;
import java.util.ArrayList;
import java.util.List;

public class GestorHistorial {

    private static List<Resultado> historialGlobal = new ArrayList<>();

    public static void guardarResultado(Resultado resultado) {
        historialGlobal.add(resultado);
    }

    public static List<Resultado> getHistorialGlobal() {
        return historialGlobal;
    }

    public static List<Resultado> buscarPorUsuario(String nombre) {
        List<Resultado> filtrados = new ArrayList<>();
        for (Resultado r : historialGlobal) {
            if (r.getUsuario().getNombre().equalsIgnoreCase(nombre)) {
                filtrados.add(r);
            }
        }
        return filtrados;
    }
}
