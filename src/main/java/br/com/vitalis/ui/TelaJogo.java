package br.com.vitalis.ui;

import br.com.vitalis.JogoService;
import br.com.vitalis.model.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class TelaJogo extends JFrame {

    private final JogoService service;
    private final JLabel topo = new JLabel();
    private final JLabel imagem = new JLabel("", SwingConstants.CENTER);
    private final JTextArea texto = new JTextArea();
    private final JPanel opcoes = new JPanel(new GridLayout(0, 1, 6, 6));

    public TelaJogo(Jogador jogador) {
        this.service = new JogoService(jogador);
        setTitle("Vitalis - " + jogador.getNome());
        setSize(900, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        topo.setFont(new Font("SansSerif", Font.BOLD, 15));
        topo.setBorder(BorderFactory.createEmptyBorder(10, 12, 0, 12));
        add(topo, BorderLayout.NORTH);

        imagem.setPreferredSize(new Dimension(860, 300));
        imagem.setOpaque(true);
        imagem.setBackground(new Color(235, 240, 245));
        imagem.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 220)));
        add(imagem, BorderLayout.CENTER);

        JPanel inferior = new JPanel(new BorderLayout(6, 6));

        texto.setEditable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setFont(new Font("Serif", Font.PLAIN, 16));
        texto.setRows(6);
        texto.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        inferior.add(new JScrollPane(texto), BorderLayout.CENTER);

        opcoes.setBorder(BorderFactory.createEmptyBorder(6, 12, 12, 12));
        inferior.add(opcoes, BorderLayout.SOUTH);

        add(inferior, BorderLayout.SOUTH);

        carregar();
    }

    private void carregar() {
        var fase = service.faseAtual();
        if (fase.isEmpty()) { concluir(); return; }

        Cenario cen = fase.get();
        topo.setText("Fase " + cen.getOrdemFase() + " de 3 - " + cen.getTitulo()
                     + "   |   Pontos: " + service.getPontuacao());
        texto.setText(cen.getNarrativa());
        texto.setCaretPosition(0);
        carregarImagem(cen.getImagem());

        opcoes.removeAll();
        char letra = 'A';
        for (Acao a : cen.getAcoes()) {
            JButton b = new JButton(letra++ + ") " + a.getTexto());
            b.setHorizontalAlignment(SwingConstants.LEFT);
            b.addActionListener(e -> escolher(a, cen));
            opcoes.add(b);
        }
        opcoes.revalidate();
        opcoes.repaint();
    }

    /** RF02: carrega a imagem de apoio da fase a partir de /imagens. */
    private void carregarImagem(String nome) {
        if (nome == null || nome.isBlank()) {
            imagem.setIcon(null);
            imagem.setText("Imagem de apoio da fase");
            return;
        }

        File arquivo = new File("imagens/" + nome);
        if (!arquivo.exists()) {
            imagem.setIcon(null);
            imagem.setText("Imagem não encontrada: imagens/" + nome);
            return;
        }

        ImageIcon original = new ImageIcon(arquivo.getAbsolutePath());
        int largura = imagem.getPreferredSize().width;
        int altura = imagem.getPreferredSize().height;

        Image escalada = original.getImage().getScaledInstance(
                largura, altura, Image.SCALE_SMOOTH);

        imagem.setIcon(new ImageIcon(escalada));
        imagem.setText("");
    }

    private void escolher(Acao a, Cenario cen) {
        Object[] r = service.escolher(a, cen);
        boolean correta = (boolean) r[0];
        String msg = (String) r[1];
        boolean ultima = (boolean) r[2];

        if (!correta) {
            JOptionPane.showMessageDialog(this, msg, "GAME OVER EDUCATIVO",
                                          JOptionPane.ERROR_MESSAGE);
            service.reiniciar();
            carregar();
            return;
        }

        JOptionPane.showMessageDialog(this, msg, "Ação bem-sucedida",
                                      JOptionPane.INFORMATION_MESSAGE);
        if (ultima) concluir(); else carregar();
    }

    private void concluir() {
        var certOpt = service.emitirCertificado();
        if (certOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Você concluiu as fases, mas houve erro crítico na partida.\n"
                + "O certificado só é emitido em uma conclusão sem erros.",
                "Certificado não emitido", JOptionPane.WARNING_MESSAGE);
            dispose();
            return;
        }

        Certificado cert = certOpt.get();
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("certificado_vitalis.pdf"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            service.gerarPdf(cert, fc.getSelectedFile().toPath());
            JOptionPane.showMessageDialog(this,
                "Parabéns! " + service.getPontuacao() + " pontos.\nCertificado salvo em:\n"
                + fc.getSelectedFile().getAbsolutePath());
        }
        dispose();
    }
}