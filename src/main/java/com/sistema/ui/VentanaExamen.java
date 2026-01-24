package com.sistema.ui;

import com.sistema.logica.Evaluador;
import com.sistema.modelos.Pregunta;
import com.sistema.modelos.RegistroRespuesta;
import com.sistema.modelos.Resultado;
import com.sistema.modelos.Usuario;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;

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

    private static final Color COLOR_FONDO_CLARO = Color.WHITE;
    private static final Color COLOR_FONDO_ENCABEZADO = new Color(240, 240, 240);
    private static final Color COLOR_TEXTO_OSCURO = Color.BLACK;
    private static final Color COLOR_TEXTO_ACENTO = new Color(0, 102, 204);

    public VentanaExamen(Usuario usuario, List<Pregunta> preguntas) {
        this.usuario = usuario;
        this.examenActual = preguntas;

        if (examenActual.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay preguntas para los criterios seleccionados.");
            this.dispose();
            return;
        }
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

        } catch (Exception ex) {
            System.err.println("No se pudo cargar el Look and Feel. Usando el por defecto.");
        }

        configurarVentana();
        initUI();
        iniciarTimer();
        mostrarPregunta();
    }

    private void configurarVentana() {
        setTitle("Simulación de Examen Real");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO_CLARO);
    }

    private void initUI() {
        // Panel Norte: Info del usuario y Timer
        JPanel pnlNorte = new JPanel(new GridLayout(1, 2));
        pnlNorte.setBackground(COLOR_FONDO_ENCABEZADO); // Fondo de encabezado gris claro

        lblUsuario = new JLabel(" Estudiante: " + usuario.getNombre());
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        lblUsuario.setForeground(COLOR_TEXTO_OSCURO); // Texto oscuro

        lblTimer = new JLabel("Tiempo: 60s ", SwingConstants.RIGHT);
        lblTimer.setFont(new Font("Monospaced", Font.BOLD, 20));
        lblTimer.setForeground(COLOR_TEXTO_ACENTO); // Timer azul

        pnlNorte.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 15, 10, 15),
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY) // Separador gris claro
        ));

        pnlNorte.add(lblUsuario);
        pnlNorte.add(lblTimer);
        add(pnlNorte, BorderLayout.NORTH);

        // Panel Central: Pregunta y Opciones
        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.setBackground(COLOR_FONDO_CLARO); // Fondo claro
        pnlCentro.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel pnlContenidoPregunta = new JPanel();
        pnlContenidoPregunta.setLayout(new BoxLayout(pnlContenidoPregunta, BoxLayout.Y_AXIS));
        pnlContenidoPregunta.setBackground(COLOR_FONDO_CLARO); // Fondo claro

        pnlContenidoPregunta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), // Borde gris claro
                        "Pregunta Actual",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Arial", Font.ITALIC, 12),
                        Color.DARK_GRAY
                ),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));

        lblEnunciado = new JLabel("Pregunta aquí");
        lblEnunciado.setFont(new Font("Arial", Font.BOLD, 16));
        lblEnunciado.setForeground(COLOR_TEXTO_OSCURO); // Texto oscuro
        lblEnunciado.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlContenidoPregunta.add(lblEnunciado);
        pnlContenidoPregunta.add(Box.createRigidArea(new Dimension(0, 30)));

        grupoOpciones = new ButtonGroup();
        for (int i = 0; i < 3; i++) {
            opciones[i] = new JRadioButton();
            opciones[i].setFont(new Font("SansSerif", Font.PLAIN, 14));
            opciones[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            opciones[i].setBackground(COLOR_FONDO_CLARO); // Fondo claro para los radio buttons
            opciones[i].setForeground(COLOR_TEXTO_OSCURO); // Texto oscuro para los radio buttons
            grupoOpciones.add(opciones[i]);
            pnlContenidoPregunta.add(opciones[i]);
            if (i < 2) {
                pnlContenidoPregunta.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        pnlCentro.add(pnlContenidoPregunta, BorderLayout.CENTER);
        add(pnlCentro, BorderLayout.CENTER);

        // Panel Sur: Botón Siguiente
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlSur.setBackground(COLOR_FONDO_CLARO); // Fondo claro
        pnlSur.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnSiguiente = new JButton("Siguiente Pregunta");
        btnSiguiente.setFont(new Font("Arial", Font.BOLD, 14));
        btnSiguiente.addActionListener(e -> validarYSiguiente());

        pnlSur.add(btnSiguiente);
        add(pnlSur, BorderLayout.SOUTH);
    }

    private void mostrarPregunta() {
        if (indicePregunta < examenActual.size()) {
            Pregunta p = examenActual.get(indicePregunta);
            lblEnunciado.setText("<html><div style='text-align: left; width: 550px;'>" + "¿Qué es la pregunta " + (indicePregunta + 1) + "? " + p.getEnunciado() + "</div></html>");

            List<String> opcionesLista = p.getOpciones();
            for (int i = 0; i < 3; i++) {
                opciones[i].setText("<html><div style='width: 500px;'>" + opcionesLista.get(i) + "</div></html>");
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

        if (seleccion == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una opción antes de continuar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pregunta pActual = examenActual.get(indicePregunta);
        RegistroRespuesta registro = new RegistroRespuesta(pActual, seleccion);
        respuestasDelUsuario.add(registro);

        if (registro.isEsCorrecta()) {
            aciertos++;
        }

        JOptionPane.showMessageDialog(this, "Retroalimentación: " + pActual.getRetroalimentacion());

        indicePregunta++;
        mostrarPregunta();
    }

    private void iniciarTimer() {
        timer = new Timer(1000, e -> {
            tiempoRestante--;
            lblTimer.setText("Tiempo: " + tiempoRestante + "s ");
            // Cambiar color del timer cuando queda poco tiempo (ej. menos de 10s)
            if (tiempoRestante <= 10) {
                lblTimer.setForeground(Color.RED.darker());
            } else {
                lblTimer.setForeground(COLOR_TEXTO_ACENTO);
            }

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

        com.sistema.logica.GestorHistorial.guardarResultado(resultado);

        com.sistema.logica.PersistenciaDatos.guardarResultado(resultado);

        JOptionPane.showMessageDialog(this, "Examen Finalizado.\nEstudiante: " + usuario.getNombre() + "\nNota: " + nota + "%");
        this.dispose();
    }
}
