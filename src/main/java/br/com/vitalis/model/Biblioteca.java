package br.com.vitalis.model;

public class Biblioteca {
    private int id;
    private String titulo, conteudo, categoria;

    public Biblioteca(String titulo, String conteudo, String categoria) {
        this.titulo = titulo; this.conteudo = conteudo; this.categoria = categoria;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String t) { this.titulo = t; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String c) { this.conteudo = c; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String c) { this.categoria = c; }

    @Override public String toString() { return titulo + " [" + categoria + "]"; }
}