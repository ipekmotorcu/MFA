package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ListenerView extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField searchBar;
    private JList<String> likedSongsList;
    private JList<String> playlistList;
    private DefaultListModel<String> playlistModel;
    private DefaultListModel<String> songsInPlaylistModel;
    private JPanel songDetailPanel, playlistDetailPanel;
    private JLabel songNameLabel;

    int userid;

    public ListenerView(int userid) {
        // Frame setup
        this.userid = userid;
        setTitle("Listener Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Main panels
        JPanel dashboardPanel = createDashboardPanel();
        songDetailPanel = createSongDetailPanel();
        playlistDetailPanel = createPlaylistDetailPanel();

        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(songDetailPanel, "SongDetail");
        mainPanel.add(playlistDetailPanel, "PlaylistDetail");

        add(mainPanel);
        setVisible(true);
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

        searchButton.addActionListener(e -> showSongDetail("Searched Song"));

        // Liked Songs
        JPanel likedSongsPanel = new JPanel();
        likedSongsPanel.setBorder(BorderFactory.createTitledBorder("Liked Songs"));
        likedSongsList = new JList<>(new String[]{"Song 1", "Song 2", "Song 3"});
        likedSongsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        likedSongsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedSong = likedSongsList.getSelectedValue();
                showSongDetail(selectedSong);
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

        playlistList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedPlaylist = playlistList.getSelectedValue();
                if (selectedPlaylist != null) {
                    showPlaylistDetail(selectedPlaylist);
                }
            }
        });

        JButton createPlaylistButton = new JButton("Create Playlist");
        createPlaylistButton.addActionListener(e -> createNewPlaylist());
        playlistsPanel.add(createPlaylistButton);
        centerPanel.add(playlistsPanel);

        return dashboardPanel;
    }

    private JPanel createSongDetailPanel() {
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setPreferredSize(new Dimension(400, 300));

        songNameLabel = new JLabel("Song Name", SwingConstants.CENTER);
        songNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        songNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailPanel.add(songNameLabel);

        JLabel songPropertiesLabel = new JLabel("Properties: Genre, Length", SwingConstants.CENTER);
        songPropertiesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailPanel.add(songPropertiesLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton likeButton = new JButton("Like");
        JButton dislikeButton = new JButton("Dislike");
        JButton reactButton = new JButton("React");
        buttonPanel.add(likeButton);
        buttonPanel.add(dislikeButton);
        buttonPanel.add(reactButton);
        detailPanel.add(buttonPanel);

        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        detailPanel.add(backButton);

        detailPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return detailPanel;
    }

    private JPanel createPlaylistDetailPanel() {
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BorderLayout());

        JLabel playlistLabel = new JLabel("Playlist", SwingConstants.CENTER);
        playlistLabel.setFont(new Font("Arial", Font.BOLD, 18));
        detailPanel.add(playlistLabel, BorderLayout.NORTH);

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

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

        addSongButton.addActionListener(e -> {
            String songToAdd = JOptionPane.showInputDialog(this, "Enter song name to add:");
            if (songToAdd != null && !songToAdd.isEmpty()) {
                songsInPlaylistModel.addElement(songToAdd);
            }
        });

        removeSongButton.addActionListener(e -> {
            String selectedSong = songsInPlaylistList.getSelectedValue();
            if (selectedSong != null) {
                songsInPlaylistModel.removeElement(selectedSong);
            }
        });

        return detailPanel;
    }

    private void showSongDetail(String songName) {
        songNameLabel.setText("Song Name: " + songName);
        cardLayout.show(mainPanel, "SongDetail");
    }

    private void showPlaylistDetail(String playlistName) {
        songsInPlaylistModel.clear();
        // Example: Populate playlist with songs (in a real app, fetch from database)
        songsInPlaylistModel.addElement("Song A");
        songsInPlaylistModel.addElement("Song B");
        songsInPlaylistModel.addElement("Song C");
        cardLayout.show(mainPanel, "PlaylistDetail");
    }

    private void createNewPlaylist() {
        String playlistName = JOptionPane.showInputDialog(this, "Enter playlist name:");
        if (playlistName != null && !playlistName.isEmpty()) {
            playlistModel.addElement(playlistName);
        }
    }

    public static void main(String[] args) {
        new ListenerView(1);
    }
}
