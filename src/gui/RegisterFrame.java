// Name : Ryan Lowry
// Date : 02/04/2026
// File : Register.java
package gui;

import database.UserDAO;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    // input fields for user register
    private JTextField txtUser;
    private JTextField txtEmail;
    private JPasswordField txtPass;

    // buttons for actions
    private JButton btnRegister;
    private JButton btnBack;

    public RegisterFrame() {

        setTitle("Register");
        setSize(400, 320);
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
        container.setOpaque(false);

        // title label
        JLabel lblTitle = new JLabel("Create Account");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));

        // form layout for inputs
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setMaximumSize(new Dimension(260, 150));
        formPanel.setOpaque(false);

        txtUser = new JTextField();
        txtEmail = new JTextField();
        txtPass = new JPasswordField();

        btnRegister = new JButton("Register");
        btnBack = new JButton("Back to Login");

        // style register button
        btnRegister.setBackground(new Color(70, 130, 180));
        btnRegister.setForeground(Color.WHITE);

        // add form username,email,password
        formPanel.add(new JLabel("Username:"));
        formPanel.add(txtUser);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Password:"));
        formPanel.add(txtPass);

        formPanel.add(btnRegister);
        formPanel.add(btnBack);

        // spacing between title and form
        container.add(lblTitle);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        container.add(formPanel);

        mainPanel.add(container);
        add(mainPanel);

        // handles register button click
        btnRegister.addActionListener(e -> {
            try {
                String username = txtUser.getText();
                String email = txtEmail.getText();
                String password = new String(txtPass.getPassword());

                // validation checks
                if (username.equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter username");
                    return;
                }

                if (!email.contains("@")) {
                    JOptionPane.showMessageDialog(null, "Invalid email");
                    return;
                }

                if (password.length() < 4) {
                    JOptionPane.showMessageDialog(null, "Password too short");
                    return;
                }

                // add user to database
                UserDAO dao = new UserDAO();
                int rows = dao.addUser(username, email, password, "USER");

                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Registered!");
                    dispose();
                    new LoginFrame();
                } else {
                    JOptionPane.showMessageDialog(null, "Error");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error");
            }
        });

        // returns to login screen
        btnBack.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }
}