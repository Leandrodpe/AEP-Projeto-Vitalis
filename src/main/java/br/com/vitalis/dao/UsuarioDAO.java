package br.com.vitalis.dao;

import br.com.vitalis.Conexao;
import br.com.vitalis.model.*;
import java.sql.*;
import java.util.*;

public class UsuarioDAO implements GenericDAO<Usuario, Integer> {

    @Override
    public Usuario inserir(Usuario u) {
        String sql = "INSERT INTO usuarios (nome,email,idade,senha,tipo_usuario) VALUES (?,?,?,?,?)";
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNome()); ps.setString(2, u.getEmail());
            ps.setInt(3, u.getIdade()); ps.setString(4, u.getSenha());
            ps.setString(5, u.getTipo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) u.setId(rs.getInt(1));
            }
            return u;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public boolean atualizar(Usuario u) {
        String sql = "UPDATE usuarios SET nome=?,email=?,idade=?,senha=? WHERE id=?";
        try (Connection c = Conexao.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getNome()); ps.setString(2, u.getEmail());
            ps.setInt(3, u.getIdade()); ps.setString(4, u.getSenha());
            ps.setInt(5, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public boolean deletar(Integer id) {
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM usuarios WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public Optional<Usuario> buscar(Integer id) {
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM usuarios WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    public Optional<Usuario> autenticar(String email, String senha) {
        String sql = "SELECT * FROM usuarios WHERE email=? AND senha=?";
        try (Connection c = Conexao.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email); ps.setString(2, senha);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    @Override
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        try (Connection c = Conexao.get();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM usuarios ORDER BY nome");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
            return lista;
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    private Usuario map(ResultSet rs) throws SQLException {
        Usuario u = "ADMINISTRADOR".equals(rs.getString("tipo_usuario"))
            ? new Administrador(rs.getString("nome"), rs.getString("email"),
                                rs.getInt("idade"), rs.getString("senha"))
            : new Jogador(rs.getString("nome"), rs.getString("email"),
                          rs.getInt("idade"), rs.getString("senha"));
        u.setId(rs.getInt("id"));
        return u;
    }
}