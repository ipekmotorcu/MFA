import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {
    private int userId;
    private String username;
    private int age;

    private ArrayList<Playlist> playlists;
    public User(int userId, String username, int age) {
        this.userId = userId;
        this.username = username;
        this.age = age;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void createPlaylist(String playlistName) {
        String query = "INSERT INTO PLAYLIST (ListenerID, PlaylistName, CreationDate, IsPublic) VALUES (?, ?, CURDATE(), TRUE)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, getUserId()); // Use the database.Listener's UserID as ListenerID
            stmt.setString(2, playlistName);
            stmt.executeUpdate();
            System.out.println("Playlist created successfully: " + playlistName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add music to a playlist
    public void addMusicToPlaylist(int playlistId, int musicId) {
        String query = "INSERT INTO PLAYLIST_MUSIC (PlaylistID, MusicID, DateAdded) VALUES (?, ?, CURDATE())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, playlistId);
            stmt.setInt(2, musicId);
            stmt.executeUpdate();
            System.out.println("Music added to playlist successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to remove music from a playlist
    public void removeMusicFromPlaylist(int playlistId, int musicId) {
        String query = "DELETE FROM PLAYLIST_MUSIC WHERE PlaylistID = ? AND MusicID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, playlistId);
            stmt.setInt(2, musicId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Music removed from playlist successfully.");
            } else {
                System.out.println("Music not found in playlist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
