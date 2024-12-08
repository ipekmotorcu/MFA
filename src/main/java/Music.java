public class Music {
    private String musicName;
    private int musicID;
    private Album album;
    private double duration;
    private Artist artistName;
    private Category category;
    private boolean explicitLabel;

    public Music(String musicName, int musicID, Album album, double duration, Artist artistName, Category category, boolean explicitLabel) {
        this.musicName = musicName;
        this.musicID = musicID;
        this.album = album;
        this.duration = duration;
        this.artistName = artistName;
        this.category = category;
        this.explicitLabel = explicitLabel;
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

    public double getDuration() {
        return duration;
    }

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

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setArtistName(Artist artistName) {
        this.artistName = artistName;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setExplicitLabel(boolean explicitLabel) {
        this.explicitLabel = explicitLabel;
    }
}
