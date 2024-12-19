package GUI;



import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import database.Listener; // Import database.Listener class

public class ListenerView extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField searchBar;
    private JLabel songNameLabel, songLikesLabel, songDislikesLabel;
    private JList<String> likedSongsList, playlistList;
    private DefaultListModel<String> playlistModel, songsInPlaylistModel;
    private int songId; // To keep track of the current song being viewed
    private int userid;
    private Connection connection;
    private Listener listener;

    public ListenerView(int userid) {
        this.userid = userid;

        // Database connection
        connectToDatabase();
        listener = new Listener(connection, userid);

        // Frame setup
        setTitle("database.Listener Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Main panels
        JPanel dashboardPanel = createDashboardPanel();
        JPanel songDetailPanel = createSongDetailPanel();
        JPanel playlistDetailPanel = createPlaylistDetailPanel();

        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(songDetailPanel, "SongDetail");
        mainPanel.add(playlistDetailPanel, "PlaylistDetail");

        add(mainPanel);
        setVisible(true);
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mfa", "root", "im66709903");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Listener Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        dashboardPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        dashboardPanel.add(centerPanel, BorderLayout.CENTER);

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchBar = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchBar);
        searchPanel.add(searchButton);
        centerPanel.add(searchPanel);

        searchButton.addActionListener(e -> searchSong());

        // Liked Songs
        JPanel likedSongsPanel = new JPanel();
        likedSongsPanel.setBorder(BorderFactory.createTitledBorder("Liked Songs"));
        DefaultListModel<String> likedSongsModel = new DefaultListModel<>();
        likedSongsList = new JList<>(likedSongsModel);
        likedSongsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Fetch liked songs
        try {
            List<String> likedSongs = listener.getLikedSongs();
            likedSongsModel.clear();
            for (String song : likedSongs) {
                likedSongsModel.addElement(song);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching liked songs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        likedSongsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedSong = likedSongsList.getSelectedValue();
                if (selectedSong != null) {
                    try {
                        // Fetch MusicID for the selected song
                        int selectedSongId = listener.getMusicIdByName(selectedSong);
                        if (selectedSongId != -1) {
                            songId = selectedSongId; // Update the current song ID
                            fetchSongDetailsByName(selectedSong);
                        } else {
                            JOptionPane.showMessageDialog(this, "Song ID not found for " + selectedSong,
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error fetching MusicID: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });



        JScrollPane likedScrollPane = new JScrollPane(likedSongsList);
        likedScrollPane.setPreferredSize(new Dimension(350, 150));
        likedSongsPanel.add(likedScrollPane);
        centerPanel.add(likedSongsPanel);

        // Playlists
        JPanel playlistsPanel = new JPanel();
        playlistsPanel.setBorder(BorderFactory.createTitledBorder("Playlists"));
        playlistModel = new DefaultListModel<>();
        playlistList = new JList<>(playlistModel);
        JScrollPane playlistScrollPane = new JScrollPane(playlistList);
        playlistScrollPane.setPreferredSize(new Dimension(350, 150));
        playlistsPanel.add(playlistScrollPane);

        try {
            List<String> playlists = listener.getPlaylists();
            playlistModel.clear();
            for (String playlist : playlists) {
                playlistModel.addElement(playlist);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching playlists: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        playlistList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedPlaylist = playlistList.getSelectedValue();
                if (selectedPlaylist != null) {
                    showPlaylistDetail(selectedPlaylist);
                }
            }
        });

        JButton createPlaylistButton = new JButton("Create Playlist");
        createPlaylistButton.addActionListener(e -> {
            String playlistName = JOptionPane.showInputDialog(this, "Enter playlist name:");
            if (playlistName != null && !playlistName.isEmpty()) {
                try {
                    listener.createPlaylist(playlistName);
                    playlistModel.addElement(playlistName);
                    JOptionPane.showMessageDialog(this, "Playlist created successfully.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error creating playlist: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        playlistsPanel.add(createPlaylistButton);
        centerPanel.add(playlistsPanel);

        return dashboardPanel;
    }
    private JPanel createPlaylistDetailPanel() {
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BorderLayout());

        JLabel playlistLabel = new JLabel("Playlist Details", SwingConstants.CENTER);
        playlistLabel.setFont(new Font("Arial", Font.BOLD, 18));
        detailPanel.add(playlistLabel, BorderLayout.NORTH);

        // Initialize the DefaultListModel
        songsInPlaylistModel = new DefaultListModel<>();
        JList<String> songsInPlaylistList = new JList<>(songsInPlaylistModel);
        JScrollPane scrollPane = new JScrollPane(songsInPlaylistList);
        detailPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addSongButton = new JButton("Add Song");
        JButton removeSongButton = new JButton("Remove Song");
        JButton backButton = new JButton("Back");

        buttonPanel.add(addSongButton);
        buttonPanel.add(removeSongButton);
        buttonPanel.add(backButton);
        detailPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Back Button Logic
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

        // Add Song Logic
        addSongButton.addActionListener(e -> {
            String songToAdd = JOptionPane.showInputDialog(this, "Enter song name to add:");
            if (songToAdd != null && !songToAdd.isEmpty()) {
                try {
                    String selectedPlaylist = playlistList.getSelectedValue();
                    if (selectedPlaylist != null) {
                        listener.addSongToPlaylist(selectedPlaylist, songToAdd);
                        songsInPlaylistModel.addElement(songToAdd); // Update UI
                        JOptionPane.showMessageDialog(this, "Song added successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Select a playlist first.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error adding song: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Remove Song Logic
        removeSongButton.addActionListener(e -> {
            String selectedSong = songsInPlaylistList.getSelectedValue();
            if (selectedSong != null) {
                try {
                    String selectedPlaylist = playlistList.getSelectedValue();
                    if (selectedPlaylist != null) {
                        listener.removeSongFromPlaylist(selectedPlaylist, selectedSong);
                        songsInPlaylistModel.removeElement(selectedSong); // Update UI
                        JOptionPane.showMessageDialog(this, "Song removed successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Select a playlist first.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error removing song: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No song selected.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        return detailPanel;
    }

    private JPanel createSongDetailPanel() {
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        songNameLabel = new JLabel("Song Name: ", SwingConstants.CENTER);
        songLikesLabel = new JLabel("Likes: 0", SwingConstants.CENTER);
        songDislikesLabel = new JLabel("Dislikes: 0", SwingConstants.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton likeButton = new JButton("Like");
        JButton dislikeButton = new JButton("Dislike");

        likeButton.addActionListener(e -> {
            try {
                listener.updateReaction(songId, "like");
                refreshLikedSongs(); // Refresh the liked songs list dynamically
                fetchSongDetailsByName(songNameLabel.getText().replace("Song Name: ", ""));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating reaction: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dislikeButton.addActionListener(e -> {
            try {
                listener.updateReaction(songId, "dislike");
                refreshLikedSongs(); // Refresh the liked songs list dynamically
                fetchSongDetailsByName(songNameLabel.getText().replace("Song Name: ", ""));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating reaction: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        buttonPanel.add(likeButton);
        buttonPanel.add(dislikeButton);

        detailPanel.add(songNameLabel);
        detailPanel.add(songLikesLabel);
        detailPanel.add(songDislikesLabel);
        detailPanel.add(buttonPanel);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        detailPanel.add(backButton);

        return detailPanel;
    }

    private void searchSong() {
        String searchTerm = searchBar.getText();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a song name.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            var resultSet = listener.searchMusic(searchTerm);
            if (resultSet.next()) {
                songId = resultSet.getInt("MusicID");
                fetchSongDetailsByName(resultSet.getString("Name"));
            } else {
                JOptionPane.showMessageDialog(this, "No songs found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error searching for song: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchSongDetailsByName(String songName) {
        try {
            var resultSet = listener.getSongDetails(songName);
            if (resultSet.next()) {
                songNameLabel.setText("Song Name: " + resultSet.getString("Name"));
                songLikesLabel.setText("Likes: " + resultSet.getInt("Likes"));
                songDislikesLabel.setText("Dislikes: " + resultSet.getInt("Dislikes"));
                cardLayout.show(mainPanel, "SongDetail");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching song details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateReaction(String reactionType) {
        try {
            // Get the current reaction for the song
            String currentReaction = listener.getCurrentReaction(songId);

            // Determine the new reaction
            if (currentReaction == null) {
                // No reaction exists, so add a new one
                listener.updateReaction(songId, reactionType);
            } else if (!currentReaction.equals(reactionType)) {
                // Reaction exists but is different; update to the new reaction
                listener.updateReaction(songId, reactionType);
            } else {
                // Reaction exists and is the same; remove the reaction (optional)
                listener.updateReaction(songId, null); // Null reaction to remove
            }

            // Refresh UI
            fetchSongDetailsByName(songNameLabel.getText().replace("Song Name: ", ""));
            refreshLikedSongs(); // Update liked songs list
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating reaction: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void showPlaylistDetail(String playlistName) {
        songsInPlaylistModel.clear(); // Clear existing songs in the model
        try {
            List<String> songs = listener.getSongsInPlaylist(playlistName);
            for (String song : songs) {
                songsInPlaylistModel.addElement(song);
            }
            // Switch to the Playlist Detail panel
            cardLayout.show(mainPanel, "PlaylistDetail");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching songs: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void refreshLikedSongs() {
        DefaultListModel<String> likedSongsModel = (DefaultListModel<String>) likedSongsList.getModel();
        likedSongsModel.clear(); // Clear the current liked songs

        try {
            for (String song : listener.getLikedSongs()) {
                likedSongsModel.addElement(song); // Add updated liked songs
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error refreshing liked songs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    public static void main(String[] args) {
        new ListenerView(1);
    }
}
