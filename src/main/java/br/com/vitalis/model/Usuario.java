package br.com.vitalis.model;

public abstract class Usuario {
    protected int id, idade;
    protected String nome, email, senha;

    public Usuario(String nome, String email, int idade, String senha) {
        this.nome = nome; this.email = email; this.idade = idade; this.senha = senha;
    }

    public abstract String getTipo();
    public abstract boolean isAdmin();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String n) { this.nome = n; }
    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }
    public int getIdade() { return idade; }
    public void setIdade(int i) { this.idade = i; }
    public String getSenha() { return senha; }
    public void setSenha(String s) { this.senha = s; }
}