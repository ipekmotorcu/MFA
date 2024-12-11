package GUI;

import javax.swing.*;
import java.awt.*;

public class AdminView extends JFrame {
    public AdminView() {
        // Frame
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);

        // User list
        JPanel userListPanel = new JPanel(new BorderLayout());
        userListPanel.setBorder(BorderFactory.createTitledBorder("Users"));

        //USERS ARE A BASIC LIST FOR NOW AFTER DB CONNECTION WILL BE FIXED

        String[] users = {"User1", "User2", "User3", "User4"};
        JList<String> userList = new JList<>(users);
        JScrollPane userScrollPane = new JScrollPane(userList);
        userListPanel.add(userScrollPane, BorderLayout.CENTER);
        centerPanel.add(userListPanel, BorderLayout.CENTER);

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField searchBar = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search User:"));
        searchPanel.add(searchBar);
        searchPanel.add(searchButton);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // Ban Button
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton banButton = new JButton("Ban User");
        actionPanel.add(banButton);
        centerPanel.add(actionPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        new AdminView();
    }
}
