package br.com.vitalis;

import br.com.vitalis.dao.*;
import br.com.vitalis.model.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.Optional;

public class JogoService {

    private static final int PONTOS = 100;
    private final CenarioDAO cenarioDAO = new CenarioDAO();
    private final ProgressoDAO progressoDAO = new ProgressoDAO();
    private final CertificadoDAO certificadoDAO = new CertificadoDAO();
    private final Jogador jogador;

    private int fase = 1, pontuacao = 0;
    private boolean houveErroCritico = false;

    public JogoService(Jogador jogador) { this.jogador = jogador; }

    public Optional<Cenario> faseAtual() { return cenarioDAO.buscarPorOrdem(fase); }

    /** Retorna [correta, mensagem, ultimaFase]. */
    public Object[] escolher(Acao acao, Cenario cen) {
        Progresso p = progressoDAO.inserir(new Progresso(jogador.getId(), cen.getId()));

        if (acao.isCorreta()) {
            pontuacao += PONTOS;
            p.acertar(PONTOS);
            progressoDAO.atualizar(p);
            fase++;
            boolean ultima = cenarioDAO.buscarPorOrdem(fase).isEmpty();
            return new Object[]{true, acao.executar(), ultima};
        }

        p.errar();
        progressoDAO.atualizar(p);
        houveErroCritico = true;
        return new Object[]{false, acao.feedback(), false};
    }

    /** RF05: certificado só é emitido se concluir sem erro crítico. */
    public Optional<Certificado> emitirCertificado() {
        if (houveErroCritico) return Optional.empty();
        return Optional.of(certificadoDAO.inserir(new Certificado(jogador.getId(), pontuacao)));
    }

    public void reiniciar() { fase = 1; pontuacao = 0; houveErroCritico = false; }

    public Path gerarPdf(Certificado cert, Path destino) {
        Document doc = new Document(PageSize.A4.rotate(), 50, 50, 60, 60);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(destino.toFile()));
            doc.open();
            Font titulo = new Font(Font.FontFamily.HELVETICA, 28, Font.BOLD,
                                   new BaseColor(0, 102, 153));
            Font corpo = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL);

            Paragraph p = new Paragraph("PROJETO VITALIS", titulo);
            p.setAlignment(Element.ALIGN_CENTER); doc.add(p);

            p = new Paragraph("Certificado de Conclusão - Primeiros Socorros", corpo);
            p.setAlignment(Element.ALIGN_CENTER); p.setSpacingAfter(40); doc.add(p);

            p = new Paragraph(jogador.getNome(),
                              new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD));
            p.setAlignment(Element.ALIGN_CENTER); doc.add(p);

            p = new Paragraph("concluiu a trilha educativa de primeiros socorros do Projeto Vitalis.", corpo);
            p.setAlignment(Element.ALIGN_CENTER); p.setSpacingBefore(20); doc.add(p);

            p = new Paragraph("Pontuação: " + cert.getPontuacaoFinal()
                              + " pontos | Código: " + cert.getCodigo(), corpo);
            p.setAlignment(Element.ALIGN_CENTER); p.setSpacingBefore(30); doc.add(p);

            doc.close();
            return destino;
        } catch (Exception e) { throw new RuntimeException(e.getMessage(), e); }
    }

    public int getPontuacao() { return pontuacao; }
    public Jogador getJogador() { return jogador; }
}