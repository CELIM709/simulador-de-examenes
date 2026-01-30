package com.sistema.ui;

import com.sistema.logica.GestorHistorial;
import com.sistema.modelos.Pregunta;
import com.sistema.modelos.RegistroRespuesta;
import com.sistema.modelos.Resultado;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VistaDetalleRespuestas extends JDialog {

    private JTable tablaIntentos;
    private DefaultTableModel modeloTabla;
    private JTextArea areaDetalle;

    public VistaDetalleRespuestas(JFrame parent) {
        super(parent, "Registro Detallado de Respuestas por Usuario", true);
        setSize(950, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(new String[]{"Fecha", "Usuario", "Cédula", "Aciertos", "Puntaje"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaIntentos = new JTable(modeloTabla);
        tablaIntentos.setRowHeight(30);
        tablaIntentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaIntentos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        areaDetalle = new JTextArea();
        areaDetalle.setEditable(false);
        areaDetalle.setFont(new Font("Consolas", Font.PLAIN, 14));

        areaDetalle.setBackground(Color.WHITE);
        areaDetalle.setForeground(Color.BLACK);
        areaDetalle.setCaretColor(Color.BLACK);

        areaDetalle.setMargin(new Insets(15, 15, 15, 15));
        areaDetalle.setLineWrap(true);
        areaDetalle.setWrapStyleWord(true);

        cargarIntentos();

        tablaIntentos.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaIntentos.getSelectedRow();
            if (!e.getValueIsAdjusting() && fila != -1) {
                mostrarDetallePreguntas(fila);
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tablaIntentos);
        JScrollPane scrollTexto = new JScrollPane(areaDetalle);
        scrollTexto.setBorder(BorderFactory.createTitledBorder("Detalle del Examen seleccionado"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollTabla, scrollTexto);
        splitPane.setDividerLocation(200);

        add(splitPane, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setPreferredSize(new Dimension(100, 35));
        btnCerrar.addActionListener(e -> dispose());
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSur.add(btnCerrar);
        add(pnlSur, BorderLayout.SOUTH);
    }

    private void cargarIntentos() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Resultado res : GestorHistorial.getHistorialGlobal()) {
            modeloTabla.addRow(new Object[]{
                res.getFecha().format(dtf),
                res.getUsuario().getNombre(),
                res.getUsuario().getCedula(),
                res.getAciertos() + " / " + res.getTotalPreguntas(),
                String.format("%.1f%%", res.getPuntaje())
            });
        }
    }

    private void mostrarDetallePreguntas(int indice) {
        Resultado res = GestorHistorial.getHistorialGlobal().get(indice);
        StringBuilder sb = new StringBuilder();
        sb.append("ESTUDIANTE: ").append(res.getUsuario().getNombre().toUpperCase()).append("\n");
        sb.append("CÉDULA: ").append(res.getUsuario().getCedula()).append("\n");
        sb.append("PUNTAJE OBTENIDO: ").append(String.format("%.1f%%", res.getPuntaje())).append("\n");
        sb.append("================================================================================\n\n");

        int i = 1;
        for (RegistroRespuesta reg : res.getDetalles()) {
            Pregunta p = reg.getPregunta(); // Obtenemos la pregunta original
            sb.append("PREGUNTA ").append(i).append(": ").append(p.getEnunciado()).append("\n");

            if (reg.isEsCorrecta()) {
                sb.append("RESULTADO: [CORRECTA] ✔\n");
            } else {
                sb.append("RESULTADO: [INCORRECTA] ✘\n");
                sb.append("RESPUESTA DADA: ").append(reg.getTextoRespuestaUsuario()).append("\n");

                // --- AQUÍ ESTABA EL ERROR: Cambiamos get(0) por el índice real ---
                String textoCorrecto = p.getOpciones().get(p.getIndiceCorrecto());
                sb.append("RESPUESTA CORRECTA: ").append(textoCorrecto).append("\n");
            }

            sb.append("RETROALIMENTACIÓN: ").append(p.getRetroalimentacion()).append("\n");
            sb.append("--------------------------------------------------------------------------------\n\n");
            i++;
        }

        areaDetalle.setText(sb.toString());
        areaDetalle.setCaretPosition(0);
    }
}
