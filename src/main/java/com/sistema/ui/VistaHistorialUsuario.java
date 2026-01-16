package com.sistema.ui;

import com.sistema.logica.PersistenciaDatos;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VistaHistorialUsuario extends JDialog {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextArea areaDetalle;
    private List<String> detallesCompletos = new ArrayList<>();

    public VistaHistorialUsuario(JFrame parent) {
        super(parent, "Historial de Evaluaciones Guardadas", true);
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new String[]{"Fecha y Hora", "Usuario / Cédula", "Puntaje"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(30);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        areaDetalle = new JTextArea();
        areaDetalle.setEditable(false);
        areaDetalle.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaDetalle.setBackground(Color.WHITE);
        areaDetalle.setForeground(Color.BLACK); // Asegura que el texto sea negro
        areaDetalle.setMargin(new Insets(10, 10, 10, 10));

        procesarArchivo();

        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (!e.getValueIsAdjusting() && fila != -1) {
                areaDetalle.setText(detallesCompletos.get(fila));
                areaDetalle.setCaretPosition(0);
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(tabla), new JScrollPane(areaDetalle));
        splitPane.setDividerLocation(200);

        add(splitPane, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSur.add(btnCerrar);
        add(pnlSur, BorderLayout.SOUTH);
    }

    private void procesarArchivo() {
        List<String> lineas = PersistenciaDatos.leerHistorial();
        StringBuilder bloqueDetalle = new StringBuilder();
        String usuario = "", puntaje = "", fecha = "";

        for (String linea : lineas) {
            if (linea.startsWith("----------------")) {
                modelo.addRow(new Object[]{fecha, usuario, puntaje});
                detallesCompletos.add(bloqueDetalle.toString());
                bloqueDetalle = new StringBuilder();
            } else {
                bloqueDetalle.append(linea).append("\n");
                if (linea.startsWith("USUARIO:")) {
                    usuario = linea.replace("USUARIO:", "");
                }
                if (linea.startsWith("PUNTAJE:")) {
                    puntaje = linea.replace("PUNTAJE:", "");
                }
                if (linea.startsWith("FECHA:")) {
                    fecha = linea.replace("FECHA:", "");
                    if (fecha.length() > 16) {
                        fecha = fecha.substring(0, 16).replace("T", " ");
                    }
                }
            }
        }
    }
}
