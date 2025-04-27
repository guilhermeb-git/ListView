package Dao;

import Connection.ConnectionFactory;
import Models.Tarefa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarefaDao {

    public void adicionar(Tarefa tarefa) throws SQLException {
        String sql = "INSERT INTO tarefas (titulo, descricao, data_entrega, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setDate(3, Date.valueOf(tarefa.getDataEntrega()));
            stmt.setString(4, tarefa.getStatus());
            stmt.executeUpdate();
        }
    }

    public List<Tarefa> listar() throws SQLException {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT * FROM tarefas ORDER BY id";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(rs.getInt("id"));
                tarefa.setTitulo(rs.getString("titulo"));
                tarefa.setDescricao(rs.getString("descricao"));
                tarefa.setDataEntrega(rs.getDate("data_entrega").toLocalDate());
                tarefa.setStatus(rs.getString("status"));
                tarefas.add(tarefa);
            }
        }
        return tarefas;
    }

    public void atualizar(Tarefa tarefa) throws SQLException {
        String sql = "UPDATE tarefas SET titulo=?, descricao=?, data_entrega=?, status=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setDate(3, Date.valueOf(tarefa.getDataEntrega()));
            stmt.setString(4, tarefa.getStatus());
            stmt.setInt(5, tarefa.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM tarefas WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}

