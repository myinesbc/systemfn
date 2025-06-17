package banco;

import users.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public boolean inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (fullname, idnumber, password) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getFullname());
            stmt.setString(2, usuario.getIdnumber());
            stmt.setString(3, usuario.getPassword());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario buscarPorIdnumber(String idnumber) {
        String sql = "SELECT id, fullname, idnumber, password FROM usuarios WHERE idnumber = ?";
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idnumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String fullname = rs.getString("fullname");
                String retrievedIdnumber = rs.getString("idnumber");
                String password = rs.getString("password");
                int id = rs.getInt("id"); 
                Usuario usuario = new Usuario(fullname, retrievedIdnumber, password);
                usuario.setId(id); 
                return usuario;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, fullname, idnumber, password FROM usuarios";
        try (Connection conn = ConexaoBanco.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String fullname = rs.getString("fullname");
                String idnumber = rs.getString("idnumber");
                String password = rs.getString("password");
                int id = rs.getInt("id"); 
                Usuario usuario = new Usuario(fullname, idnumber, password);
                usuario.setId(id); 
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }
}