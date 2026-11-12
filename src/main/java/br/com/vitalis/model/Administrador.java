package br.com.vitalis.model;

public class Administrador extends Usuario {
    public Administrador(String nome, String email, int idade, String senha) {
        super(nome, email, idade, senha);
    }
    @Override public String getTipo() { return "ADMINISTRADOR"; }
    @Override public boolean isAdmin() { return true; }
}