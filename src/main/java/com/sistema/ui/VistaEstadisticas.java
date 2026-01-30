package com.sistema.ui;

import com.sistema.logica.CalculadoraEstadisticas;
import com.sistema.logica.PersistenciaDatos;
import com.sistema.modelos.NivelDificultad;
import com.sistema.modelos.Resultado;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class VistaEstadisticas extends JDialog {

    private JComboBox<String> cbUsuario, cbTema, cbNivel;
    private JLabel lblPromedio, lblContador;
    private JPanel pnlGrafico;
    private List<Resultado> datosHistoricos;

    public VistaEstadisticas(JFrame parent) {
        super(parent, "Dashboard de Estadísticas Históricas", true);
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        datosHistoricos = PersistenciaDatos.recuperarResultadosHistoricos();

        initFiltros();
        initPantallaPrincipal();
        actualizar();
    }

    private void initFiltros() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnl.setBorder(BorderFactory.createTitledBorder("Filtros de Análisis"));

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

        pnl.add(new JLabel("Usuario:"));
        pnl.add(cbUsuario);
        pnl.add(new JLabel("Tema:"));
        pnl.add(cbTema);
        pnl.add(new JLabel("Nivel:"));
        pnl.add(cbNivel);

        JButton btn = new JButton("Actualizar Gráficos");
        btn.setBackground(new Color(0, 102, 204));
        btn.setForeground(Color.WHITE);
        btn.addActionListener(e -> actualizar());
        pnl.add(btn);

        add(pnl, BorderLayout.NORTH);
    }

    private void initPantallaPrincipal() {
        JPanel pnlIzquierdo = new JPanel(new GridLayout(4, 1, 10, 10));
        pnlIzquierdo.setPreferredSize(new Dimension(250, 0));
        pnlIzquierdo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblPromedio = new JLabel("0.0%", SwingConstants.CENTER);
        lblPromedio.setFont(new Font("Arial", Font.BOLD, 45));

        lblContador = new JLabel("Exámenes: 0", SwingConstants.CENTER);

        pnlIzquierdo.add(new JLabel("PROMEDIO FILTRADO", SwingConstants.CENTER));
        pnlIzquierdo.add(lblPromedio);
        pnlIzquierdo.add(lblContador);

        pnlGrafico = new JPanel(new BorderLayout());
        pnlGrafico.setBackground(Color.WHITE);
        pnlGrafico.setBorder(BorderFactory.createTitledBorder("Rendimiento por Tema"));

        add(pnlIzquierdo, BorderLayout.WEST);
        add(pnlGrafico, BorderLayout.CENTER);
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
        lblContador.setText("Registros: " + filtrados.size());
        lblPromedio.setForeground(prom >= 70 ? new Color(0, 120, 0) : Color.RED);

        generarGraficoBarras(filtrados);
    }

    private void generarGraficoBarras(List<Resultado> filtrados) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String userSel = cbUsuario.getSelectedItem().toString();
        String temaSel = cbTema.getSelectedItem().toString();
        String nivelSel = cbNivel.getSelectedItem().toString();

        String ejeX, serie;
        if (userSel.equals("Todos")) {
            ejeX = "Usuario";
            serie = temaSel.equals("Todos") ? "Tema" : "Nivel";
        } else if (temaSel.equals("Todos")) {
            ejeX = "Tema";
            serie = nivelSel.equals("Todos") ? "Nivel" : "Usuario";
        } else {
            ejeX = "Nivel";
            serie = "Usuario";
        }

        if (ejeX.equals("Nivel")) {
            dataset.addValue(0.0, userSel, "Básico");
            dataset.addValue(0.0, userSel, "Intermedio");
            dataset.addValue(0.0, userSel, "Avanzado");
        } else if (ejeX.equals("Tema")) {
            dataset.addValue(0.0, serie, "Java");
            dataset.addValue(0.0, serie, "POO");
            dataset.addValue(0.0, serie, "Estructuras");
        }

        Map<String, Map<String, Double>> agrupado = filtrados.stream()
                .collect(Collectors.groupingBy(
                        r -> obtenerValorDeResultado(r, ejeX),
                        Collectors.groupingBy(
                                r -> obtenerValorDeResultado(r, serie),
                                Collectors.averagingDouble(Resultado::getPuntaje)
                        )
                ));

        agrupado.forEach((valEjeX, mapSeries) -> {
            mapSeries.forEach((valSerie, promedio) -> {
                dataset.addValue(promedio, valSerie, valEjeX);
            });
        });

        JFreeChart chart = ChartFactory.createBarChart(
                "Análisis: " + serie + " por " + ejeX,
                ejeX, "Porcentaje (%)", dataset,
                PlotOrientation.VERTICAL, true, true, false
        );

        org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        org.jfree.chart.axis.NumberAxis rangeAxis = (org.jfree.chart.axis.NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0.0, 105.0);

        org.jfree.chart.renderer.category.BarRenderer renderer = (org.jfree.chart.renderer.category.BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setShadowVisible(false);

        renderer.setMaximumBarWidth(0.10);

        renderer.setDefaultItemLabelGenerator(new org.jfree.chart.labels.StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);

        pnlGrafico.removeAll();
        pnlGrafico.add(new ChartPanel(chart), BorderLayout.CENTER);
        pnlGrafico.revalidate();
        pnlGrafico.repaint();
    }

    private String obtenerValorDeResultado(Resultado r, String categoria) {
        switch (categoria) {
            case "Usuario":
                return r.getUsuario().getNombre();
            case "Tema":
                return r.getDetalles().get(0).getPregunta().getTema();
            case "Nivel":
                return r.getDetalles().get(0).getPregunta().getNivel().toString();
            default:
                return "General";
        }
    }

    private void personalizarGrafico(JFreeChart chart) {
        org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        org.jfree.chart.axis.NumberAxis rangeAxis = (org.jfree.chart.axis.NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0.0, 100.0);

        org.jfree.chart.renderer.category.BarRenderer renderer = (org.jfree.chart.renderer.category.BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setShadowVisible(false);

        renderer.setDefaultItemLabelGenerator(new org.jfree.chart.labels.StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);

        renderer.setSeriesPaint(0, new Color(79, 129, 189));
        renderer.setSeriesPaint(1, new Color(155, 187, 89));
        renderer.setSeriesPaint(2, new Color(192, 80, 77));
    }
}
