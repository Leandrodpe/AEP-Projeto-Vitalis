package br.com.vitalis.dao;

import br.com.vitalis.Conexao;
import br.com.vitalis.model.Progresso;
import java.sql.*;
import java.util.*;

public class ProgressoDAO implements GenericDAO<Progresso, Integer> {

    @Override
    public Progresso inserir(Progresso p) {
        String sql = "INSERT INTO progressos (jogador_id,cenario_id,pontuacao,status_partida) VALUES (?,?,?,?)";
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getJogadorId()); ps.setInt(2, p.getCenarioId());
            ps.setInt(3, p.getPontuacao()); ps.setString(4, p.getStatus().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getInt(1));
            }
            return p;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public boolean atualizar(Progresso p) {
        String sql = "UPDATE progressos SET pontuacao=?,status_partida=? WHERE id=?";
        try (Connection c = Conexao.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, p.getPontuacao()); ps.setString(2, p.getStatus().name());
            ps.setInt(3, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public boolean deletar(Integer id) {
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM progressos WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public Optional<Progresso> buscar(Integer id) {
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM progressos WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public List<Progresso> listar() {
        List<Progresso> lista = new ArrayList<>();
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM progressos");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
            return lista;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    private Progresso map(ResultSet rs) throws SQLException {
        Progresso p = new Progresso(rs.getInt("jogador_id"), rs.getInt("cenario_id"));
        p.setId(rs.getInt("id"));
        p.setPontuacao(rs.getInt("pontuacao"));
        p.setStatus(Progresso.Status.valueOf(rs.getString("status_partida")));
        return p;
    }
}