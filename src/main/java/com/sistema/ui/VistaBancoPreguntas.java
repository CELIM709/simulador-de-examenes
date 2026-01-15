package com.sistema.ui;

import com.sistema.logica.BancoPreguntas;
import com.sistema.modelos.Pregunta;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VistaBancoPreguntas extends JDialog {

    public VistaBancoPreguntas(JFrame parent, BancoPreguntas banco) {
        super(parent, "Banco de Preguntas Completo", true);
        setSize(800, 400);
        setLocationRelativeTo(parent);

        String[] columnas = {"Tema", "Nivel", "Enunciado", "Respuesta Correcta"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        List<Pregunta> lista = banco.getTodasLasPreguntas();
        for (Pregunta p : lista) {
            Object[] fila = {
                p.getTema(),
                p.getNivel(),
                p.getEnunciado(),
                p.getOpciones().get(0) // Muestra la primera o puedes lógica para la correcta
            };
            modelo.addRow(fila);
        }

        JTable tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        setLayout(new BorderLayout());
        add(new JLabel("Listado de Preguntas en Memoria", SwingConstants.CENTER), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        add(btnCerrar, BorderLayout.SOUTH);
    }
}
