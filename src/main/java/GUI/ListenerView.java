package GUI;

import javax.swing.*;
import java.awt.*;

public class ListenerView extends JFrame {
    public ListenerView() {
        //Frame
        setTitle("Listener Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Listener Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        add(centerPanel, BorderLayout.CENTER);

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField searchBar = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchBar);
        searchPanel.add(searchButton);
        centerPanel.add(searchPanel);

        // LIKED SONGS ?? (SHOULD IT LOOK LIKE THIS OR THAT WAY)
        JPanel likedSongsPanel = new JPanel();
        likedSongsPanel.setBorder(BorderFactory.createTitledBorder("Liked Songs"));
        JList<String> likedSongsList = new JList<>(new String[]{"Song 1", "Song 2", "Song 3"});
        JScrollPane likedScrollPane = new JScrollPane(likedSongsList);
        likedScrollPane.setPreferredSize(new Dimension(350, 150));
        likedSongsPanel.add(likedScrollPane);
        centerPanel.add(likedSongsPanel);

        // Interact
        JPanel interactionPanel = new JPanel();
        JButton likeButton = new JButton("Like");
        JButton dislikeButton = new JButton("Dislike");
        JButton emojiButton = new JButton("Leave Emoji");
        interactionPanel.add(likeButton);
        interactionPanel.add(dislikeButton);
        interactionPanel.add(emojiButton);
        centerPanel.add(interactionPanel);

        // Artist Follow/Unfollow Section
        JPanel followPanel = new JPanel(new FlowLayout());
        JButton followButton = new JButton("Follow Artist");
        JButton unfollowButton = new JButton("Unfollow Artist");
        followPanel.add(followButton);
        followPanel.add(unfollowButton);
        centerPanel.add(followPanel);

        // LIKED SONGS ??
        JPanel navPanel = new JPanel();
        JButton likedSongsPageButton = new JButton("Liked Songs");
        navPanel.add(likedSongsPageButton);
        add(navPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        new ListenerView();
    }
}
