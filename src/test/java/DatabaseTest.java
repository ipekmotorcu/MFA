public class DatabaseTest {

        public static void main(String[] args) {
            Admin admin = new Admin(1, "Admin1");
            Artist artist=new Artist(1,"Artist1",21);
            // Test createMusic(çalışıyor)
           // admin.createMusic("Song1", 1, 3.5, "Pop", false);

            // Test deleteMusic(çalışıyor)
            //admin.deleteMusic(6);

            // Test viewStatistics(çalışıyor)
            //admin.viewStatistics();

            // Test createListener(çalışıyor)
           // admin.createListener("NewListener", 25);

            // Test deleteUser(çalışıyor)
            //admin.deleteUser(8);
            //artist.createMusic("Song1", 1, 3.5, "Pop", false);

            Listener listener = new Listener(1, "JohnDoe", 25);

            // Create a new playlist
            //listener.createPlaylist("My Favorite Songs");

            // Add music to a playlist
            //listener.addMusicToPlaylist(3, 1); // Example PlaylistID = 1, MusicID = 101

            // Remove music from a playlist
            //listener.removeMusicFromPlaylist(3, 1);

            // View all available music
            //listener.viewMusic();

            // Search for music by name
            //listener.searchMusic("Song Two");
            artist.viewStatistics();
        }
    }


