package com.sistema.logica;

import com.sistema.modelos.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersistenciaDatos {

    private static final String ARCHIVO = "resultados_examen.txt";

    public static void guardarResultado(Resultado res) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO, true))) {
            writer.println("USUARIO:" + res.getUsuario().getNombre() + "|" + res.getUsuario().getCedula());
            writer.println("TEMA:" + res.getDetalles().get(0).getPregunta().getTema());
            writer.println("NIVEL:" + res.getDetalles().get(0).getPregunta().getNivel().name());
            writer.println("PUNTAJE:" + res.getPuntaje());
            writer.println("ACIERTOS:" + res.getAciertos() + "/" + res.getTotalPreguntas());
            writer.println("FECHA:" + res.getFecha().toString());
            writer.println("--------------------------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Este es el método que causaba el error de compilación. 
    // Ahora tiene el nombre correcto: leerHistorial()
    public static List<String> leerHistorial() {
        List<String> lineas = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return lineas;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineas;
    }

    public static List<Resultado> recuperarResultadosHistoricos() {
        List<Resultado> historial = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return historial;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            Usuario user = null;
            String tema = "";
            NivelDificultad nivel = null;
            double puntaje = 0;
            int aciertos = 0, total = 0;
            LocalDateTime fecha = null;

            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("USUARIO:")) {
                    String[] partes = linea.substring(8).split("\\|");
                    user = new Usuario(partes[0], partes[1]);
                } else if (linea.startsWith("TEMA:")) {
                    tema = linea.substring(5);
                } else if (linea.startsWith("NIVEL:")) {
                    nivel = NivelDificultad.valueOf(linea.substring(6).trim());
                } else if (linea.startsWith("PUNTAJE:")) {
                    puntaje = Double.parseDouble(linea.substring(8));
                } else if (linea.startsWith("ACIERTOS:")) {
                    String[] partes = linea.substring(9).split("/");
                    aciertos = Integer.parseInt(partes[0]);
                    total = Integer.parseInt(partes[1]);
                } else if (linea.startsWith("FECHA:")) {
                    fecha = LocalDateTime.parse(linea.substring(6).trim());
                } else if (linea.startsWith("----------------")) {
                    Pregunta dummy = new Pregunta("", new ArrayList<>(), 0, tema, nivel, "");
                    RegistroRespuesta reg = new RegistroRespuesta(dummy, 0);
                    Resultado r = new Resultado(user, aciertos, total, puntaje, Arrays.asList(reg));
                    historial.add(r);
                }
            }
        } catch (Exception e) {
            System.err.println("Error procesando historial para estadísticas: " + e.getMessage());
        }
        return historial;
    }
}
