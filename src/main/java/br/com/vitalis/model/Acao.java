package br.com.vitalis.model;

public interface Acao {
    String getTexto();
    boolean isCorreta();
    String executar();
    String feedback();
}