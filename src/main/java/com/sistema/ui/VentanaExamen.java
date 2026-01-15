package com.sistema.ui;

import com.sistema.logica.Evaluador;
import com.sistema.logica.PersistenciaDatos;
import com.sistema.modelos.Pregunta;
import com.sistema.modelos.RegistroRespuesta;
import com.sistema.modelos.Resultado;
import com.sistema.modelos.Usuario;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class VentanaExamen extends JFrame {

    private Usuario usuario;
    private List<Pregunta> examenActual;
    private List<RegistroRespuesta> respuestasDelUsuario = new ArrayList<>();

    private int indicePregunta = 0;
    private int aciertos = 0;
    private int tiempoRestante = 60; // 60 segundos
    private Timer timer;

    private JLabel lblEnunciado, lblTimer, lblUsuario;
    private JRadioButton[] opciones = new JRadioButton[3];
    private ButtonGroup grupoOpciones;

    public VentanaExamen(Usuario usuario, List<Pregunta> preguntas) {
        this.usuario = usuario;
        this.examenActual = preguntas;

        if (examenActual.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay preguntas para los criterios seleccionados.");
            this.dispose();
            return;
        }

        configurarVentana();
        initUI();
        iniciarTimer();
        mostrarPregunta();
    }

    private void configurarVentana() {
        setTitle("Simulación de Examen Real");
        setSize(600, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    private void initUI() {
        // Panel Norte: Info del usuario y Timer
        JPanel pnlNorte = new JPanel(new GridLayout(1, 2));
        lblUsuario = new JLabel(" Estudiante: " + usuario.getNombre());
        lblTimer = new JLabel("Tiempo: 60s ", SwingConstants.RIGHT);
        lblTimer.setFont(new Font("Monospaced", Font.BOLD, 18));
        lblTimer.setForeground(Color.RED);
        pnlNorte.add(lblUsuario);
        pnlNorte.add(lblTimer);
        add(pnlNorte, BorderLayout.NORTH);

        // Panel Central: Pregunta y Opciones
        JPanel pnlCentro = new JPanel();
        pnlCentro.setLayout(new BoxLayout(pnlCentro, BoxLayout.Y_AXIS));
        pnlCentro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblEnunciado = new JLabel("Pregunta aquí");
        lblEnunciado.setFont(new Font("Arial", Font.BOLD, 14));
        lblEnunciado.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlCentro.add(lblEnunciado);
        pnlCentro.add(Box.createRigidArea(new Dimension(0, 20)));

        grupoOpciones = new ButtonGroup();
        for (int i = 0; i < 3; i++) {
            opciones[i] = new JRadioButton();
            opciones[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            grupoOpciones.add(opciones[i]);
            pnlCentro.add(opciones[i]);
        }
        add(pnlCentro, BorderLayout.CENTER);

        // Panel Sur: Botón Siguiente
        JButton btnSiguiente = new JButton("Siguiente Pregunta");
        btnSiguiente.addActionListener(e -> validarYSiguiente());
        add(btnSiguiente, BorderLayout.SOUTH);
    }

    private void mostrarPregunta() {
        if (indicePregunta < examenActual.size()) {
            Pregunta p = examenActual.get(indicePregunta);
            lblEnunciado.setText("<html><div style='text-align: center;'>" + p.getEnunciado() + "</div></html>");

            List<String> opcionesLista = p.getOpciones();
            for (int i = 0; i < 3; i++) {
                opciones[i].setText(opcionesLista.get(i));
                opciones[i].setSelected(false);
            }
            grupoOpciones.clearSelection();
        } else {
            finalizarExamen();
        }
    }

    private void validarYSiguiente() {
        int seleccion = -1;
        for (int i = 0; i < 3; i++) {
            if (opciones[i].isSelected()) {
                seleccion = i;
                break;
            }
        }

        Pregunta pActual = examenActual.get(indicePregunta);
        RegistroRespuesta registro = new RegistroRespuesta(pActual, seleccion);
        respuestasDelUsuario.add(registro);

        if (registro.isEsCorrecta()) {
            aciertos++;
        }

        // Retroalimentación instantánea
        JOptionPane.showMessageDialog(this, "Retroalimentación: " + pActual.getRetroalimentacion());

        indicePregunta++;
        mostrarPregunta();
    }

    private void iniciarTimer() {
        timer = new Timer(1000, e -> {
            tiempoRestante--;
            lblTimer.setText("Tiempo: " + tiempoRestante + "s ");
            if (tiempoRestante <= 0) {
                ((Timer) e.getSource()).stop();
                JOptionPane.showMessageDialog(this, "¡Tiempo agotado!");
                finalizarExamen();
            }
        });
        timer.start();
    }

    private void finalizarExamen() {
        if (timer != null) {
            timer.stop();
        }

        double nota = Evaluador.calcularPuntaje(aciertos, examenActual.size());
        Resultado resultado = new Resultado(usuario, aciertos, examenActual.size(), nota, respuestasDelUsuario);

        // Guardar en archivo y en memoria
        PersistenciaDatos.guardarResultado(resultado);

        JOptionPane.showMessageDialog(this, "Examen Terminado\nAciertos: " + aciertos + "\nNota: " + nota + "%");
        this.dispose();
    }
}
