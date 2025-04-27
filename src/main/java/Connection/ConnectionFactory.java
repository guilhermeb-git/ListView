package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/tarefasdb", "postgres", "1020");
        } catch (SQLException e) {
            throw new SQLException("Error connecting to the database", e);
        }
    }
}

