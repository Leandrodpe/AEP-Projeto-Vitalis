package br.com.vitalis.ui;

import br.com.vitalis.dao.UsuarioDAO;
import br.com.vitalis.model.*;
import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {

    private final JTextField email = new JTextField(18);
    private final JPasswordField senha = new JPasswordField(18);
    private final UsuarioDAO dao = new UsuarioDAO();

    public TelaLogin() {
        setTitle("Vitalis - Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 8, 8));

        JLabel titulo = new JLabel("PROJETO VITALIS", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titulo);

        JPanel p1 = new JPanel(); p1.add(new JLabel("E-mail:")); p1.add(email); add(p1);
        JPanel p2 = new JPanel(); p2.add(new JLabel("Senha:")); p2.add(senha); add(p2);

        JPanel p3 = new JPanel();
        JButton entrar = new JButton("Entrar");
        JButton cadastrar = new JButton("Criar conta");
        JButton biblioteca = new JButton("Biblioteca");
        p3.add(entrar); p3.add(cadastrar); p3.add(biblioteca); add(p3);

        entrar.addActionListener(e -> login());
        cadastrar.addActionListener(e -> cadastrar());
        biblioteca.addActionListener(e -> new TelaBiblioteca().setVisible(true));
        getRootPane().setDefaultButton(entrar);
    }

    private void login() {
        var u = dao.autenticar(email.getText().trim(), new String(senha.getPassword()));
        if (u.isEmpty()) {
            JOptionPane.showMessageDialog(this, "E-mail ou senha inválidos.");
            return;
        }
        dispose();
        if (u.get() instanceof Administrador admin) new TelaAdmin(admin).setVisible(true);
        else new TelaJogo((Jogador) u.get()).setVisible(true);
    }

    private void cadastrar() {
        try {
            String nome = JOptionPane.showInputDialog(this, "Nome:");
            String mail = JOptionPane.showInputDialog(this, "E-mail:");
            int idade = Integer.parseInt(JOptionPane.showInputDialog(this, "Idade:"));
            String pass = JOptionPane.showInputDialog(this, "Senha:");
            dao.inserir(new Jogador(nome, mail, idade, pass));
            JOptionPane.showMessageDialog(this, "Conta criada!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}