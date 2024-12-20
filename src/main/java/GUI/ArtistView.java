package GUI;

import database.Artist;
import database.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

    public class ArtistView extends JFrame {
        private CardLayout cardLayout;
        private JPanel mainPanel;
        private JTable songTable;
        private DefaultTableModel songTableModel;
        private Connection connection;
        private Artist artist;
        private int userId;
        private JComboBox<String> albumSelector;
        private String currentSelectedAlbum = null;

        public ArtistView(int userId) {
            this.userId = userId;

            // Database connection
            connectToDatabase();
            artist = new Artist(connection, userId);

            // Frame setup
            setTitle("Artist Dashboard");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 600);

            cardLayout = new CardLayout();
            mainPanel = new JPanel(cardLayout);

            // Create main panels
            JPanel dashboardPanel = createDashboardPanel();
            JPanel addSongPanel = createAddSongPanel();

            mainPanel.add(dashboardPanel, "Dashboard");
            mainPanel.add(addSongPanel, "AddSong");

            add(mainPanel);
            setVisible(true);

            // Initial data load
            refreshDashboard();
        }

        private void connectToDatabase() {
            try {
                connection = DBConnection.getConnection();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
        private JPanel createAddSongPanel() {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;

            // Form fields
            JTextField songNameField = new JTextField(20);
            JTextField albumNameField = new JTextField(20);
            JTextField categoryField = new JTextField(20);
            JCheckBox explicitCheck = new JCheckBox("Explicit Content");

            // Add components to panel
            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("Song Name:"), gbc);
            gbc.gridx = 1;
            panel.add(songNameField, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("Album:"), gbc);
            gbc.gridx = 1;
            panel.add(albumNameField, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            panel.add(new JLabel("Category:"), gbc);
            gbc.gridx = 1;
            panel.add(categoryField, gbc);

            gbc.gridx = 1; gbc.gridy = 3;
            panel.add(explicitCheck, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel();
            JButton addButton = new JButton("Add Song");
            JButton cancelButton = new JButton("Cancel");

            addButton.addActionListener(e -> {
                try {
                    String songName = songNameField.getText();
                    String albumName = albumNameField.getText();
                    String category = categoryField.getText();

                    if (songName.isEmpty() || albumName == null || category.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please fill all fields");
                        return;
                    }

                    artist.addMusic(songName, albumName, category);
                    JOptionPane.showMessageDialog(this, "Song added successfully!");
                    refreshDashboard();
                    cardLayout.show(mainPanel, "Dashboard");

                    // Clear fields
                    songNameField.setText("");
                    categoryField.setText("");
                    explicitCheck.setSelected(false);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error adding song: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

            buttonPanel.add(addButton);
            buttonPanel.add(cancelButton);

            gbc.gridx = 0; gbc.gridy = 4;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(buttonPanel, gbc);

            return panel;
        }
        private void createSongTable() {
            String[] columnNames = {"Music ID", "Title", "Album", "Category", "Play Count", "Release Date"};
            songTableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            songTable = new JTable(songTableModel);
            songTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }


        private JPanel createDashboardPanel() {
            JPanel dashboard = new JPanel(new BorderLayout());

            // Title
            JLabel titleLabel = new JLabel("Artist Dashboard", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            dashboard.add(titleLabel, BorderLayout.NORTH);

            // Album selection panel
            JPanel albumPanel = new JPanel();
            albumPanel.setBorder(BorderFactory.createTitledBorder("Select Album"));
            albumSelector = new JComboBox<>();
            refreshAlbumSelector();

            albumSelector.addActionListener(e -> {
                currentSelectedAlbum = (String) albumSelector.getSelectedItem();
                refreshSongTable(currentSelectedAlbum);
            });

            albumPanel.add(new JLabel("Album: "));
            albumPanel.add(albumSelector);

            // Center panel with song table
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(albumPanel, BorderLayout.NORTH);

            createSongTable();
            JScrollPane tableScroll = new JScrollPane(songTable);
            centerPanel.add(tableScroll, BorderLayout.CENTER);

            // Action buttons
            JPanel buttonPanel = new JPanel();
            JButton createAlbumButton = new JButton("Create Album");
            JButton addSongButton = new JButton("Add Song to Album");
            JButton removeSongButton = new JButton("Remove Song from Album");
            JButton viewStatistics = new JButton("View Statistics");

            createAlbumButton.addActionListener(e -> showCreateAlbumDialog());
            addSongButton.addActionListener(e -> {
                if (currentSelectedAlbum == null) {
                    JOptionPane.showMessageDialog(this, "Please select an album first");
                    return;
                }
                showAddSongDialog(currentSelectedAlbum);
            });

            removeSongButton.addActionListener(e -> {
                if (currentSelectedAlbum == null) {
                    JOptionPane.showMessageDialog(this, "Please select an album first");
                    return;
                }
                showRemoveSongDialog(currentSelectedAlbum);
            });

            viewStatistics.addActionListener(e -> showViewStatisticsDialog());
            addSongButton.addActionListener(e -> {
                if (currentSelectedAlbum == null) {
                    JOptionPane.showMessageDialog(this, "Please select an album first");
                    return;
                }
                showAddSongDialog(currentSelectedAlbum);
            });

            buttonPanel.add(createAlbumButton);
            buttonPanel.add(addSongButton);
            buttonPanel.add(removeSongButton);
            buttonPanel.add(viewStatistics);
            centerPanel.add(buttonPanel, BorderLayout.SOUTH);

            dashboard.add(centerPanel, BorderLayout.CENTER);

            return dashboard;
        }

        private void showAddSongDialog(String albumName) {
            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

            JTextField songNameField = new JTextField();
            JTextField categoryField = new JTextField();
            JCheckBox explicitCheck = new JCheckBox();

            panel.add(new JLabel("Song Name:"));
            panel.add(songNameField);
            panel.add(new JLabel("Category:"));
            panel.add(categoryField);
            panel.add(new JLabel("Explicit:"));
            panel.add(explicitCheck);

            int result = JOptionPane.showConfirmDialog(this, panel,
                    "Add Song to " + albumName,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String songName = songNameField.getText();
                String category = categoryField.getText();

                if (songName.isEmpty() || category.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields");
                    return;
                }

                try {
                    artist.addMusic(songName, albumName, category);
                    refreshSongTable(albumName);
                    JOptionPane.showMessageDialog(this, "Song added successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error adding song: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        private void showCreateAlbumDialog() {
            String albumName = JOptionPane.showInputDialog(this, "Enter album name:");
            if (albumName != null && !albumName.trim().isEmpty()) {
                try {
                    artist.createAlbum(albumName);
                    refreshDashboard();
                    JOptionPane.showMessageDialog(this, "Album created successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error creating album: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void showViewStatisticsDialog() {
            String albumName = JOptionPane.showInputDialog(this, "Enter album name:");

            if (albumName != null && !albumName.trim().isEmpty()) {
                try {
                    ResultSet stats = artist.viewStatistics(albumName);

                    if (stats.next()) {
                        int trackCount = stats.getInt("TrackCount");
                        int totalPlays = stats.getInt("TotalPlays");

                        // Create a formatted message
                        String message = String.format("""
                    Statistics for album '%s':
                    
                    Number of Tracks: %d
                    Total Plays: %d
                    Average Plays per Track: %.2f""",
                                albumName,
                                trackCount,
                                totalPlays,
                                totalPlays / (double) trackCount
                        );

                        // Show results in a dialog
                        JOptionPane.showMessageDialog(this,
                                message,
                                "Album Statistics",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "No statistics found for album: " + albumName,
                                "No Data",
                                JOptionPane.WARNING_MESSAGE);
                    }

                    stats.close();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error viewing statistics: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void showRemoveSongDialog(String albumName) {
            int selectedRow = songTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a song to remove");
                return;
            }

            String songName = (String) songTable.getValueAt(selectedRow, 1);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove '" + songName + "' from album '" + albumName + "'?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    artist.removeMusic(songName);
                    refreshSongTable(albumName);
                    JOptionPane.showMessageDialog(this, "Song removed successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error removing song: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void refreshAlbumSelector() {
            albumSelector.removeAllItems();
            try {
                for (String album : artist.getAlbums()) {
                    albumSelector.addItem(album);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error loading albums: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void refreshSongTable(String albumName) {
            songTableModel.setRowCount(0);
            if (albumName == null) return;

            try {
                // You'll need to add this method to your Artist class
                ResultSet songs = artist.getSongsInAlbum(albumName);
                while (songs.next()) {
                    songTableModel.addRow(new Object[]{
                            songs.getInt("MusicID"),
                            songs.getString("Name"),
                            albumName,
                            songs.getString("CategoryName"),
                            songs.getInt("PlayCount"),
                            songs.getDate("ReleaseDate")
                    });
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error loading songs: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void refreshDashboard() {
            refreshAlbumSelector();
            if (currentSelectedAlbum != null) {
                refreshSongTable(currentSelectedAlbum);
            }
        }
    }

/*
    public ArtistView() {
        // Frame
        setTitle("database.Artist Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("database.Artist Dashboard", SwingConstants.CENTER);
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
        JButton createAlbumButton = new JButton("Create database.Album");
        JButton modifyAlbum = new JButton("Modify database.Album");
        buttonPanel.add(createAlbumButton);
        buttonPanel.add(modifyAlbum);;
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
}*/
