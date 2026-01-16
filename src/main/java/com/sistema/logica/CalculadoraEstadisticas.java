package com.sistema.logica;

import com.sistema.modelos.Resultado;
import java.util.List;
import java.util.stream.Collectors;

public class CalculadoraEstadisticas {

    public static List<Resultado> filtrar(List<Resultado> fuente, String usuario, String tema, String nivelStr) {
        return fuente.stream()
                .filter(r -> usuario.equals("Todos") || r.getUsuario().getNombre().equalsIgnoreCase(usuario))
                .filter(r -> tema.equals("Todos") || r.getDetalles().get(0).getPregunta().getTema().equalsIgnoreCase(tema))
                .filter(r -> nivelStr.equals("Todos") || r.getDetalles().get(0).getPregunta().getNivel().toString().equals(nivelStr))
                .collect(Collectors.toList());
    }

    public static double calcularPromedio(List<Resultado> resultados) {
        return resultados.stream().mapToDouble(Resultado::getPuntaje).average().orElse(0.0);
    }
}
