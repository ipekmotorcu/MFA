import java.util.ArrayList;

public class Playlist {
    private int playListID;
    private String playListName;

    private ArrayList<Music> arrMusic;



    public Playlist(int playListID, String playListName) {
        this.playListID = playListID;
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
}
