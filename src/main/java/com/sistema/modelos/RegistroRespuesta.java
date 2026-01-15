package com.sistema.modelos;

public class RegistroRespuesta {

    private Pregunta pregunta;
    private int indiceSeleccionado;
    private boolean esCorrecta;

    public RegistroRespuesta(Pregunta pregunta, int indiceSeleccionado) {
        this.pregunta = pregunta;
        this.indiceSeleccionado = indiceSeleccionado;
        this.esCorrecta = pregunta.esCorrecta(indiceSeleccionado);
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public int getIndiceSeleccionado() {
        return indiceSeleccionado;
    }

    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    public String getTextoRespuestaUsuario() {
        if (indiceSeleccionado == -1) {
            return "No respondida";
        }
        return pregunta.getOpciones().get(indiceSeleccionado);
    }
}
