package GUI;

import database.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginView extends JFrame {
    JLabel userLabel, passwordLabel, titleLabel;
    JTextField userText;
    JPasswordField passwordText; // Use JPasswordField for password security
    JButton loginButton;
    Connection connection;

    public LoginView() {
        this.setTitle("LOGIN to MFA");

        // Connect to database
        connectToDatabase();

        // Main panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);

        // Title label (centered)
        titleLabel = new JLabel("Welcome to Music for All", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Font styling
        titleLabel.setBounds(20, 20, 360, 30); // Adjust bounds for centering
        loginPanel.add(titleLabel);

        // database.User label and text field
        userLabel = new JLabel("User Name");
        userLabel.setBounds(20, 80, 80, 25);
        loginPanel.add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(100, 80, 165, 25);
        loginPanel.add(userText);

        // Password label and text field
        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(20, 120, 80, 25);
        loginPanel.add(passwordLabel);

        passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 120, 165, 25);
        loginPanel.add(passwordText);

        // Login button
        loginButton = new JButton("LOGIN");
        loginButton.setBounds(100, 200, 160, 25);
        loginPanel.add(loginButton);

        // Button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateUser();
            }
        });

        // Add the main panel to the frame
        this.setContentPane(loginPanel);
        this.setSize(400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    // Connect to the database
    private void connectToDatabase() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    // Validate user credentials and open the corresponding view
    private void validateUser() {
        String username = userText.getText();
        String password = new String(passwordText.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username or password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String query = "SELECT UserType, UserID FROM USER WHERE Username = ? AND Password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("UserType");
                int userId = resultSet.getInt("UserID");

                openUserView(role, userId);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error validating user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Open the appropriate view based on user role
    private void openUserView(String role, int userId) {
        this.dispose(); // Close the login window

        switch (role.toLowerCase()) {
            case "admin":
                new AdminView(userId);
                break;
            case "listener":
                new ListenerView(userId); // Open ListenerView for listeners
                break;
            case "artist":
                new ArtistView(userId); //
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown role: " + role, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new LoginView();
    }
}
