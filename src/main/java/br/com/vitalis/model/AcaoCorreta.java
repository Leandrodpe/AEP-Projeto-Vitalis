package br.com.vitalis.model;

public class AcaoCorreta implements Acao {
    private final String texto, narrativa;

    public AcaoCorreta(String texto, String narrativa) {
        this.texto = texto; this.narrativa = narrativa;
    }

    @Override public String getTexto() { return texto; }
    @Override public boolean isCorreta() { return true; }
    @Override public String executar() { return "AÇÃO BEM-SUCEDIDA!\n\n" + narrativa; }
    @Override public String feedback() { return "Correto! Protocolo aplicado adequadamente."; }
}