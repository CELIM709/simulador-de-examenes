package com.sistema.ui;

import com.sistema.logica.BancoPreguntas;
import com.sistema.modelos.NivelDificultad;
import com.sistema.modelos.Usuario;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VentanaConfiguracionExamen extends JDialog {

    private JTextField txtNombre, txtCedula;
    private JComboBox<String> cbTema;
    private JComboBox<NivelDificultad> cbNivel;
    private Usuario usuario;
    private String temaSeleccionado;
    private NivelDificultad nivelSeleccionado;
    private boolean cancelado = true;

    private static final Color COLOR_FONDO_CLARO = Color.WHITE;
    private static final Color COLOR_TEXTO_OSCURO = Color.BLACK;
    private static final Color COLOR_BOTON_PRINCIPAL = new Color(0, 102, 204);
    private static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 12);
    private static final Font FONT_BOTON = new Font("Arial", Font.BOLD, 14);

    public VentanaConfiguracionExamen(JFrame parent, BancoPreguntas banco) {
        super(parent, "Configuración del Examen", true);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {

        }

        setSize(400, 320);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(COLOR_FONDO_CLARO);
        setLayout(new BorderLayout());

        JPanel pnlFormulario = new JPanel(new GridLayout(4, 2, 15, 15));
        pnlFormulario.setBackground(COLOR_FONDO_CLARO);
        pnlFormulario.setBorder(new EmptyBorder(30, 30, 15, 30));

        JLabel lblNombre = new JLabel(" Nombre:");
        lblNombre.setFont(FONT_LABEL);
        lblNombre.setForeground(COLOR_TEXTO_OSCURO);
        pnlFormulario.add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pnlFormulario.add(txtNombre);

        JLabel lblCedula = new JLabel(" Cédula:");
        lblCedula.setFont(FONT_LABEL);
        lblCedula.setForeground(COLOR_TEXTO_OSCURO);
        pnlFormulario.add(lblCedula);
        txtCedula = new JTextField();
        txtCedula.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pnlFormulario.add(txtCedula);

        JLabel lblTema = new JLabel(" Seleccionar Tema:");
        lblTema.setFont(FONT_LABEL);
        lblTema.setForeground(COLOR_TEXTO_OSCURO);
        pnlFormulario.add(lblTema);
        cbTema = new JComboBox<>();
        cbTema.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cbTema.setBackground(Color.WHITE);
        for (String tema : banco.obtenerTemasDisponibles()) {
            cbTema.addItem(tema);
        }
        pnlFormulario.add(cbTema);

        JLabel lblNivel = new JLabel(" Dificultad:");
        lblNivel.setFont(FONT_LABEL);
        lblNivel.setForeground(COLOR_TEXTO_OSCURO);
        pnlFormulario.add(lblNivel);
        cbNivel = new JComboBox<>(NivelDificultad.values());
        cbNivel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cbNivel.setBackground(Color.WHITE);
        pnlFormulario.add(cbNivel);

        add(pnlFormulario, BorderLayout.CENTER);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlBotones.setBackground(COLOR_FONDO_CLARO);
        pnlBotones.setBorder(new EmptyBorder(0, 30, 20, 30));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(FONT_BOTON);
        btnCancelar.setBackground(Color.LIGHT_GRAY);
        btnCancelar.setForeground(COLOR_TEXTO_OSCURO);
        btnCancelar.addActionListener(e -> dispose());
        pnlBotones.add(btnCancelar);

        JButton btnAceptar = new JButton("Iniciar");
        btnAceptar.setFont(FONT_BOTON);
        btnAceptar.setBackground(COLOR_BOTON_PRINCIPAL);
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.addActionListener(e -> {
            if (txtNombre.getText().isEmpty() || txtCedula.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete sus datos personales.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                usuario = new Usuario(txtNombre.getText(), txtCedula.getText());
                temaSeleccionado = (String) cbTema.getSelectedItem();
                nivelSeleccionado = (NivelDificultad) cbNivel.getSelectedItem();
                cancelado = false;
                dispose();
            }
        });
        pnlBotones.add(btnAceptar);

        add(pnlBotones, BorderLayout.SOUTH);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getTemaSeleccionado() {
        return temaSeleccionado;
    }

    public NivelDificultad getNivelSeleccionado() {
        return nivelSeleccionado;
    }

    public boolean isCancelado() {
        return cancelado;
    }
}
