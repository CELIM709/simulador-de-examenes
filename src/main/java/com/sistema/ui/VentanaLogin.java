package com.sistema.ui;

import com.sistema.modelos.Usuario;
import java.awt.*;
import javax.swing.*;

public class VentanaLogin extends JDialog {

    private JTextField txtNombre = new JTextField();
    private JTextField txtCedula = new JTextField();
    private Usuario usuarioRegistrado;
    private boolean cancelado = true;

    public VentanaLogin(Frame parent) {
        super(parent, "Registro de Usuario", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel(" Nombre:"));
        add(txtNombre);
        add(new JLabel(" Cédula:"));
        add(txtCedula);

        JButton btnAceptar = new JButton("Comenzar");
        btnAceptar.addActionListener(e -> {
            if (!txtNombre.getText().isEmpty() && !txtCedula.getText().isEmpty()) {
                usuarioRegistrado = new Usuario(txtNombre.getText(), txtCedula.getText());
                cancelado = false;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
            }
        });
        add(btnAceptar);
    }

    public Usuario getUsuario() {
        return usuarioRegistrado;
    }

    public boolean isCancelado() {
        return cancelado;
    }
}
