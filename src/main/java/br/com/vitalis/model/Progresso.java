package br.com.vitalis.model;

public class Progresso {
    public enum Status { EM_ANDAMENTO, GAME_OVER, CONCLUIDO }

    private int id, jogadorId, cenarioId, pontuacao;
    private Status status = Status.EM_ANDAMENTO;

    public Progresso(int jogadorId, int cenarioId) {
        this.jogadorId = jogadorId; this.cenarioId = cenarioId;
    }

    public void acertar(int pts) { pontuacao += pts; status = Status.CONCLUIDO; }
    public void errar() { status = Status.GAME_OVER; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getJogadorId() { return jogadorId; }
    public int getCenarioId() { return cenarioId; }
    public int getPontuacao() { return pontuacao; }
    public void setPontuacao(int p) { this.pontuacao = p; }
    public Status getStatus() { return status; }
    public void setStatus(Status s) { this.status = s; }
}