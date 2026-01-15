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

        setTitle("Sistema de Evaluación");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JButton btnSimulacion = new JButton("Iniciar Simulación");
        btnSimulacion.addActionListener(e -> {
            VentanaLogin login = new VentanaLogin(this);
            login.setVisible(true);

            if (!login.isCancelado()) {
                // Generamos las preguntas aquí para pasarlas al constructor
                List<Pregunta> preguntas = banco.generarExamen("Java", NivelDificultad.BASICO, 5);

                // Pasamos el usuario Y la lista de preguntas
                VentanaExamen examen = new VentanaExamen(login.getUsuario(), preguntas);
                examen.setVisible(true);
            }
        });

        add(new JLabel("Bienvenido al Sistema", SwingConstants.CENTER));
        add(btnSimulacion);

        setLocationRelativeTo(null);
    }

    private void configurarVentana() {
        setTitle("Sistema de Evaluación - Menú Principal");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));
    }

    private void inicializarComponentes() {
        JLabel titulo = new JLabel("Panel de Control", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        JButton btnVerBanco = new JButton("Ver Banco de Preguntas");
        JButton btnSimulacion = new JButton("Iniciar Simulación de Examen");
        JButton btnEstadisticas = new JButton("Estadísticas de Desempeño");
        JButton btnSalir = new JButton("Salir");

        btnVerBanco.addActionListener(e -> {
            VistaBancoPreguntas vistaBanco = new VistaBancoPreguntas(this, banco);
            vistaBanco.setVisible(true);
        });

        btnSimulacion.addActionListener(e -> {
            VentanaLogin login = new VentanaLogin(this);
            login.setVisible(true);

            if (!login.isCancelado()) {
                List<Pregunta> preguntas = banco.generarExamen("Java", NivelDificultad.BASICO, 3);
                VentanaExamen examen = new VentanaExamen(login.getUsuario(), preguntas);
                examen.setVisible(true);
            }
        });

        btnEstadisticas.addActionListener(e -> {
            List<String> historial = PersistenciaDatos.leerHistorial();
            JTextArea area = new JTextArea(20, 40);
            for (String s : historial) {
                area.append(s + "\n");
            }
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Historial desde Archivo", JOptionPane.INFORMATION_MESSAGE);
        });

        btnSalir.addActionListener(e -> System.exit(0));

        add(titulo);
        add(btnVerBanco);
        add(btnSimulacion);
        add(btnEstadisticas);
        add(btnSalir);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MenuPrincipal().setVisible(true);
        });
    }
}
