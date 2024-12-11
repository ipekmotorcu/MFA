package GUI;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    JLabel userLabel, passwordLabel, titleLabel;
    JTextField userText;
    JTextField passwordText;
    JButton loginButton;

    public JTextField getUserText() {
        return userText;
    }

    public JTextField getPasswordText() {
        return passwordText;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public LoginView() {
        this.setTitle("LOGIN to MFA");

        // Main panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);

        // Title label (centered)
        titleLabel = new JLabel("Welcome to Music for All", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Font styling
        titleLabel.setBounds(20, 20, 360, 30); // Adjust bounds for centering
        loginPanel.add(titleLabel);

        // User label and text field
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

        // Add the main panel to the frame
        this.setContentPane(loginPanel);
        this.setSize(400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        System.out.println("Project is set up!");
        new LoginView();
    }
}
