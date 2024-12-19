package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ArtistView extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTable songTable;
    private DefaultTableModel songTableModel;
    private JPanel albumDetailPanel;
    private JPanel dashboardPanel;
    private JLabel totalPlaysLabel;
    private JLabel totalLikesLabel;
    private JLabel totalReactionsLabel;

    int artistId;

    public ArtistView(int artistId) {
        this.artistId = artistId;

        // Frame setup
        setTitle("Artist Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create main panels
        dashboardPanel = createDashboardPanel();
        albumDetailPanel = createAlbumDetailPanel();

        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(albumDetailPanel, "AlbumDetail");

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createDashboardPanel() {
        JPanel dashboard = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Artist Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        dashboard.add(titleLabel, BorderLayout.NORTH);

        // Center panel with song table
        JPanel centerPanel = new JPanel(new BorderLayout());
        createSongTable();
        JScrollPane tableScroll = new JScrollPane(songTable);
        centerPanel.add(tableScroll, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton createAlbumButton = new JButton("Create Album");
        JButton modifyAlbumButton = new JButton("Modify Album");
        JButton uploadSongButton = new JButton("Upload Song");

        createAlbumButton.addActionListener(e -> showCreateAlbumDialog());
        modifyAlbumButton.addActionListener(e -> showModifyAlbumDialog());
        uploadSongButton.addActionListener(e -> showUploadSongDialog());

        buttonPanel.add(createAlbumButton);
        buttonPanel.add(modifyAlbumButton);
        buttonPanel.add(uploadSongButton);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        dashboard.add(centerPanel, BorderLayout.CENTER);

        // Statistics panel
        JPanel statsPanel = createStatsPanel();
        dashboard.add(statsPanel, BorderLayout.EAST);

        return dashboard;
    }

    private void createSongTable() {
        String[] columnNames = {"Title", "Plays", "Likes", "Reactions", "Album"};
        songTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Example data - in real application, this would come from database
        songTableModel.addRow(new Object[]{"Song 1", "100", "50", "10", "Album 1"});
        songTableModel.addRow(new Object[]{"Song 2", "200", "75", "15", "Album 2"});

        songTable = new JTable(songTableModel);
        songTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = songTable.getSelectedRow();
                if (selectedRow != -1) {
                    showSongDetails(selectedRow);
                }
            }
        });
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

        totalPlaysLabel = new JLabel("Total Plays: 300");
        totalLikesLabel = new JLabel("Total Likes: 125");
        totalReactionsLabel = new JLabel("Total Reactions: 25");
        JButton refreshStatsButton = new JButton("Refresh Stats");

        refreshStatsButton.addActionListener(e -> refreshStatistics());

        statsPanel.add(totalPlaysLabel);
        statsPanel.add(totalLikesLabel);
        statsPanel.add(totalReactionsLabel);
        statsPanel.add(refreshStatsButton);

        return statsPanel;
    }

    private JPanel createAlbumDetailPanel() {
        JPanel albumPanel = new JPanel(new BorderLayout());

        // database.Album info
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Album Information"));
        infoPanel.add(new JLabel("Album Name:"));
        infoPanel.add(new JTextField(20));
        infoPanel.add(new JLabel("Release Date:"));
        infoPanel.add(new JTextField(20));
        infoPanel.add(new JLabel("Genre:"));
        infoPanel.add(new JTextField(20));

        albumPanel.add(infoPanel, BorderLayout.NORTH);

        // Song list in album
        DefaultListModel<String> songListModel = new DefaultListModel<>();
        JList<String> songList = new JList<>(songListModel);
        JScrollPane songScrollPane = new JScrollPane(songList);
        albumPanel.add(songScrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save Changes");
        JButton backButton = new JButton("Back to Dashboard");

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        saveButton.addActionListener(e -> saveAlbumChanges());

        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        albumPanel.add(buttonPanel, BorderLayout.SOUTH);

        return albumPanel;
    }

    private void showCreateAlbumDialog() {
        String albumName = JOptionPane.showInputDialog(this, "Enter album name:");
        if (albumName != null && !albumName.isEmpty()) {
            // In real application, this would create a new album in the database
            JOptionPane.showMessageDialog(this, ".Album '" + albumName + "' created successfully!");
        }
    }

    private void showModifyAlbumDialog() {
        String[] albums = {"Album 1", "Album 2", "Album 3"}; // Would come from database
        String selectedAlbum = (String) JOptionPane.showInputDialog(
                this,
                "Select album to modify:",
                "Modify Album",
                JOptionPane.QUESTION_MESSAGE,
                null,
                albums,
                albums[0]);

        if (selectedAlbum != null) {
            cardLayout.show(mainPanel, "AlbumDetail");
        }
    }

    private void showUploadSongDialog() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String fileName = fileChooser.getSelectedFile().getName();
            // In real application, this would upload the song to the database
            JOptionPane.showMessageDialog(this, "Song '" + fileName + "' uploaded successfully!");
        }
    }

    private void showSongDetails(int rowIndex) {
        String songTitle = (String) songTable.getValueAt(rowIndex, 0);
        String plays = (String) songTable.getValueAt(rowIndex, 1);
        String likes = (String) songTable.getValueAt(rowIndex, 2);

        JOptionPane.showMessageDialog(this,
                "Song Details:\n" +
                        "Title: " + songTitle + "\n" +
                        "Plays: " + plays + "\n" +
                        "Likes: " + likes,
                "Song Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshStatistics() {
        // In real application, this would fetch updated stats from the database
        totalPlaysLabel.setText("Total Plays: " + (int)(Math.random() * 1000));
        totalLikesLabel.setText("Total Likes: " + (int)(Math.random() * 500));
        totalReactionsLabel.setText("Total Reactions: " + (int)(Math.random() * 100));
    }

    private void saveAlbumChanges() {
        // In real application, this would save changes to the database
        JOptionPane.showMessageDialog(this, "Album changes saved successfully!");
        cardLayout.show(mainPanel, "Dashboard");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ArtistView(1));
    }
}


    /*public ArtistView() {
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
