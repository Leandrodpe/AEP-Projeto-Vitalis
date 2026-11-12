package br.com.vitalis.dao;

import br.com.vitalis.Conexao;
import br.com.vitalis.model.Biblioteca;
import java.sql.*;
import java.util.*;

public class BibliotecaDAO implements GenericDAO<Biblioteca, Integer> {

    @Override
    public Biblioteca inserir(Biblioteca b) {
        String sql = "INSERT INTO biblioteca (titulo,conteudo,categoria) VALUES (?,?,?)";
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, b.getTitulo()); ps.setString(2, b.getConteudo());
            ps.setString(3, b.getCategoria());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) b.setId(rs.getInt(1));
            }
            return b;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public boolean atualizar(Biblioteca b) {
        String sql = "UPDATE biblioteca SET titulo=?,conteudo=?,categoria=? WHERE id=?";
        try (Connection c = Conexao.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getTitulo()); ps.setString(2, b.getConteudo());
            ps.setString(3, b.getCategoria()); ps.setInt(4, b.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public boolean deletar(Integer id) {
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM biblioteca WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public Optional<Biblioteca> buscar(Integer id) {
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM biblioteca WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public List<Biblioteca> listar() {
        List<Biblioteca> lista = new ArrayList<>();
        String sql = "SELECT * FROM biblioteca ORDER BY categoria, titulo";
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
            return lista;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    private Biblioteca map(ResultSet rs) throws SQLException {
        Biblioteca b = new Biblioteca(rs.getString("titulo"), rs.getString("conteudo"),
                                      rs.getString("categoria"));
        b.setId(rs.getInt("id"));
        return b;
    }
}