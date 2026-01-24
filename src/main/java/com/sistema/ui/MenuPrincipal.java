package com.sistema.ui;

import com.sistema.logica.BancoPreguntas;
import com.sistema.modelos.NivelDificultad;
import com.sistema.modelos.Pregunta;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class MenuPrincipal extends JFrame {

    private BancoPreguntas banco;

    public MenuPrincipal() {

        this.banco = new BancoPreguntas();

        configurarVentana();

        inicializarComponentes();

        setLocationRelativeTo(null);
    }

    private void configurarVentana() {
        setTitle("Sistema de Evaluación - Menú Principal");
        setSize(450, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1, 10, 10));
    }

    private void inicializarComponentes() {

        JLabel titulo = new JLabel("Panel de Control", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        JButton btnSimulacion = new JButton("Iniciar Examen");
        JButton btnVerBanco = new JButton("Ver Banco de Preguntas");
        JButton btnRespuestasUsuario = new JButton("Ver Respuestas por Usuario");
        JButton btnEstadisticas = new JButton("Ver Estadísticas de Desempeño");
        JButton btnHistorialArchivo = new JButton("Ver Historial (Archivo)");
        JButton btnSalir = new JButton("Salir");

        btnSimulacion.addActionListener(e -> {
            VentanaConfiguracionExamen config = new VentanaConfiguracionExamen(this, banco);
            config.setVisible(true);

            if (!config.isCancelado()) {
                String tema = config.getTemaSeleccionado();
                NivelDificultad nivel = config.getNivelSeleccionado();

                List<Pregunta> preguntas = banco.generarExamen(tema, nivel, 5);

                if (preguntas.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "No hay suficientes preguntas de " + tema + " en nivel " + nivel + ".");
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
                JOptionPane.showMessageDialog(this, "Aún no hay exámenes realizados en esta sesión.");
            } else {
                VistaDetalleRespuestas vista = new VistaDetalleRespuestas(this);
                vista.setVisible(true);
            }
        });

        btnEstadisticas.addActionListener(e -> {
            if (com.sistema.logica.GestorHistorial.getHistorialGlobal().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay datos suficientes para generar estadísticas.");
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

        add(titulo);
        add(btnSimulacion);
        add(btnVerBanco);
        add(btnRespuestasUsuario);
        add(btnEstadisticas);
        add(btnHistorialArchivo);
        add(btnSalir);
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
