package com.sistema.logica;

import com.sistema.modelos.Resultado;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Evaluador {

    public static double calcularPuntaje(int aciertos, int total) {
        if (total == 0) {
            return 0;
        }
        return ((double) aciertos / total) * 100;
    }

    public static String obtenerCalificacionCualitativa(double puntaje) {
        if (puntaje >= 90) {
            return "Sobresaliente";
        }
        if (puntaje >= 70) {
            return "Notable";
        }
        if (puntaje >= 50) {
            return "Aprobado";
        }
        return "Reprobado";
    }

    public static void exportarTXT(Resultado res) {
        String nombreArchivo = "Resultado_" + res.getUsuario().getNombre() + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo, true))) {
            writer.println("Usuario: " + res.getUsuario().getNombre());
            writer.println("Cédula: " + res.getUsuario().getCedula());
            writer.println("Puntaje: " + res.getPuntaje() + "%");
            writer.println("-----------------------------------");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static double calcularPromedioHistorico(List<Resultado> historial) {
        return historial.stream()
                .mapToDouble(r -> r.puntaje)
                .average()
                .orElse(0.0);
    }

    public static String generarInformeEstadistico(List<Resultado> historial) {
        if (historial.isEmpty()) {
            return "Sin registros previos.";
        }

        double promedio = calcularPromedioHistorico(historial);
        int totalExamenes = historial.size();
        double mejorPuntaje = historial.stream()
                .mapToDouble(r -> r.puntaje)
                .max()
                .orElse(0.0);

        StringBuilder sb = new StringBuilder();
        sb.append("ESTADÍSTICAS GENERALES\n");
        sb.append("Total de intentos: ").append(totalExamenes).append("\n");
        sb.append("Promedio general: ").append(String.format("%.2f", promedio)).append("%\n");
        sb.append("Mejor puntaje obtenido: ").append(String.format("%.2f", mejorPuntaje)).append("%");

        return sb.toString();
    }
}
