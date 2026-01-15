package com.sistema.ui;

import com.sistema.logica.GestorHistorial;
import com.sistema.modelos.Resultado;
import javax.swing.*;

public class VistaHistorialUsuario extends JDialog {

    public VistaHistorialUsuario(JFrame parent) {
        super(parent, "Historial", true);
        setSize(500, 400);

        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (Resultado res : GestorHistorial.getHistorialGlobal()) {
            modelo.addElement("Usuario: " + res.getUsuario().getNombre() + " - Nota: " + res.getPuntaje());
        }

        add(new JScrollPane(new JList<>(modelo)));
    }
}
