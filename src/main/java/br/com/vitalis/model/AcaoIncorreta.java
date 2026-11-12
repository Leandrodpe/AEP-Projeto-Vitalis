package br.com.vitalis.model;

public class AcaoIncorreta implements Acao {
    private final String texto, feedback;

    public AcaoIncorreta(String texto, String feedback) {
        this.texto = texto; this.feedback = feedback;
    }

    @Override public String getTexto() { return texto; }
    @Override public boolean isCorreta() { return false; }
    @Override public String executar() { return "GAME OVER EDUCATIVO"; }
    @Override public String feedback() { return feedback; }
}