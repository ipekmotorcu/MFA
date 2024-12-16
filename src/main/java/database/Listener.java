package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Listener  {
    private Connection connection;
    private int userId;

    public Listener(Connection connection, int userId) {
        this.connection = connection;
        this.userId = userId;
    }

    // 1. Search for music by name
    public ResultSet searchMusic(String musicName) throws SQLException {
        String query = "SELECT MusicID, Name FROM MUSIC WHERE Name LIKE ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, "%" + musicName + "%");
        return statement.executeQuery();
    }

    // 2. Fetch liked songs
    public List<String> getLikedSongs() throws SQLException {
        List<String> likedSongs = new ArrayList<>();
        String query = "SELECT DISTINCT m.Name " +
                "FROM MUSIC m " +
                "JOIN REACTIONS r ON m.MusicID = r.MusicID " +
                "WHERE r.UserID = ? AND r.ReactionType = 'like'";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            likedSongs.add(resultSet.getString("Name"));
        }
        return likedSongs;
    }

    // 3. Fetch playlists
    public List<String> getPlaylists() throws SQLException {
        List<String> playlists = new ArrayList<>();
        String query = "SELECT PlaylistName FROM PLAYLIST WHERE ListenerID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            playlists.add(resultSet.getString("PlaylistName"));
        }
        return playlists;
    }

    // 4. Fetch songs in a playlist
    public List<String> getSongsInPlaylist(String playlistName) throws SQLException {
        List<String> songs = new ArrayList<>();
        String query = "SELECT m.Name " +
                "FROM PLAYLIST p " +
                "JOIN PLAYLIST_MUSIC pm ON p.PlaylistID = pm.PlaylistID " +
                "JOIN MUSIC m ON pm.MusicID = m.MusicID " +
                "WHERE p.PlaylistName = ? AND p.ListenerID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, playlistName);
        statement.setInt(2, userId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            songs.add(resultSet.getString("Name"));
        }
        return songs;
    }

    // 5. Create a new playlist
    public void createPlaylist(String playlistName) throws SQLException {
        String query = "INSERT INTO PLAYLIST (ListenerID, PlaylistName, CreationDate) VALUES (?, ?, CURDATE())";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.setString(2, playlistName);
        statement.executeUpdate();
    }

    // 6. Add a song to a playlist
    public void addSongToPlaylist(String playlistName, String songName) throws SQLException {
        String query = "INSERT INTO PLAYLIST_MUSIC (PlaylistID, MusicID, DateAdded) " +
                "SELECT p.PlaylistID, m.MusicID, CURDATE() " +
                "FROM PLAYLIST p, MUSIC m " +
                "WHERE p.PlaylistName = ? AND p.ListenerID = ? AND m.Name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, playlistName);
        statement.setInt(2, userId);
        statement.setString(3, songName);
        statement.executeUpdate();
    }

    // 7. Remove a song from a playlist
    public void removeSongFromPlaylist(String playlistName, String songName) throws SQLException {
        String query = "DELETE pm " +
                "FROM PLAYLIST_MUSIC pm " +
                "JOIN PLAYLIST p ON pm.PlaylistID = p.PlaylistID " +
                "JOIN MUSIC m ON pm.MusicID = m.MusicID " +
                "WHERE p.PlaylistName = ? AND p.ListenerID = ? AND m.Name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, playlistName);
        statement.setInt(2, userId);
        statement.setString(3, songName);
        statement.executeUpdate();
    }

    // 8. Fetch song details
    public ResultSet getSongDetails(String songName) throws SQLException {
        String query = "SELECT m.MusicID, m.Name, " +
                "SUM(CASE WHEN r.ReactionType = 'like' THEN 1 ELSE 0 END) AS Likes, " +
                "SUM(CASE WHEN r.ReactionType = 'dislike' THEN 1 ELSE 0 END) AS Dislikes " +
                "FROM MUSIC m " +
                "LEFT JOIN REACTIONS r ON m.MusicID = r.MusicID " +
                "WHERE m.Name = ? GROUP BY m.MusicID, m.Name";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, songName);
        return statement.executeQuery();
    }

    // 9. Update reactions
    public void updateReaction(int songId, String reactionType) throws SQLException {
        String currentReaction = getCurrentReaction(songId); // Fetch the current reaction

        if (currentReaction == null) {
            // No existing reaction, insert a new one
            insertReaction(songId, reactionType);
        } else if (currentReaction.equals(reactionType)) {
            // Reaction exists and matches the clicked button -> Remove it
            removeReaction(songId);
        } else {
            // Reaction exists but is different -> Update it
            insertReaction(songId, reactionType);
        }
    }

    // Insert or update the reaction
    private void insertReaction(int songId, String reactionType) throws SQLException {
        String query = "INSERT INTO REACTIONS (UserID, MusicID, ReactionType) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE ReactionType = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, songId);
            statement.setString(3, reactionType);
            statement.setString(4, reactionType);
            statement.executeUpdate();
        }
    }

    // Remove the reaction
    private void removeReaction(int songId) throws SQLException {
        String query = "DELETE FROM REACTIONS WHERE UserID = ? AND MusicID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, songId);
            statement.executeUpdate();
        }
    }

    // Fetch the current reaction for a song
    public String getCurrentReaction(int songId) throws SQLException {
        String query = "SELECT ReactionType FROM REACTIONS WHERE UserID = ? AND MusicID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, songId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("ReactionType"); // Return existing reaction
            }
        }
        return null; // No reaction exists
    }

    public int getMusicIdByName(String songName) throws SQLException {
        String query = "SELECT MusicID FROM MUSIC WHERE Name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, songName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("MusicID");
            }
        }
        return -1; // Return -1 if the song is not found
    }

}
