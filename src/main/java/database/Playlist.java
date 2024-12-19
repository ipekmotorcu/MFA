package database;

import database.DBConnection;
import database.Music;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Playlist {
    private int playListID;
    private String playListName;

    private ArrayList<Music> arrMusic;



    public Playlist(/*int playListID,*/ String playListName) {
        //this.playListID = playListID;
        this.playListName = playListName;
        this.arrMusic = new ArrayList<>();
    }

    public int getPlayListID() {
        return playListID;
    }

    public void setPlayListID(int playListID) {
        this.playListID = playListID;
    }

    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

    public ArrayList<Music> getArrMusic() {
        return arrMusic;
    }

    public void setArrMusic(ArrayList<Music> arrMusic) {
        this.arrMusic = arrMusic;
    }

    public void addMusic(Music music) {
        arrMusic.add(music);
    }

    public void removeMusic(Music music) {
        arrMusic.remove(music);
    }

    public void addMusicToPlaylist(int musicID, int playlistID, LocalDate dateAdded) {
        String checkPlaylistQuery = "SELECT COUNT(*) FROM PLAYLIST WHERE PlaylistID = ?";
        String checkMusicQuery = "SELECT COUNT(*) FROM MUSIC WHERE MusicID = ?";
        String checkDuplicateQuery = "SELECT COUNT(*) FROM PLAYLIST_MUSIC WHERE PlaylistID = ? AND MusicID = ?";
        String insertMusicQuery = "INSERT INTO PLAYLIST_MUSIC (PlaylistID, MusicID, DateAdded) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkPlaylistStmt = conn.prepareStatement(checkPlaylistQuery);
             PreparedStatement checkMusicStmt = conn.prepareStatement(checkMusicQuery);
             PreparedStatement checkDuplicateStmt = conn.prepareStatement(checkDuplicateQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertMusicQuery)) {

            // Check if PlaylistID exists
            checkPlaylistStmt.setInt(1, playlistID);
            try (ResultSet rs = checkPlaylistStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("Error: database.Playlist with ID " + playlistID + " does not exist.");
                    return;
                }
            }

            // Check if MusicID exists
            checkMusicStmt.setInt(1, musicID);
            try (ResultSet rs = checkMusicStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("Error: database.Music with ID " + musicID + " does not exist.");
                    return;
                }
            }

            // Check for duplicate entry
            checkDuplicateStmt.setInt(1, playlistID);
            checkDuplicateStmt.setInt(2, musicID);
            try (ResultSet rs = checkDuplicateStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Error: database.Music with ID " + musicID + " is already in database.Playlist " + playlistID);
                    return;
                }
            }

            // Insert the music into the playlist
            insertStmt.setInt(1, playlistID);
            insertStmt.setInt(2, musicID);
            insertStmt.setDate(3, java.sql.Date.valueOf(dateAdded));

            int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("database.Music with ID " + musicID + " successfully added to database.Playlist " + playlistID);
            } else {
                System.out.println("Failed to add database.Music to database.Playlist.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMusicFromPlaylist(int playlistId, int musicId) {
        String deleteMusicFromPlaylistQuery = "DELETE FROM PLAYLIST_MUSIC WHERE PlaylistID = ? AND MusicID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteMusicFromPlaylistQuery)) {

            // Set parameters for the DELETE query
            deleteStmt.setInt(1, playlistId);
            deleteStmt.setInt(2, musicId);

            // Execute the DELETE query
            int rowsAffected = deleteStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("database.Music successfully deleted from playlist.");
            } else {
                System.out.println("No matching entry found to delete. Please check the PlaylistID and MusicID.");
            }

        } catch (SQLException e) {
            System.err.println("An error occurred while deleting music from playlist.");
            e.printStackTrace();
        }
    }

}

