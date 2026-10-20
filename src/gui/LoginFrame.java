package gui;

import database.RentalDatabaseSql;
import models.Client;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private final RentalDatabaseSql db;

    public LoginFrame() {
        this.db = new RentalDatabaseSql();

        setTitle("Car Rental System - Login");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        BackgroundPanel bg = new BackgroundPanel("background.jpg");
        bg.setLayout(new BorderLayout());
        setContentPane(bg);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setOpaque(true);
        mainPanel.setBackground(new Color(255, 255, 255, 210));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Car Rental System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Login as:"), gbc);

        gbc.gridx = 1;
        userTypeCombo = new JComboBox<>(new String[]{"Client", "Admin"});
        mainPanel.add(userTypeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        mainPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        mainPanel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register Client");

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        centerWrap.add(mainPanel);
        bg.add(centerWrap, BorderLayout.CENTER);

        passwordField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter username and password",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if ("Client".equals(userType)) {
                models.Client client = db.findClientByUsername(username);
                if (client != null && client.login(username, password)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Welcome, " + client.getFullName() + "!",
                            "Login Successful",
                            JOptionPane.PLAIN_MESSAGE,
                            getWelcomeIcon()
                    );

                    new ClientDashboard(client).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid credentials",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                models.Admin admin = db.findAdminByUsername(username);
                if (admin != null && admin.login(username, password)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Welcome, " + admin.getFullName() + "!",
                            "Login Successful",
                            JOptionPane.PLAIN_MESSAGE,
                            getWelcomeIcon()
                    );

                    new AdminDashboard(admin).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid credentials",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            String msg = ex.getMessage();
            if (ex.getCause() != null) msg += "\nCause: " + ex.getCause().getMessage();
            JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleRegister() {
        JTextField newUsername = new JTextField();
        JPasswordField newPassword = new JPasswordField();
        JTextField email = new JTextField();
        JTextField fullName = new JTextField();
        JTextField license = new JTextField();

        Object[] fields = {
                "Username:", newUsername,
                "Password:", newPassword,
                "Email:", email,
                "Full Name:", fullName,
                "License Number:", license
        };

        ImageIcon logo = new ImageIcon("logo.png");
        Image img = logo.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        ImageIcon smallLogo = new ImageIcon(img);
        int result = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Register New Client",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                smallLogo
        );


        if (result == JOptionPane.OK_OPTION) {
            try {
                String user = newUsername.getText().trim();
                String pass = new String(newPassword.getPassword());

                if (user.isEmpty() || pass.isEmpty()) {
                    throw new IllegalArgumentException("Username and password required");
                }

                if (db.findClientByUsername(user) != null) {
                    throw new IllegalArgumentException("Username already exists");
                }

                Client newClient = new Client(user, pass, email.getText().trim(),
                        fullName.getText().trim(), license.getText().trim());

                db.addClient(newClient);

                ImageIcon regIcon = new ImageIcon("registrationok.jpg");
                Image regImg = regIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                ImageIcon smallRegIcon = new ImageIcon(regImg);

                JOptionPane.showMessageDialog(
                        this,
                        "Registration successful! You can now login.",
                        "Success",
                        JOptionPane.PLAIN_MESSAGE,
                        smallRegIcon
                );
            } catch (Exception ex) {
                String msg = ex.getMessage();
                if (ex.getCause() != null) msg += "\nCause: " + ex.getCause().getMessage();
                JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private ImageIcon getWelcomeIcon() {
        ImageIcon icon = new ImageIcon("emoji.jpg");
        Image img = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
