import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Listener extends User{

    private ArrayList<Playlist> playlists;
    public Listener(int userId, String username, int age) {
        super(userId, username, age);
        this.playlists = new ArrayList<>();
    }

    // Method to create a new playlist
    public void createPlaylist(String playlistName) {
        String query = "INSERT INTO PLAYLIST (ListenerID, PlaylistName, CreationDate, IsPublic) VALUES (?, ?, CURDATE(), TRUE)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, getUserId()); // Use the Listener's UserID as ListenerID
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

    // Method to view all available music
    public void viewMusic() {
        String query = "SELECT MusicID, Name, Explicit FROM MUSIC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("Available Music:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("Name") +
                        (rs.getBoolean("Explicit") ? " (Explicit)" : ""));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to search for music by name
    //Şimdilik sadece music var mı explicit mi onu gösteriyor diğer özellikler de eklenebilir
    public void searchMusic(String musicName) {
        String query = "SELECT MusicID, Name, Explicit FROM MUSIC WHERE Name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + musicName + "%"); // Search with wildcards
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Search Results for \"" + musicName + "\":");
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println("- " + rs.getString("Name") +
                            (rs.getBoolean("Explicit") ? " (Explicit)" : ""));
                }
                if (!found) {
                    System.out.println("No music found matching your search.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
