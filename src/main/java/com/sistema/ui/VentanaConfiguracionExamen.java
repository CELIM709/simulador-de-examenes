package com.sistema.ui;

import com.sistema.logica.BancoPreguntas;
import com.sistema.modelos.NivelDificultad;
import com.sistema.modelos.Usuario;
import java.awt.*;
import javax.swing.*;

public class VentanaConfiguracionExamen extends JDialog {

    private JTextField txtNombre, txtCedula;
    private JComboBox<String> cbTema;
    private JComboBox<NivelDificultad> cbNivel;
    private Usuario usuario;
    private String temaSeleccionado;
    private NivelDificultad nivelSeleccionado;
    private boolean cancelado = true;

    public VentanaConfiguracionExamen(JFrame parent, BancoPreguntas banco) {
        super(parent, "Configuración del Examen", true);
        setSize(350, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel(" Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel(" Cédula:"));
        txtCedula = new JTextField();
        add(txtCedula);

        add(new JLabel(" Seleccionar Tema:"));
        cbTema = new JComboBox<>();
        for (String tema : banco.obtenerTemasDisponibles()) {
            cbTema.addItem(tema);
        }
        add(cbTema);

        add(new JLabel(" Dificultad:"));
        cbNivel = new JComboBox<>(NivelDificultad.values());
        add(cbNivel);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar);

        JButton btnAceptar = new JButton("Iniciar");
        btnAceptar.addActionListener(e -> {
            if (txtNombre.getText().isEmpty() || txtCedula.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete sus datos personales.");
            } else {
                usuario = new Usuario(txtNombre.getText(), txtCedula.getText());
                temaSeleccionado = (String) cbTema.getSelectedItem();
                nivelSeleccionado = (NivelDificultad) cbNivel.getSelectedItem();
                cancelado = false;
                dispose();
            }
        });
        add(btnAceptar);
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
