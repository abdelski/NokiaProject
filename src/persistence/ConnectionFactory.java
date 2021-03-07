package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if (conn == null) {
            String dbURL = "jdbc:sqlite:MovieDB";
            conn = DriverManager.getConnection(dbURL);
        }
        return conn;
    }

}
