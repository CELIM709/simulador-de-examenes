package com.sistema.logica;

import com.sistema.modelos.*;
import java.util.*;
import java.util.stream.Collectors;

public class BancoPreguntas {

    private List<Pregunta> todasLasPreguntas;

    public BancoPreguntas() {
        todasLasPreguntas = new ArrayList<>();
        cargarPreguntasEjemplo();
    }

    private void cargarPreguntasEjemplo() {
        // --- TEMA: JAVA ---
        todasLasPreguntas.add(new Pregunta("¿Qué es Java?",
                Arrays.asList("Un café", "Lenguaje de Programación", "Un SO"), 1,
                "Java", NivelDificultad.BASICO, "Java es un lenguaje de alto nivel y orientado a objetos."));

        todasLasPreguntas.add(new Pregunta("¿Cuál es el tamaño de un int en Java?",
                Arrays.asList("16 bits", "32 bits", "64 bits"), 1,
                "Java", NivelDificultad.BASICO, "En Java, el tipo int siempre ocupa 32 bits (4 bytes)."));

        todasLasPreguntas.add(new Pregunta("¿Qué palabra clave se usa para heredar una clase?",
                Arrays.asList("implements", "extends", "inherits"), 1,
                "Java", NivelDificultad.INTERMEDIO, "Se usa 'extends' para clases y 'implements' para interfaces."));

        todasLasPreguntas.add(new Pregunta("¿Qué es el Garbage Collector?",
                Arrays.asList("Un virus", "Gestor de memoria automática", "Un depurador"), 1,
                "Java", NivelDificultad.INTERMEDIO, "Es el proceso que libera memoria eliminando objetos no referenciados."));

        todasLasPreguntas.add(new Pregunta("¿Cuál es una interfaz funcional en Java 8?",
                Arrays.asList("Predicate", "ArrayList", "Scanner"), 0,
                "Java", NivelDificultad.AVANZADO, "Predicate es una interfaz funcional que recibe un objeto y devuelve un boolean."));

        // --- TEMA: POO (Programación Orientada a Objetos) ---
        todasLasPreguntas.add(new Pregunta("¿Qué es el Polimorfismo?",
                Arrays.asList("Muchas formas", "Variables fijas", "Un error de sintaxis"), 0,
                "POO", NivelDificultad.INTERMEDIO, "Es la capacidad de un objeto de comportarse de diferentes formas según el contexto."));

        todasLasPreguntas.add(new Pregunta("¿Qué es la Encapsulación?",
                Arrays.asList("Borrar datos", "Ocultar el estado interno", "Crear copias"), 1,
                "POO", NivelDificultad.BASICO, "Consiste en proteger los datos internos de una clase usando modificadores de acceso."));

        todasLasPreguntas.add(new Pregunta("¿Qué define una clase Abstracta?",
                Arrays.asList("No se puede instanciar", "Solo tiene métodos estáticos", "No tiene constructor"), 0,
                "POO", NivelDificultad.INTERMEDIO, "Una clase abstracta sirve de base y no puede ser instanciada directamente."));

        todasLasPreguntas.add(new Pregunta("Principio SOLID: ¿Qué significa la 'S'?",
                Arrays.asList("Static", "Single Responsibility", "Software"), 1,
                "POO", NivelDificultad.AVANZADO, "Single Responsibility: Una clase debe tener una única razón para cambiar."));

        // --- TEMA: ESTRUCTURAS DE DATOS ---
        todasLasPreguntas.add(new Pregunta("¿Cuál es la principal ventaja de un HashMap?",
                Arrays.asList("Ordenamiento", "Búsqueda rápida por clave", "Bajo consumo de RAM"), 1,
                "Estructuras", NivelDificultad.INTERMEDIO, "El HashMap permite acceso en tiempo constante O(1) promedio mediante claves."));

        todasLasPreguntas.add(new Pregunta("¿Qué estructura funciona como LIFO (Last In, First Out)?",
                Arrays.asList("Queue (Cola)", "Stack (Pila)", "LinkedList"), 1,
                "Estructuras", NivelDificultad.BASICO, "La Pila (Stack) es donde el último elemento en entrar es el primero en salir."));
    }

    public List<Pregunta> generarExamen(String tema, NivelDificultad nivel, int cantidad) {
        List<Pregunta> filtradas = todasLasPreguntas.stream()
                .filter(p -> p.getTema().equalsIgnoreCase(tema) && p.getNivel() == nivel)
                .collect(Collectors.toList());

        Collections.shuffle(filtradas);

        return filtradas.stream()
                .limit(cantidad)
                .collect(Collectors.toList());
    }

    public void agregarPregunta(Pregunta p) {
        this.todasLasPreguntas.add(p);
    }

    public List<String> obtenerTemasDisponibles() {
        return todasLasPreguntas.stream()
                .map(Pregunta::getTema)
                .distinct()
                .collect(Collectors.toList());
    }

    public int getTotalPreguntas() {
        return todasLasPreguntas.size();
    }

    public List<Pregunta> getTodasLasPreguntas() {
        return todasLasPreguntas;
    }
}
