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
        areaDetalle.setForeground(Color.BLACK);
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

        // --- PANEL DE BOTONES ---
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnExportar = new JButton("Exportar Reporte (PDF/TXT)");
        btnExportar.setBackground(new Color(0, 102, 204));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.addActionListener(e -> accionExportar());

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        pnlSur.add(btnExportar);
        pnlSur.add(btnCerrar);
        add(pnlSur, BorderLayout.SOUTH);
    }

    private void accionExportar() {
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte Agrupado en PDF");
        fileChooser.setSelectedFile(new java.io.File("Reporte_Estudiantes.pdf"));

        if (fileChooser.showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
            try {
                // LLAMADA AL NUEVO MÉTODO CON ITEXT
                PersistenciaDatos.exportarReporteAgrupadoPDF(fileChooser.getSelectedFile());

                javax.swing.JOptionPane.showMessageDialog(this, "¡PDF generado con éxito!");

                // Opcional: Abrir el archivo automáticamente
                java.awt.Desktop.getDesktop().open(fileChooser.getSelectedFile());

            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Error al crear el PDF: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
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

/*package com.sistema.ui;

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
 */
