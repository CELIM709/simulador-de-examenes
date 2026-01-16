package com.sistema.ui;

import com.sistema.logica.BancoPreguntas;
import com.sistema.modelos.NivelDificultad;
import com.sistema.modelos.Pregunta;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class VistaBancoPreguntas extends JDialog {

    private JTable tabla;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> cbTema, cbNivel;

    public VistaBancoPreguntas(JFrame parent, BancoPreguntas banco) {
        super(parent, "Gestión del Banco de Preguntas", true);
        setSize(1000, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // --- 1. PANEL DE FILTROS (NORTE) ---
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlFiltros.setBorder(BorderFactory.createTitledBorder("Filtros de búsqueda"));

        // Filtro de Tema
        pnlFiltros.add(new JLabel("Tema:"));
        cbTema = new JComboBox<>();
        cbTema.addItem("Todos");
        // Cargamos temas dinámicamente desde el banco
        for (String tema : banco.obtenerTemasDisponibles()) {
            cbTema.addItem(tema);
        }
        pnlFiltros.add(cbTema);

        // Filtro de Nivel
        pnlFiltros.add(new JLabel("Nivel:"));
        cbNivel = new JComboBox<>();
        cbNivel.addItem("Todos");
        for (NivelDificultad nivel : NivelDificultad.values()) {
            cbNivel.addItem(nivel.toString());
        }
        pnlFiltros.add(cbNivel);

        // Botón para aplicar filtro
        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(e -> aplicarFiltro());
        pnlFiltros.add(btnFiltrar);

        // --- 2. CONFIGURACIÓN DE TABLA (CENTRO) ---
        String[] columnas = {"Tema", "Nivel", "Enunciado de la Pregunta", "Respuesta Correcta"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cargarDatos(banco.getTodasLasPreguntas());

        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter); // Habilita el filtrado y ordenamiento

        // Estética de la tabla
        tabla.setRowHeight(35);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(500); // Enunciado más ancho

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabla.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabla.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        JScrollPane scroll = new JScrollPane(tabla);

        // --- 3. PANEL DE BOTONES (SUR) ---
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JButton btnAgregar = new JButton("Agregar Pregunta");
        btnAgregar.setBackground(new Color(40, 167, 69));
        btnAgregar.setForeground(Color.WHITE);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        pnlBotones.add(btnAgregar);
        pnlBotones.add(btnCerrar);

        // --- 4. ENSAMBLAJE FINAL ---
        add(pnlFiltros, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.SOUTH);
    }

    private void cargarDatos(List<Pregunta> preguntas) {
        modelo.setRowCount(0);
        for (Pregunta p : preguntas) {
            modelo.addRow(new Object[]{
                p.getTema(),
                p.getNivel(),
                p.getEnunciado(),
                p.getOpciones().get(0)
            });
        }
    }

    /**
     * Lógica de filtrado combinada
     */
    private void aplicarFiltro() {
        String tema = cbTema.getSelectedItem().toString();
        String nivel = cbNivel.getSelectedItem().toString();

        List<RowFilter<Object, Object>> filtros = new ArrayList<>();

        // Filtro por Tema (Columna 0)
        if (!tema.equals("Todos")) {
            filtros.add(RowFilter.regexFilter("^" + tema + "$", 0));
        }

        // Filtro por Nivel (Columna 1)
        if (!nivel.equals("Todos")) {
            filtros.add(RowFilter.regexFilter("^" + nivel + "$", 1));
        }

        // Aplicar la combinación de filtros (AND)
        if (filtros.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filtros));
        }
    }
}
