package com.sistema.ui;

import com.sistema.logica.BancoPreguntas;
import com.sistema.modelos.NivelDificultad;
import com.sistema.modelos.Pregunta;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MenuPrincipal extends JFrame {

    private BancoPreguntas banco;

    private static final Color COLOR_FONDO_CLARO = new Color(248, 248, 255); // Blanco/Azul claro sutil
    private static final Color COLOR_TEXTO_OSCURO = new Color(51, 51, 51); // Gris muy oscuro
    private static final Color COLOR_PRINCIPAL = new Color(0, 123, 255); // Azul brillante
    private static final Color COLOR_ACENTO = new Color(40, 167, 69); // Verde 
    private static final Font FONT_TITULO = new Font("Arial", Font.BOLD, 28);
    private static final Font FONT_BOTON = new Font("Arial", Font.BOLD, 16);

    public MenuPrincipal() {

        this.banco = new BancoPreguntas();

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {

        }

        configurarVentana();

        inicializarComponentes();

        setLocationRelativeTo(null);
    }

    private void configurarVentana() {
        setTitle("Sistema de Evaluación - Menú Principal");
        setSize(450, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO_CLARO);
    }

    private void inicializarComponentes() {

        // Panel superior para el título
        JPanel pnlTitulo = new JPanel();
        pnlTitulo.setBackground(COLOR_FONDO_CLARO);
        pnlTitulo.setBorder(new EmptyBorder(30, 0, 20, 0));

        JLabel titulo = new JLabel("Panel de Control", SwingConstants.CENTER);
        titulo.setFont(FONT_TITULO);
        titulo.setForeground(COLOR_PRINCIPAL);
        pnlTitulo.add(titulo);
        add(pnlTitulo, BorderLayout.NORTH);

        JPanel pnlBotones = new JPanel();
        pnlBotones.setLayout(new BoxLayout(pnlBotones, BoxLayout.Y_AXIS));
        pnlBotones.setBackground(COLOR_FONDO_CLARO);
        pnlBotones.setBorder(new EmptyBorder(10, 50, 30, 50));

        JButton btnSimulacion = new JButton("Iniciar Examen");
        JButton btnVerBanco = new JButton("Ver Banco de Preguntas");
        JButton btnRespuestasUsuario = new JButton("Ver Respuestas por Usuario");
        JButton btnEstadisticas = new JButton("Ver Estadísticas de Desempeño");
        JButton btnHistorialArchivo = new JButton("Ver Historial (Archivo)");
        JButton btnSalir = new JButton("Salir");

        JButton[] botones = {btnSimulacion, btnVerBanco, btnRespuestasUsuario, btnEstadisticas, btnHistorialArchivo, btnSalir};

        for (JButton btn : botones) {
            btn.setFont(FONT_BOTON);
            btn.setForeground(Color.WHITE);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);

            if (btn == btnSimulacion) {
                btn.setBackground(COLOR_ACENTO);
            } else if (btn == btnSalir) {
                btn.setBackground(Color.RED.darker());
            } else {
                btn.setBackground(COLOR_PRINCIPAL);
            }
        }

        btnSimulacion.addActionListener(e -> {
            VentanaConfiguracionExamen config = new VentanaConfiguracionExamen(this, banco);
            config.setVisible(true);

            if (!config.isCancelado()) {
                String tema = config.getTemaSeleccionado();
                NivelDificultad nivel = config.getNivelSeleccionado();

                List<Pregunta> preguntas = banco.generarExamen(tema, nivel, 5);

                if (preguntas.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "No hay suficientes preguntas de " + tema + " en nivel " + nivel + ".", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    VentanaExamen examen = new VentanaExamen(config.getUsuario(), preguntas);
                    examen.setVisible(true);
                }
            }
        });

        btnVerBanco.addActionListener(e -> {
            VistaBancoPreguntas vistaBanco = new VistaBancoPreguntas(this, banco);
            vistaBanco.setVisible(true);
        });

        btnRespuestasUsuario.addActionListener(e -> {
            if (com.sistema.logica.GestorHistorial.getHistorialGlobal().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aún no hay exámenes realizados en esta sesión.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                VistaDetalleRespuestas vista = new VistaDetalleRespuestas(this);
                vista.setVisible(true);
            }
        });

        btnEstadisticas.addActionListener(e -> {
            if (com.sistema.logica.GestorHistorial.getHistorialGlobal().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay datos suficientes para generar estadísticas.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                VistaEstadisticas vistaEst = new VistaEstadisticas(this);
                vistaEst.setVisible(true);
            }
        });

        btnHistorialArchivo.addActionListener(e -> {
            VistaHistorialUsuario vista = new VistaHistorialUsuario(this);
            vista.setVisible(true);
        });
        btnSalir.addActionListener(e -> System.exit(0));

        pnlBotones.add(btnSimulacion);
        pnlBotones.add(Box.createVerticalStrut(20));
        pnlBotones.add(btnVerBanco);
        pnlBotones.add(Box.createVerticalStrut(20));
        pnlBotones.add(btnRespuestasUsuario);
        pnlBotones.add(Box.createVerticalStrut(20));
        pnlBotones.add(btnEstadisticas);
        pnlBotones.add(Box.createVerticalStrut(20));
        pnlBotones.add(btnHistorialArchivo);
        pnlBotones.add(Box.createVerticalStrut(20));
        pnlBotones.add(btnSalir);

        add(pnlBotones, BorderLayout.CENTER);
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            new MenuPrincipal().setVisible(true);
        });
    }
}
