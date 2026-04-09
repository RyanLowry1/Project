// Name : Ryan Lowry
// Date : 02/04/2026
// File : LoginFrame.java
package gui;

import database.UserDAO;
import model.User;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    // input fields for login
    private JTextField txtUser;
    private JPasswordField txtPass;

    // buttons for actions
    private JButton btnLogin;
    private JButton btnRegister;

    public LoginFrame() {

        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // main panel with gradient background
        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // creates gradient effect
                Graphics2D g2d = (Graphics2D) g;
                Color c1 = new Color(200, 220, 255);
                Color c2 = new Color(240, 245, 255);
                GradientPaint gp = new GradientPaint(0, 0, c1, 0, getHeight(), c2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        mainPanel.setLayout(new GridBagLayout()); // centers everything

        // container holds title and form
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false); // allows gradient to show

        // title label
        JLabel lblTitle = new JLabel("Welcome Back");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));

        // form layout
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setMaximumSize(new Dimension(250, 120));
        formPanel.setOpaque(false);

        txtUser = new JTextField();
        txtPass = new JPasswordField();

        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");

        // style login button
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);

        // add username and password field to form
        formPanel.add(new JLabel("Username:"));
        formPanel.add(txtUser);

        formPanel.add(new JLabel("Password:"));
        formPanel.add(txtPass);

        formPanel.add(btnLogin);
        formPanel.add(btnRegister);

        // spacing between title and form
        container.add(lblTitle);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        container.add(formPanel);

        mainPanel.add(container);
        add(mainPanel);

        // handles login button click
        btnLogin.addActionListener(e -> {
            try {
                String username = txtUser.getText();
                String password = new String(txtPass.getPassword());

                // checks login details using database
                UserDAO dao = new UserDAO();
                User user = dao.loginGetRole(username, password);

                // if login fails
                if (user == null) {
                    JOptionPane.showMessageDialog(null, "Login failed");
                }
                else {
                    dispose();

                    // open correct screen based on role
                    if (user.getRole().equalsIgnoreCase("ADMIN")) {
                        new AdminFrame();
                    } else {
                        new UserProductFrame(user);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error");
            }
        });

        // opens register screen
        btnRegister.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        setVisible(true);
    }
}