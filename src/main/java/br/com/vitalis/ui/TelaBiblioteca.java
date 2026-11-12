package br.com.vitalis.ui;

import br.com.vitalis.dao.BibliotecaDAO;
import br.com.vitalis.model.Biblioteca;
import javax.swing.*;
import java.awt.*;

/** RF04: Biblioteca de Consulta. */
public class TelaBiblioteca extends JFrame {

    public TelaBiblioteca() {
        setTitle("Vitalis - Biblioteca de Consulta");
        setSize(760, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JLabel titulo = new JLabel("Biblioteca de Consulta", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(12, 0, 4, 0));
        add(titulo, BorderLayout.NORTH);

        DefaultListModel<Biblioteca> modelo = new DefaultListModel<>();
        JList<Biblioteca> lista = new JList<>(modelo);
        lista.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JTextArea conteudo = new JTextArea();
        conteudo.setEditable(false);
        conteudo.setLineWrap(true);
        conteudo.setWrapStyleWord(true);
        conteudo.setFont(new Font("Serif", Font.PLAIN, 15));
        conteudo.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        for (Biblioteca b : new BibliotecaDAO().listar()) modelo.addElement(b);

        lista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Biblioteca sel = lista.getSelectedValue();
                if (sel != null) {
                    conteudo.setText(sel.getCategoria().toUpperCase() + "\n\n" + sel.getConteudo());
                    conteudo.setCaretPosition(0);
                }
            }
        });
        if (!modelo.isEmpty()) lista.setSelectedIndex(0);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(lista), new JScrollPane(conteudo));
        split.setDividerLocation(280);
        split.setBorder(BorderFactory.createEmptyBorder(8, 12, 12, 12));
        add(split, BorderLayout.CENTER);
    }
}