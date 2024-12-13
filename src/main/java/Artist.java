import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Artist extends User{
    private ArrayList<String> arrAlbum;
    public Artist(int userId, String username, int age) {
        super(userId, username, age);
        this.arrAlbum = new ArrayList<>();
    }

    // Method to create a playlist (stub for now, can be expanded)
    public void createPlaylist() {
        System.out.println("Artist " + getUsername() + " created a new playlist.");
    }

    // Method to add music to an album
    public void addMusic(String albumName, String music) {
        if (arrAlbum.contains(albumName)) {
            System.out.println("Artist " + getUsername() + " added music \"" + music + "\" to album \"" + albumName + "\".");
            // Logic for adding the song to the album
        } else {
            System.out.println("Album \"" + albumName + "\" not found for artist " + getUsername() + ".");
        }
    }

    // Method to remove music from an album
    public void removeMusic(String albumName, String music) {
        if (arrAlbum.contains(albumName)) {
            System.out.println("Artist " + getUsername() + " removed music \"" + music + "\" from album \"" + albumName + "\".");
            // Logic for removing the song from the album
        } else {
            System.out.println("Album \"" + albumName + "\" not found for artist " + getUsername() + ".");
        }
    }

    // Method to create a new album
    public void createMusic(String albumName) {
        if (!arrAlbum.contains(albumName)) {
            arrAlbum.add(albumName);
            System.out.println("Artist " + getUsername() + " created a new album: \"" + albumName + "\".");
        } else {
            System.out.println("Album \"" + albumName + "\" already exists for artist " + getUsername() + ".");
        }
    }
    public void createMusic(String musicName, int albumId, double duration, String category, boolean explicit) {
        String query = "INSERT INTO MUSIC (Name, AlbumID, CategoryID, Explicit, PlayCount, ReleaseDate) " +
                "VALUES (?, ?, (SELECT CategoryID FROM CATEGORY WHERE CategoryName = ?), ?, 0, CURDATE())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, musicName);
            stmt.setInt(2, albumId);
            stmt.setString(3, category);
            stmt.setBoolean(4, explicit);
            stmt.executeUpdate();
            System.out.println("Music added successfully: " + musicName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete an album
    public void deleteMusic(String albumName) {
        if (arrAlbum.contains(albumName)) {
            arrAlbum.remove(albumName);
            System.out.println("Artist " + getUsername() + " deleted the album: \"" + albumName + "\".");
        } else {
            System.out.println("Album \"" + albumName + "\" not found for artist " + getUsername() + ".");
        }
    }
    public void deleteMusic(int musicId) {
        String query = "DELETE FROM MUSIC WHERE MusicID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, musicId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Music deleted successfully.");
            } else {
                System.out.println("Music not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view artist's album statistics
   /* public void viewStatistics() {
        System.out.println("Statistics for Artist: " + getUsername());
        System.out.println("Number of albums: " + arrAlbum.size());
        System.out.println("Albums: " + arrAlbum);
    }*/
    //ben bunu şimdilik yazdım bu geliştirilebilir
    public void viewStatistics() {
        String query = "SELECT AlbumName, ReleaseDate FROM ALBUM WHERE ArtistID = ?";
        int albumCount = 0;

        System.out.println("Statistics for Artist: " + getUsername());

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, getUserId()); // Use the Artist's UserID as ArtistID
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Albums:");
                while (rs.next()) {
                    albumCount++;
                    String albumName = rs.getString("AlbumName");
                    String releaseDate = rs.getString("ReleaseDate");
                    System.out.println("- " + albumName + " (Released on: " + releaseDate + ")");
                }
            }

            System.out.println("Number of albums: " + albumCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
