import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Music {
    private String musicName;
    private int musicID;
    private Album album;
    //private double duration;
    private Artist artistName;
    private Category category;
    private boolean explicitLabel;
    private int playCount;
    private LocalDate releaseDate;

    public Music(String musicName, int musicID, Album album, Artist artistName, Category category, boolean explicitLabel) {
        this.musicName = musicName;
        this.musicID = musicID;
        this.album = album;
        //this.duration = duration;
        this.artistName = artistName;
        this.category = category;
        this.explicitLabel = explicitLabel;
    }

    public Music(String musicName, int musicID, Album album, Artist artistName, Category category, boolean explicitLabel, int playCount, LocalDate releaseDate) {
        this.musicName = musicName;
        this.musicID = musicID;
        this.album = album;
        this.artistName = artistName;
        this.category = category;
        this.explicitLabel = explicitLabel;
        //this.duration = duration;
        this.playCount = playCount;
        this.releaseDate = releaseDate;
    }

    public String getMusicName() {
        return musicName;
    }

    public int getMusicID() {
        return musicID;
    }

    public Album getAlbum() {
        return album;
    }

/*    public double getDuration() {
        return duration;
    }*/

    public Artist getArtistName() {
        return artistName;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isExplicitLabel() {
        return explicitLabel;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public void setMusicID(int musicID) {
        this.musicID = musicID;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    /*public void setDuration(double duration) {
        this.duration = duration;
    }*/

    public void setArtistName(Artist artistName) {
        this.artistName = artistName;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setExplicitLabel(boolean explicitLabel) {
        this.explicitLabel = explicitLabel;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void reactToMusic(int userId, int musicId, String reactionType) {
        String insertReactionQuery = "INSERT INTO REACTIONS (UserID, MusicID, ReactionType) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertReactionQuery)) {

            // Set the parameters for the query
            insertStmt.setInt(1, userId);
            insertStmt.setInt(2, musicId);
            insertStmt.setString(3, reactionType);

            // Execute the INSERT query
            int rowsAffected = insertStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Reaction successfully added!");
            } else {
                System.out.println("Failed to add the reaction. Please check your inputs.");
            }

        } catch (SQLException e) {
            System.err.println("An error occurred while adding a reaction.");
            e.printStackTrace();
        }
    }
}
