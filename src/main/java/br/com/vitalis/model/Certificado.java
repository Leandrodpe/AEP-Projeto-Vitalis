package br.com.vitalis.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Certificado {
    private int id, jogadorId, pontuacaoFinal;
    private String codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    private LocalDateTime data = LocalDateTime.now();

    public Certificado(int jogadorId, int pontuacaoFinal) {
        this.jogadorId = jogadorId; this.pontuacaoFinal = pontuacaoFinal;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getJogadorId() { return jogadorId; }
    public int getPontuacaoFinal() { return pontuacaoFinal; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String c) { this.codigo = c; }
    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime d) { this.data = d; }
}