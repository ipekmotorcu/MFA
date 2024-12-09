import java.util.ArrayList;

public class Artist extends User{
    private ArrayList<String> arrAlbum;
    public Artist(int userId, String username, int age) {
        super(userId, username, age);
        this.arrAlbum = new ArrayList<>();
    }

    // Method to create a playlist (stub for now, can be expanded)
    public void createPlaylist() {
        System.out.println("Artist " + getUsername() + " created a new playlist.");
    }

    // Method to add music to an album
    public void addMusic(String albumName, String music) {
        if (arrAlbum.contains(albumName)) {
            System.out.println("Artist " + getUsername() + " added music \"" + music + "\" to album \"" + albumName + "\".");
            // Logic for adding the song to the album
        } else {
            System.out.println("Album \"" + albumName + "\" not found for artist " + getUsername() + ".");
        }
    }

    // Method to remove music from an album
    public void removeMusic(String albumName, String music) {
        if (arrAlbum.contains(albumName)) {
            System.out.println("Artist " + getUsername() + " removed music \"" + music + "\" from album \"" + albumName + "\".");
            // Logic for removing the song from the album
        } else {
            System.out.println("Album \"" + albumName + "\" not found for artist " + getUsername() + ".");
        }
    }

    // Method to create a new album
    public void createMusic(String albumName) {
        if (!arrAlbum.contains(albumName)) {
            arrAlbum.add(albumName);
            System.out.println("Artist " + getUsername() + " created a new album: \"" + albumName + "\".");
        } else {
            System.out.println("Album \"" + albumName + "\" already exists for artist " + getUsername() + ".");
        }
    }

    // Method to delete an album
    public void deleteMusic(String albumName) {
        if (arrAlbum.contains(albumName)) {
            arrAlbum.remove(albumName);
            System.out.println("Artist " + getUsername() + " deleted the album: \"" + albumName + "\".");
        } else {
            System.out.println("Album \"" + albumName + "\" not found for artist " + getUsername() + ".");
        }
    }

    // Method to view artist's album statistics
    public void viewStatistics() {
        System.out.println("Statistics for Artist: " + getUsername());
        System.out.println("Number of albums: " + arrAlbum.size());
        System.out.println("Albums: " + arrAlbum);
    }
}
