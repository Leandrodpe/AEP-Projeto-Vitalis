package br.com.vitalis.model;

import java.util.*;

public class Jogador extends Usuario {
    private final List<Progresso> progressos = new ArrayList<>();
    private final List<Certificado> certificados = new ArrayList<>();

    public Jogador(String nome, String email, int idade, String senha) {
        super(nome, email, idade, senha);
    }

    @Override public String getTipo() { return "JOGADOR"; }
    @Override public boolean isAdmin() { return false; }

    public void addProgresso(Progresso p) { progressos.add(p); }
    public void addCertificado(Certificado c) { certificados.add(c); }
    public List<Progresso> getProgressos() { return progressos; }
    public List<Certificado> getCertificados() { return certificados; }
}