import java.util.ArrayList;

public class Listener extends User{

    private ArrayList<Playlist> playlists;
    public Listener(int userId, String username, int age) {
        super(userId, username, age);
        this.playlists = new ArrayList<>();
    }

    public void createPlaylist(String playlistName) {
        // Implementation to create a new playlist
    }

    public void addMusicToPlaylist(int playlistId, int musicId) {
        // Implementation to add music to a playlist
    }

    public void removeMusicFromPlaylist(int playlistId, int musicId) {
        // Implementation to remove music from a playlist
    }
    public void viewMusic(){
        //bütün müzikleri görmesi için

    }
    public void searchMusic(){
        //müzik araması için

    }
}
