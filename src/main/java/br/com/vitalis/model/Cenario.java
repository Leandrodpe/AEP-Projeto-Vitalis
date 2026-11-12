package br.com.vitalis.model;

import java.util.*;

public class Cenario {
    private int id, ordemFase;
    private String titulo, narrativa, imagem;
    private final List<Acao> acoes = new ArrayList<>();

    public Cenario(String titulo, String narrativa, int ordemFase, String imagem) {
        this.titulo = titulo; this.narrativa = narrativa;
        this.ordemFase = ordemFase; this.imagem = imagem;
    }

    public void addAcao(Acao a) { acoes.add(a); }
    public List<Acao> getAcoes() { return acoes; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getOrdemFase() { return ordemFase; }
    public void setOrdemFase(int o) { this.ordemFase = o; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String t) { this.titulo = t; }
    public String getNarrativa() { return narrativa; }
    public void setNarrativa(String n) { this.narrativa = n; }
    public String getImagem() { return imagem; }
    public void setImagem(String i) { this.imagem = i; }
}