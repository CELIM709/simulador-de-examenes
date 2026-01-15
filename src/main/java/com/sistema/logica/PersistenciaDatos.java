package com.sistema.logica;

import com.sistema.modelos.RegistroRespuesta;
import com.sistema.modelos.Resultado;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaDatos {

    private static final String ARCHIVO = "resultados_examen.txt";

    public static void guardarResultado(Resultado res) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO, true))) {
            writer.println("USUARIO:" + res.getUsuario().getNombre() + "|" + res.getUsuario().getCedula());
            writer.println("PUNTAJE:" + res.getPuntaje() + "% (" + res.getAciertos() + "/" + res.getTotalPreguntas() + ")");
            writer.println("FECHA:" + res.getFecha());
            writer.println("DETALLE_RESPUESTAS:");
            for (RegistroRespuesta reg : res.getDetalles()) {
                String estado = reg.isEsCorrecta() ? "CORRECTO" : "INCORRECTO";
                writer.println("- " + reg.getPregunta().getEnunciado() + " | R: " + reg.getTextoRespuestaUsuario() + " | [" + estado + "]");
            }
            writer.println("--------------------------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> leerHistorial() {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            lineas.add("No hay registros previos.");
        }
        return lineas;
    }
}
