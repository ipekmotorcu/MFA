package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Album {
    private int albumID;
    private String albumName;
    private ArrayList<Music> arrMusic;


    public Album(int albumID, String albumName) {
        this.albumID = albumID;
        this.albumName = albumName;
        arrMusic = new ArrayList<>();
    }


    public Album(int albumID, String albumName, ArrayList<Music> arrMusic) {
        this.albumID = albumID;
        this.albumName = albumName;
        this.arrMusic = arrMusic;
    }

    // Getters and setters
    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public ArrayList<Music> getArrMusic() {
        return arrMusic;
    }

    public void addMusic(Music music) {
        arrMusic.add(music);
    }

    public void removeMusic(Music music) {
        arrMusic.remove(music);
    }

    public void addMusicToAlbum(int albumId, String musicName, int categoryId, boolean isExplicit, int playCount, LocalDate releaseDate) {
        String checkAlbumQuery = "SELECT AlbumID FROM ALBUM WHERE AlbumID = ?";
        String insertMusicQuery = "INSERT INTO MUSIC (Name, AlbumID, CategoryID, Explicit, PlayCount, ReleaseDate) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        String updateTracksCountQuery = "UPDATE ALBUM SET TracksCount = TracksCount + 1 WHERE AlbumID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkAlbumStmt = conn.prepareStatement(checkAlbumQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertMusicQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateTracksCountQuery)) {

            // Check if the AlbumID exists
            checkAlbumStmt.setInt(1, albumId);
            ResultSet rs = checkAlbumStmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("AlbumID " + albumId + " does not exist in the ALBUM table.");
            }

            // Insert new music into MUSIC table
            insertStmt.setString(1, musicName);
            insertStmt.setInt(2, albumId);
            insertStmt.setInt(3, categoryId);
            insertStmt.setBoolean(4, isExplicit);
            insertStmt.setInt(5, playCount);
            insertStmt.setDate(6, java.sql.Date.valueOf(releaseDate));
            insertStmt.executeUpdate();

            // Update TracksCount in ALBUM table
            updateStmt.setInt(1, albumId);
            updateStmt.executeUpdate();

            System.out.println("database.Music added to album successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMusicFromAlbum(int musicID, int albumID) {
        String checkMusicQuery = "SELECT MusicID, AlbumID FROM MUSIC WHERE MusicID = ? AND AlbumID = ?";
        String deleteMusicQuery = "DELETE FROM MUSIC WHERE MusicID = ? AND AlbumID = ?";
        String updateTracksCountQuery = "UPDATE ALBUM SET TracksCount = TracksCount - 1 WHERE AlbumID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkMusicStmt = conn.prepareStatement(checkMusicQuery);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteMusicQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateTracksCountQuery)) {

            // Check if the music exists in the specified album
            checkMusicStmt.setInt(1, musicID);
            checkMusicStmt.setInt(2, albumID);
            ResultSet rs = checkMusicStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("No music found with ID " + musicID + " in album ID " + albumID);
                return; // Exit if the music is not found in the album
            }

            // Delete music from the MUSIC table
            deleteStmt.setInt(1, musicID);
            deleteStmt.setInt(2, albumID);
            int rowsDeleted = deleteStmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("database.Music with ID " + musicID + " successfully deleted from album ID " + albumID);

                // Update TracksCount in ALBUM table
                updateStmt.setInt(1, albumID);
                updateStmt.executeUpdate();
            } else {
                System.out.println("Failed to delete music with ID " + musicID + " from album ID " + albumID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



