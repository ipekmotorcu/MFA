import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin {
    private int adminId;
    private String adminName;

    public Admin(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
    }

    // Method to insert music into the database
    /*public void createMusic(String musicName, int albumId, double duration, String category, boolean explicit) {
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
    }*/

    // Method to delete music by its ID
   /* public void deleteMusic(int musicId) {
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
    }*/

    // Method to view music statistics
    public void viewStatistics() {
        String query = "SELECT Name, PlayCount FROM MUSIC ORDER BY PlayCount DESC LIMIT 10";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("Top 10 Most Played Music:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("Name") + " (Plays: " + rs.getInt("PlayCount") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to create a new Listener
    //Hem listener tablosuna hem de user tablosuna ekliyor ama user ve listener Id leri aynı olmuyor bu sıkıntı yaratmaz gibi geldi ama gerekirse düzeltebiliriz
    public void createListener(String username, int age) {
        String userQuery = "INSERT INTO USER (Username, Password, DateOfBirth, Age, UserType, IsBanned) " +
                "VALUES (?, 'default_password', CURDATE(), ?, 'Listener', FALSE)";
        String listenerQuery = "INSERT INTO LISTENER (UserID, TopPlayTime) VALUES (?, 0)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement userStmt = conn.prepareStatement(userQuery, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement listenerStmt = conn.prepareStatement(listenerQuery)) {

            // Insert into USER table
            userStmt.setString(1, username);
            userStmt.setInt(2, age);
            userStmt.executeUpdate();

            // Retrieve the generated UserID
            try (ResultSet generatedKeys = userStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    // Insert into LISTENER table
                    listenerStmt.setInt(1, userId);
                    listenerStmt.executeUpdate();

                    System.out.println("Listener created successfully: " + username);
                } else {
                    throw new SQLException("Creating user failed, no UserID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // User'ı bütün tablolardan siliyor,  ban user da olabilir bunun adı ama burada engellemek yerine siliyoruz
    public void deleteUser(int userId) {
        String deleteFromFollowers = "DELETE FROM FOLLOWERS WHERE UserID = ? OR ArtistID IN (SELECT ArtistID FROM ARTIST WHERE UserID = ?)";
        String deleteFromPlaylistMusic = "DELETE FROM PLAYLIST_MUSIC WHERE PlaylistID IN (SELECT PlaylistID FROM PLAYLIST WHERE ListenerID IN (SELECT ListenerID FROM LISTENER WHERE UserID = ?))";
        String deleteFromPlaylist = "DELETE FROM PLAYLIST WHERE ListenerID IN (SELECT ListenerID FROM LISTENER WHERE UserID = ?)";
        String deleteFromLittleListener = "DELETE FROM LITTLE_LISTENER WHERE ListenerID IN (SELECT ListenerID FROM LISTENER WHERE UserID = ?)";
        String deleteFromListener = "DELETE FROM LISTENER WHERE UserID = ?";
        String deleteFromArtist = "DELETE FROM ARTIST WHERE UserID = ?";
        String deleteFromUser = "DELETE FROM USER WHERE UserID = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Delete from FOLLOWERS
            try (PreparedStatement stmt = conn.prepareStatement(deleteFromFollowers)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, userId);
                stmt.executeUpdate();
            }

            // Delete from PLAYLIST_MUSIC
            try (PreparedStatement stmt = conn.prepareStatement(deleteFromPlaylistMusic)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // Delete from PLAYLIST
            try (PreparedStatement stmt = conn.prepareStatement(deleteFromPlaylist)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // Delete from LITTLE_LISTENER
            try (PreparedStatement stmt = conn.prepareStatement(deleteFromLittleListener)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // Delete from LISTENER
            try (PreparedStatement stmt = conn.prepareStatement(deleteFromListener)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // Delete from ARTIST
            try (PreparedStatement stmt = conn.prepareStatement(deleteFromArtist)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // Finally, delete from USER
            try (PreparedStatement stmt = conn.prepareStatement(deleteFromUser)) {
                stmt.setInt(1, userId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User deleted successfully.");
                } else {
                    System.out.println("User not found.");
                }
            }

            conn.commit(); // Commit transaction

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

