// Name : Ryan Lowry
// Date : 02/04/2026
// File : User.java
package model;

public class User {

    // stores user details for login and session
    private int userId;
    private String username;
    private String password;
    private String role; // Admin or User

    public User() {}

    // used when logging in
    public User(int userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // GETTERS

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}