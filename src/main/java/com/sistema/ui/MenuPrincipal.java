package com.sistema.ui;

import com.sistema.logica.BancoPreguntas;
import com.sistema.logica.PersistenciaDatos;
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
        setSize(450, 550); // Ajuste de tamaño para los nuevos botones
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
            VentanaLogin login = new VentanaLogin(this);
            login.setVisible(true);

            if (!login.isCancelado()) {
                List<Pregunta> preguntas = banco.generarExamen("Java", NivelDificultad.BASICO, 5);
                if (preguntas.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay preguntas disponibles en el banco.");
                } else {
                    VentanaExamen examen = new VentanaExamen(login.getUsuario(), preguntas);
                    examen.setVisible(true);
                }
            }
        });

        btnVerBanco.addActionListener(e -> {
            VistaBancoPreguntas vistaBanco = new VistaBancoPreguntas(this, banco);
            vistaBanco.setVisible(true);
        });

        btnRespuestasUsuario.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funcionalidad: Ver Respuestas por Usuario (En desarrollo)");
        });

        btnEstadisticas.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funcionalidad: Ver Estadísticas de Desempeño (En desarrollo)");
        });

        btnHistorialArchivo.addActionListener(e -> {
            List<String> historial = PersistenciaDatos.leerHistorial();
            JTextArea area = new JTextArea(20, 40);
            for (String s : historial) {
                area.append(s + "\n");
            }
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Historial desde Archivo", JOptionPane.INFORMATION_MESSAGE);
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
