package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Artist  {
    private ArrayList<String> arrAlbum;
    private int userId;
    private Connection connection;

    public Artist(Connection connection, int userId) {
        this.connection = connection;
        this.userId = userId;
        this.arrAlbum = new ArrayList<>();
    }

    public void createAlbum(String albumName) throws SQLException {
        String query = "INSERT INTO ALBUM (ArtistID, AlbumName, ReleaseDate, TracksCount)\n" +
                "VALUES(?, ?, CURRENT_DATE, 0);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.setString(2, albumName);
        statement.executeUpdate();
    }


    // Method to add music to an album, explicit label in default 0
    public void addMusic(String musicName, String albumName, String categoryName) throws SQLException {
        String query = "INSERT INTO MUSIC\n" +
                "( Name, AlbumID, CategoryID, Explicit, PlayCount, ReleaseDate)\n" +
                "VALUES(?, ?, ?, 0, 0, CURRENT_DATE);";
        String updateTrackCountQuery = "UPDATE ALBUM SET TracksCount = TracksCount + 1 WHERE AlbumID = ?;";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, musicName);
        statement.setInt(2, getAlbumIdByName(albumName));
        statement.setInt(3, getCategoryIdByName(categoryName));
        statement.executeUpdate();

        PreparedStatement updateTrackCountStatement = connection.prepareStatement(updateTrackCountQuery);
        updateTrackCountStatement.setInt(1, getAlbumIdByName(albumName));
        updateTrackCountStatement.executeUpdate();
    }

    // Method to remove music from an album
    public void removeMusic(String music) throws SQLException {
        String getMusicIdQuery = "SELECT MusicID, AlbumID FROM MUSIC WHERE Name = ?;";
        String deleteMusicQuery = "DELETE FROM MUSIC WHERE MusicID = ?;";
        String updateTrackCountQuery = "UPDATE ALBUM SET TracksCount = TracksCount - 1 WHERE AlbumID = ?;";

        int musicId = -1;
        int albumId = -1;

        // Retrieve MusicID and AlbumID before deleting
        PreparedStatement getMusicIdStatement = connection.prepareStatement(getMusicIdQuery);
        getMusicIdStatement.setString(1, music);
        ResultSet resultSet = getMusicIdStatement.executeQuery();
        if (resultSet.next()) {
            musicId = resultSet.getInt("MusicID");
            albumId = resultSet.getInt("AlbumID");
        } else {
            throw new SQLException("Music not found: " + music);
        }

        // Delete the music
        PreparedStatement deleteMusicStatement = connection.prepareStatement(deleteMusicQuery);
        deleteMusicStatement.setInt(1, musicId);
        deleteMusicStatement.executeUpdate();

        // Update TracksCount
        PreparedStatement updateTrackCountStatement = connection.prepareStatement(updateTrackCountQuery);
        updateTrackCountStatement.setInt(1, albumId);
        updateTrackCountStatement.executeUpdate();



        //needs to update track count in album
    }

    public ResultSet getSongsInAlbum(String albumName) throws SQLException {
        String query = "SELECT m.MusicID, m.Name, c.CategoryName, m.PlayCount, m.ReleaseDate " +
                "FROM MUSIC m " +
                "JOIN CATEGORY c ON m.CategoryID = c.CategoryID " +
                "WHERE m.AlbumID = (SELECT AlbumID FROM ALBUM WHERE AlbumName = ? AND ArtistID = ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, albumName);
        statement.setInt(2, userId);
        return statement.executeQuery();
    }

    public ArrayList<String> getAlbums() throws SQLException {
        String query = "SELECT a.AlbumName FROM ALBUM a WHERE a.ArtistID= ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> albums = new ArrayList<>();
        while (resultSet.next()) {
            albums.add(resultSet.getString("AlbumName"));
        }
        return albums;
    }

    public ResultSet viewStatistics(String albumName) throws SQLException {
        //it will be added
       String query = "SELECT COUNT(DISTINCT MusicID) AS TrackCount, SUM(PlayCount) AS TotalPlays " +
               "FROM MUSIC m WHERE m.AlbumID = ?";
       PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, getAlbumIdByName(albumName));
        return statement.executeQuery();

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

    public int getLikeByMusicId(int musicId) throws SQLException {
        String query = "SELECT COUNT(ReactionType) AS likeCount FROM REACTIONS   WHERE MusicID=?  AND ReactionType='like';";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, musicId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("likeCount");
            }
        }
        return -1;
    }
    public int getDislikeByMusicID(int musicId) throws SQLException {
        String query = "SELECT COUNT(r.ReactionType) AS dislikeCount FROM REACTIONS r  WHERE r.MusicID=?  AND r.ReactionType='dislike';";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, musicId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("dislikeCount");
            }
        }
         return -1;
    }
    public int getTotalLikeByAlbumID(int albumId) throws SQLException {
        String query = "SELECT COUNT(r.ReactionType) AS likeCount " +
                "FROM reactions r " +
                "JOIN music m ON r.MusicID = m.MusicID " +
                "WHERE m.AlbumID = ? " +
                "AND r.ReactionType = 'like';";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, albumId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("likeCount");
            }
        }
        return -1;
    }
    public int getTotalDisikeByAlbumID(int albumId) throws SQLException {
        String query = "SELECT COUNT(r.ReactionType) AS dislikeCount " +
                "FROM reactions r " +
                "JOIN music m ON r.MusicID = m.MusicID " +
                "WHERE m.AlbumID = ? " +
                "AND r.ReactionType = 'dislike';";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, albumId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("dislikeCount");
            }
        }
        return -1;
    }

    // Method to view artist's album statistics
   /*
    //ben bunu şimdilik yazdım bu geliştirilebilir
    public void viewStatistics() {
        String query = "SELECT AlbumName, ReleaseDate FROM ALBUM WHERE ArtistID = ?";
        int albumCount = 0;

        System.out.println("Statistics for Artist: " + getUsername());

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, getUserId()); // Use the database.Artist's UserID as ArtistID
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
    }*/

    public int getAlbumIdByName(String albumName) throws SQLException {
        String query = "SELECT AlbumID  FROM ALBUM WHERE AlbumName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, albumName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("AlbumID");
            }
        }
        return -1;
    }
    public int getCategoryIdByName(String categoryName) throws SQLException {
        String query = "SELECT CategoryID  FROM CATEGORY WHERE CategoryName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, categoryName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("CategoryID");
            }
        }
        return -1;
    }


}
