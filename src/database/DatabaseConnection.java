package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DATABASE_URL =
            "jdbc:mysql://localhost:3306/restaurant_schema?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "57Longwood%";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE_URL, USERNAME, PASSWORD
        );
    }
}
