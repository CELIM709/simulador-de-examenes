package com.sistema.ui;

import com.sistema.modelos.NivelDificultad;
import com.sistema.modelos.Pregunta;
import java.awt.*;
import java.util.Arrays;
import javax.swing.*;

public class VentanaFormularioPregunta extends JDialog {

    private JTextField txtTema, txtEnunciado, txtOp1, txtOp2, txtOp3, txtRetro;
    private JComboBox<NivelDificultad> cbNivel;
    private JComboBox<Integer> cbCorrecta;
    private Pregunta nuevaPregunta;
    private boolean guardado = false;

    public VentanaFormularioPregunta(JDialog parent) {
        super(parent, "Nueva Pregunta", true);
        setSize(400, 450);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(9, 2, 10, 10));

        add(new JLabel(" Tema:"));
        txtTema = new JTextField();
        add(txtTema);

        add(new JLabel(" Nivel:"));
        cbNivel = new JComboBox<>(NivelDificultad.values());
        add(cbNivel);

        add(new JLabel(" Enunciado:"));
        txtEnunciado = new JTextField();
        add(txtEnunciado);

        add(new JLabel(" Opción 1:"));
        txtOp1 = new JTextField();
        add(txtOp1);

        add(new JLabel(" Opción 2:"));
        txtOp2 = new JTextField();
        add(txtOp2);

        add(new JLabel(" Opción 3:"));
        txtOp3 = new JTextField();
        add(txtOp3);

        add(new JLabel(" Índice Correcta (0-2):"));
        cbCorrecta = new JComboBox<>(new Integer[]{0, 1, 2});
        add(cbCorrecta);

        add(new JLabel(" Retroalimentación:"));
        txtRetro = new JTextField();
        add(txtRetro);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            if (validar()) {
                nuevaPregunta = new Pregunta(
                        txtEnunciado.getText(),
                        Arrays.asList(txtOp1.getText(), txtOp2.getText(), txtOp3.getText()),
                        (int) cbCorrecta.getSelectedItem(),
                        txtTema.getText(),
                        (NivelDificultad) cbNivel.getSelectedItem(),
                        txtRetro.getText()
                );
                guardado = true;
                dispose();
            }
        });
        add(btnGuardar);
    }

    private boolean validar() {
        return !txtTema.getText().isEmpty() && !txtEnunciado.getText().isEmpty() && !txtOp1.getText().isEmpty();
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Pregunta getPregunta() {
        return nuevaPregunta;
    }
}
