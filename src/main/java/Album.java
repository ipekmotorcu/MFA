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
}
