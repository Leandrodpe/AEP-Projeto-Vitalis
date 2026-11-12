package br.com.vitalis.ui;

import br.com.vitalis.dao.*;
import br.com.vitalis.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaAdmin extends JFrame {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final CenarioDAO cenarioDAO = new CenarioDAO();
    private final BibliotecaDAO bibliotecaDAO = new BibliotecaDAO();

    private final DefaultTableModel mCen = new DefaultTableModel(
        new String[]{"ID", "Ordem", "Título"}, 0);
    private final DefaultTableModel mUsu = new DefaultTableModel(
        new String[]{"ID", "Nome", "E-mail", "Tipo"}, 0);
    private final DefaultTableModel mBib = new DefaultTableModel(
        new String[]{"ID", "Título", "Categoria"}, 0);

    public TelaAdmin(Administrador admin) {
        setTitle("Vitalis - Administração");
        setSize(820, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Cenários", painelCenarios());
        abas.addTab("Usuários", painelUsuarios());
        abas.addTab("Biblioteca", painelBiblioteca());
        add(abas);

        recarregarCenarios();
        recarregarUsuarios();
        recarregarBiblioteca();
    }

    private JPanel painelCenarios() {
        JPanel p = new JPanel(new BorderLayout());
        JTable t = new JTable(mCen);
        p.add(new JScrollPane(t), BorderLayout.CENTER);

        JPanel b = new JPanel();
        JButton novo = new JButton("Novo");
        JButton editar = new JButton("Editar");
        JButton excluir = new JButton("Excluir");
        b.add(novo); b.add(editar); b.add(excluir);
        p.add(b, BorderLayout.SOUTH);

        novo.addActionListener(e -> {
            try {
                String tit = JOptionPane.showInputDialog(this, "Título:");
                String nar = JOptionPane.showInputDialog(this, "Narrativa:");
                int ord = Integer.parseInt(JOptionPane.showInputDialog(this, "Ordem:"));
                String img = JOptionPane.showInputDialog(this, "Imagem (arquivo em /imagens):");
                cenarioDAO.inserir(new Cenario(tit, nar, ord, img));
                recarregarCenarios();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        editar.addActionListener(e -> {
            int linha = t.getSelectedRow();
            if (linha < 0) return;
            int id = (int) mCen.getValueAt(linha, 0);
            cenarioDAO.buscar(id).ifPresent(c -> {
                String novoTit = JOptionPane.showInputDialog(this, "Título:", c.getTitulo());
                if (novoTit != null && !novoTit.isBlank()) {
                    c.setTitulo(novoTit);
                    cenarioDAO.atualizar(c);
                    recarregarCenarios();
                }
            });
        });

        excluir.addActionListener(e -> {
            int linha = t.getSelectedRow();
            if (linha < 0) return;
            cenarioDAO.deletar((int) mCen.getValueAt(linha, 0));
            recarregarCenarios();
        });

        return p;
    }

    private JPanel painelUsuarios() {
        JPanel p = new JPanel(new BorderLayout());
        JTable t = new JTable(mUsu);
        p.add(new JScrollPane(t), BorderLayout.CENTER);

        JPanel b = new JPanel();
        JButton excluir = new JButton("Excluir usuário");
        b.add(excluir);
        p.add(b, BorderLayout.SOUTH);

        excluir.addActionListener(e -> {
            int linha = t.getSelectedRow();
            if (linha < 0) return;
            usuarioDAO.deletar((int) mUsu.getValueAt(linha, 0));
            recarregarUsuarios();
        });

        return p;
    }

    private JPanel painelBiblioteca() {
        JPanel p = new JPanel(new BorderLayout());
        JTable t = new JTable(mBib);
        p.add(new JScrollPane(t), BorderLayout.CENTER);

        JPanel b = new JPanel();
        JButton novo = new JButton("Novo guia");
        JButton excluir = new JButton("Excluir");
        b.add(novo); b.add(excluir);
        p.add(b, BorderLayout.SOUTH);

        novo.addActionListener(e -> {
            String tit = JOptionPane.showInputDialog(this, "Título:");
            String cat = JOptionPane.showInputDialog(this, "Categoria:");
            String cont = JOptionPane.showInputDialog(this, "Conteúdo:");
            bibliotecaDAO.inserir(new Biblioteca(tit, cont, cat));
            recarregarBiblioteca();
        });

        excluir.addActionListener(e -> {
            int linha = t.getSelectedRow();
            if (linha < 0) return;
            bibliotecaDAO.deletar((int) mBib.getValueAt(linha, 0));
            recarregarBiblioteca();
        });

        return p;
    }

    private void recarregarCenarios() {
        mCen.setRowCount(0);
        for (Cenario c : cenarioDAO.listar())
            mCen.addRow(new Object[]{c.getId(), c.getOrdemFase(), c.getTitulo()});
    }

    private void recarregarUsuarios() {
        mUsu.setRowCount(0);
        for (Usuario u : usuarioDAO.listar())
            mUsu.addRow(new Object[]{u.getId(), u.getNome(), u.getEmail(), u.getTipo()});
    }

    private void recarregarBiblioteca() {
        mBib.setRowCount(0);
        for (Biblioteca b : bibliotecaDAO.listar())
            mBib.addRow(new Object[]{b.getId(), b.getTitulo(), b.getCategoria()});
    }
}