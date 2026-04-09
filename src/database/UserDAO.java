package database;

import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    // stores username, email, password and role
    public int addUser(String username, String email, String password, String role) throws Exception {
        String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, username);
        ps.setString(2, email);
        ps.setString(3, password);
        ps.setString(4, role);

        int rows = ps.executeUpdate();

        ps.close();
        conn.close();

        return rows;
    }

    // checks if username and password match and returns a User object, returns null if login fails
    public User loginGetRole(String username, String password) throws Exception {
        String sql = "SELECT user_id, username, role FROM users WHERE username=? AND password=?";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();

        // will store user if login is successful
        User user = null;
        // if a match is found, build a User object
        if (rs.next()) {
            user = new User(rs.getInt("user_id"), rs.getString("username"), password, rs.getString("role"));
        }

        rs.close();
        ps.close();
        conn.close();

        return user;
    }
}