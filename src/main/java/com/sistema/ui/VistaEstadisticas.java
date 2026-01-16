package com.sistema.ui;

import com.sistema.logica.CalculadoraEstadisticas;
import com.sistema.logica.PersistenciaDatos;
import com.sistema.modelos.NivelDificultad;
import com.sistema.modelos.Resultado;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class VistaEstadisticas extends JDialog {

    private JComboBox<String> cbUsuario, cbTema, cbNivel;
    private JLabel lblPromedio, lblContador;
    private List<Resultado> datosHistoricos;

    public VistaEstadisticas(JFrame parent) {
        super(parent, "Estadísticas Históricas", true);
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // CARGA DE DATOS DESDE EL ARCHIVO .TXT
        datosHistoricos = PersistenciaDatos.recuperarResultadosHistoricos();

        initFiltros();
        initPantalla();
        actualizar();
    }

    private void initFiltros() {
        JPanel pnl = new JPanel(new GridLayout(4, 2, 5, 5));
        pnl.setBorder(BorderFactory.createTitledBorder("Filtros Históricos"));

        cbUsuario = new JComboBox<>();
        cbUsuario.addItem("Todos");
        datosHistoricos.stream().map(r -> r.getUsuario().getNombre()).distinct().forEach(cbUsuario::addItem);

        cbTema = new JComboBox<>();
        cbTema.addItem("Todos");
        datosHistoricos.stream().map(r -> r.getDetalles().get(0).getPregunta().getTema()).distinct().forEach(cbTema::addItem);

        cbNivel = new JComboBox<>();
        cbNivel.addItem("Todos");
        for (NivelDificultad n : NivelDificultad.values()) {
            cbNivel.addItem(n.toString());
        }

        pnl.add(new JLabel(" Usuario:"));
        pnl.add(cbUsuario);
        pnl.add(new JLabel(" Tema:"));
        pnl.add(cbTema);
        pnl.add(new JLabel(" Nivel:"));
        pnl.add(cbNivel);

        JButton btn = new JButton("Filtrar Historial");
        btn.addActionListener(e -> actualizar());
        pnl.add(new JLabel());
        pnl.add(btn);

        add(pnl, BorderLayout.NORTH);
    }

    private void initPantalla() {
        JPanel pnlCentro = new JPanel(new GridLayout(2, 1));
        lblPromedio = new JLabel("0.0%", SwingConstants.CENTER);
        lblPromedio.setFont(new Font("Arial", Font.BOLD, 40));
        lblContador = new JLabel("Exámenes: 0", SwingConstants.CENTER);

        pnlCentro.add(lblPromedio);
        pnlCentro.add(lblContador);
        add(pnlCentro, BorderLayout.CENTER);
    }

    private void actualizar() {
        List<Resultado> filtrados = CalculadoraEstadisticas.filtrar(
                datosHistoricos,
                cbUsuario.getSelectedItem().toString(),
                cbTema.getSelectedItem().toString(),
                cbNivel.getSelectedItem().toString()
        );

        double prom = CalculadoraEstadisticas.calcularPromedio(filtrados);
        lblPromedio.setText(String.format("%.1f%%", prom));
        lblContador.setText("Registros encontrados en archivo: " + filtrados.size());

        lblPromedio.setForeground(prom >= 70 ? new Color(0, 120, 0) : Color.RED);
    }
}
