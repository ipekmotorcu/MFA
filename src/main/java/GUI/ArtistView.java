package GUI;

import javax.swing.*;
import java.awt.*;

public class ArtistView extends JFrame {
    public ArtistView() {
        // Frame
        setTitle("Artist Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Artist Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);

        //SONGS ARE A BASIC LIST FOR NOW AFTER DB CONNECTION WILL BE FIXED
        JTable songTable = new JTable(new String[][]{
                {"Song 1", "100", "50", "10"},
                {"Song 2", "200", "75", "15"}
        }, new String[]{"Title", "Plays", "Likes", "Reactions"});
        JScrollPane tableScroll = new JScrollPane(songTable);
        centerPanel.add(tableScroll, BorderLayout.CENTER);

        //Buttons
        JPanel buttonPanel = new JPanel();
        JButton uploadButton = new JButton("Upload Song");
        JButton editButton = new JButton("Edit Song");
        JButton deleteButton = new JButton("Delete Song");
        buttonPanel.add(uploadButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        //Statistics ( fix to get from db)
        JPanel statsPanel = new JPanel(new GridLayout(3, 1));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
        statsPanel.add(new JLabel("Total Plays: 300"));
        statsPanel.add(new JLabel("Total Likes: 125"));
        statsPanel.add(new JLabel("Total Reactions: 25"));
        add(statsPanel, BorderLayout.EAST);

        JPanel navPanel = new JPanel();
        add(navPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        new ArtistView();
    }
}
