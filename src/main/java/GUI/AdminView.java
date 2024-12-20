package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import database.Admin;
import database.DBConnection;
public class AdminView extends JFrame {
    private Admin admin;
    private CardLayout cardLayout; // To switch between Main Page, Ban database.User Page, and Statistics Page
    private JPanel mainPanel, banUserPanel, statisticsPanel, containerPanel;
    private int userid;
    public AdminView(int userid) {
        this.userid = userid;
        this.admin=new Admin(userid,"admin");

        // Frame settings
        setTitle("Admin Main Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Layout Management
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        // Initialize panels
        mainPanel = createMainPage();
        banUserPanel = createBanUserPage();
        statisticsPanel = createStatisticsPage();

        containerPanel.add(mainPanel, "MainPage");
        containerPanel.add(banUserPanel, "BanUserPage");
        containerPanel.add(statisticsPanel, "StatisticsPage");

        add(containerPanel);
        cardLayout.show(containerPanel, "MainPage"); // Start with Main Page

        setVisible(true);
    }

    private JPanel createMainPage() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Main Page Buttons
        JButton statsButton = new JButton("Statistics");
        JButton banUserButton = new JButton("Ban User");
        JButton createListenerButton = new JButton("Create Listener");

        // Button actions
        statsButton.addActionListener(e -> cardLayout.show(containerPanel, "StatisticsPage"));
        banUserButton.addActionListener(e -> cardLayout.show(containerPanel, "BanUserPage"));

        createListenerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt the admin to enter a new listener's username, password, and age
                JTextField usernameField = new JTextField(20);
                JTextField ageField = new JTextField(20);
                JPasswordField passwordField = new JPasswordField(20);

                JPanel inputPanel = new JPanel(new GridLayout(3, 2));
                inputPanel.add(new JLabel("Username:"));
                inputPanel.add(usernameField);
                inputPanel.add(new JLabel("Password:"));
                inputPanel.add(passwordField);
                inputPanel.add(new JLabel("Age:"));
                inputPanel.add(ageField);

                int option = JOptionPane.showConfirmDialog(null, inputPanel, "Create New Listener", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    String username = usernameField.getText().trim();
                    char[] passwordChars = passwordField.getPassword();
                    String password = new String(passwordChars).trim();
                    String ageText = ageField.getText().trim();

                    // Validate input
                    if (username.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Username cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int age = 0;
                    try {
                        age = Integer.parseInt(ageText);
                        if (age <= 0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid age.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Create the listener
                    admin.createListener(username, password, age);

                    // Show success message
                    JOptionPane.showMessageDialog(null, "Listener created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Add components
        panel.add(statsButton);
        panel.add(banUserButton);
        panel.add(createListenerButton);

        return panel;
    }

    private JPanel createBanUserPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Ban User", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField searchBar = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search User:"));
        searchPanel.add(searchBar);
        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        // database.User List Panel
        DefaultListModel<String> userListModel = new DefaultListModel<>();
        JList<String> userList = new JList<>(userListModel);
        JScrollPane scrollPane = new JScrollPane(userList);
        panel.add(scrollPane, BorderLayout.CENTER);

        admin.fetchUsersFromDB(userListModel, "");

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton banButton = new JButton("Ban Selected User");
        JButton backButton = new JButton("Back to Main Page");

        actionPanel.add(banButton);
        actionPanel.add(backButton);

        panel.add(actionPanel, BorderLayout.SOUTH);

        // Ban database.User Action
        banButton.addActionListener(e -> {
            String selectedUser = userList.getSelectedValue();
            if (selectedUser == null) {
                JOptionPane.showMessageDialog(this, "Please select a user to ban.", "No User Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int userId = Integer.parseInt(selectedUser.split(" - ")[0]);
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to ban this user?",
                        "Confirm Ban",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    admin.banUserInDB(userId);
                    JOptionPane.showMessageDialog(this, "User banned successfully.");
                    admin.fetchUsersFromDB(userListModel,""); // Refresh the list
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid user format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Back to Main Page Action
        backButton.addActionListener(e -> cardLayout.show(containerPanel, "MainPage"));

        // Search Button Action
        searchButton.addActionListener(e -> {
            String searchTerm = searchBar.getText().trim(); // Get search term from the search bar
            admin.fetchUsersFromDB(userListModel, searchTerm); // Call the method with search term
        });


        return panel;
    }

    private JPanel createStatisticsPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Statistics", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Statistics Text Area
        JTextArea statsTextArea = new JTextArea(15, 50);
        statsTextArea.setEditable(false); // Make it non-editable
        JScrollPane statsScrollPane = new JScrollPane(statsTextArea);
        panel.add(statsScrollPane, BorderLayout.CENTER);

        // Fetch statistics from DB
        admin.fetchStatisticsFromDB(statsTextArea);

        // Action Panel with "Back" button
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton backButton = new JButton("Back to Main Page");
        actionPanel.add(backButton);

        panel.add(actionPanel, BorderLayout.SOUTH);

        // Back to Main Page Action
        backButton.addActionListener(e -> cardLayout.show(containerPanel, "MainPage"));

        return panel;
    }
    public static void main(String[] args) {
        Admin admin = new Admin(1, "AdminName"); // Example database.Admin instance
        new AdminView(6);
    }
}
