package br.com.vitalis.dao;

import br.com.vitalis.Conexao;
import br.com.vitalis.model.Certificado;
import java.sql.*;
import java.util.*;

public class CertificadoDAO implements GenericDAO<Certificado, Integer> {

    @Override
    public Certificado inserir(Certificado cert) {
        String sql = "INSERT INTO certificados (jogador_id,codigo_verificador,pontuacao_final) VALUES (?,?,?)";
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cert.getJogadorId()); ps.setString(2, cert.getCodigo());
            ps.setInt(3, cert.getPontuacaoFinal());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) cert.setId(rs.getInt(1));
            }
            return cert;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public boolean atualizar(Certificado cert) {
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement(
                 "UPDATE certificados SET pontuacao_final=? WHERE id=?")) {
            ps.setInt(1, cert.getPontuacaoFinal()); ps.setInt(2, cert.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public boolean deletar(Integer id) {
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM certificados WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public Optional<Certificado> buscar(Integer id) {
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM certificados WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public List<Certificado> listar() {
        List<Certificado> lista = new ArrayList<>();
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM certificados");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
            return lista;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    private Certificado map(ResultSet rs) throws SQLException {
        Certificado cert = new Certificado(rs.getInt("jogador_id"), rs.getInt("pontuacao_final"));
        cert.setId(rs.getInt("id"));
        cert.setCodigo(rs.getString("codigo_verificador"));
        return cert;
    }
}