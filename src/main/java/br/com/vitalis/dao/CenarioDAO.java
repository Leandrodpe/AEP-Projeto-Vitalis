package br.com.vitalis.dao;

import br.com.vitalis.Conexao;
import br.com.vitalis.model.*;
import java.sql.*;
import java.util.*;

public class CenarioDAO implements GenericDAO<Cenario, Integer> {

    @Override
    public Cenario inserir(Cenario cen) {
        String sql = "INSERT INTO cenarios (titulo,narrativa,ordem_fase,imagem) VALUES (?,?,?,?)";
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cen.getTitulo()); ps.setString(2, cen.getNarrativa());
            ps.setInt(3, cen.getOrdemFase()); ps.setString(4, cen.getImagem());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) cen.setId(rs.getInt(1));
            }
            return cen;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public boolean atualizar(Cenario cen) {
        String sql = "UPDATE cenarios SET titulo=?,narrativa=?,ordem_fase=?,imagem=? WHERE id=?";
        try (Connection c = Conexao.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cen.getTitulo()); ps.setString(2, cen.getNarrativa());
            ps.setInt(3, cen.getOrdemFase()); ps.setString(4, cen.getImagem());
            ps.setInt(5, cen.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public boolean deletar(Integer id) {
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM cenarios WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public Optional<Cenario> buscar(Integer id) {
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM cenarios WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                Cenario cen = map(rs);
                carregarAcoes(cen);
                return Optional.of(cen);
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    public Optional<Cenario> buscarPorOrdem(int ordem) {
        String sql = "SELECT * FROM cenarios WHERE ordem_fase=?";
        try (Connection c = Conexao.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, ordem);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                Cenario cen = map(rs);
                carregarAcoes(cen);
                return Optional.of(cen);
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public List<Cenario> listar() {
        List<Cenario> lista = new ArrayList<>();
        String sql = "SELECT * FROM cenarios ORDER BY ordem_fase";
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cenario cen = map(rs);
                carregarAcoes(cen);
                lista.add(cen);
            }
            return lista;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    private void carregarAcoes(Cenario cen) throws SQLException {
        String sql = "SELECT * FROM opcoes_escolha WHERE cenario_id=? ORDER BY id";
        try (Connection c = Conexao.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cen.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cen.addAcao(rs.getBoolean("e_correta")
                        ? new AcaoCorreta(rs.getString("texto"), rs.getString("narrativa_sucesso"))
                        : new AcaoIncorreta(rs.getString("texto"), rs.getString("feedback_didatico")));
                }
            }
        }
    }

    private Cenario map(ResultSet rs) throws SQLException {
        Cenario cen = new Cenario(rs.getString("titulo"), rs.getString("narrativa"),
                                  rs.getInt("ordem_fase"), rs.getString("imagem"));
        cen.setId(rs.getInt("id"));
        return cen;
    }
}