import java.util.ArrayList;

public class User {
    private int userId;
    private String username;
    private int age;

    private ArrayList<Playlist> playlists;
    public User(int userId, String username, int age) {
        this.userId = userId;
        this.username = username;
        this.age = age;
    }
    public void createPlaylist(String playlistName) {
        // Implementation to create a new playlist
        Playlist list=new Playlist(playlistName);//Playlist ID database kısmında otomatik oluşturulabiliyor
        // gerekirse oradan çekeriz burada oluşturmaya gerek yok gibi
        playlists.add(list);
        //!!!Database'e kaydetmeliyiz
    }

    public void addMusicToPlaylist(int playlistId, int musicId) {
        // Implementation to add music to a playlist
    }

    public void removeMusicFromPlaylist(int playlistId, int musicId) {
        // Implementation to remove music from a playlist
    }
}
