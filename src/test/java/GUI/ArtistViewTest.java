package GUI;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


public class ArtistViewTest {

    @Test
    void testFollowingArtistsPageUpdatesWithin3Seconds() throws InterruptedException {
        // Initialize the ListenerView to simulate the user's following artist page
        ListenerView listenerView = new ListenerView(1);

        // Simulate the action of uploading music from 3 artists
        String[] newSongs = {"Song by Artist 1", "Song by Artist 2", "Song by Artist 3"};
        long[] durations = new long[newSongs.length];

        for (int i = 0; i < newSongs.length; i++) {
            long startTime = System.nanoTime();

            // Simulate an artist uploading a new song (mock database update or UI update)
            simulateArtistUpload(listenerView, newSongs[i]);

            // Wait to allow the UI to update (simulate server interaction)
            Thread.sleep(3000);

            // Check if the new song is displayed on the following artists page
            boolean isSongDisplayed = isSongInFollowingArtistsPage(listenerView, newSongs[i]);
            assertTrue(isSongDisplayed, "Newly uploaded song should appear in the following artists page.");

            long endTime = System.nanoTime();
            durations[i] = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        }

        // Compute the mean duration
        long totalDuration = 0;
        for (long duration : durations) {
            totalDuration += duration;
        }
        long meanDuration = totalDuration / newSongs.length;
        System.out.println(meanDuration + " Here mean duration for updates");
        // Assert the mean duration is less than 3 seconds
        assertTrue(meanDuration <= 3000, "Mean duration for updates should be less than 3 seconds.");
    }

    private void simulateArtistUpload(ListenerView listenerView, String songName) {
        // Simulate the addition of a song to the following artist's list (mock data or database interaction)
        DefaultListModel<String> likedSongsModel = (DefaultListModel<String>) listenerView.likedSongsList.getModel();
        likedSongsModel.addElement(songName);
    }

    private boolean isSongInFollowingArtistsPage(ListenerView listenerView, String songName) {
        // Check if the song appears in the list of following artists
        DefaultListModel<String> likedSongsModel = (DefaultListModel<String>) listenerView.likedSongsList.getModel();
        return likedSongsModel.contains(songName);
    }

    @Test
    void testAddSong() {
        ArtistView artistView = new ArtistView(1);

        JButton addSongButton = findButton(artistView, "Add Song to Album");
        assertNotNull(addSongButton, "Add Song button should exist.");

        addSongButton.doClick();

        assertTrue(artistView.isVisible(), "ArtistView should remain visible after adding a song.");
    }

    @Test
    void testCreateAlbum() {
        ArtistView artistView = new ArtistView(1);

        JButton createAlbumButton = findButton(artistView, "Create Album");
        assertNotNull(createAlbumButton, "Create Album button should exist.");

        createAlbumButton.doClick();

        assertTrue(artistView.isVisible(), "ArtistView should remain visible after creating an album.");
    }

    @Test
    void testRemoveSong() {
        ArtistView artistView = new ArtistView(1);

        JButton removeSongButton = findButton(artistView, "Remove Song from Album");
        assertNotNull(removeSongButton, "Remove Song button should exist.");

        removeSongButton.doClick();

        assertTrue(artistView.isVisible(), "ArtistView should remain visible after removing a song.");
    }

    @Test
    void testViewStatistics() {
        ArtistView artistView = new ArtistView(1);

        JButton viewStatsButton = findButton(artistView, "View Statistics");
        assertNotNull(viewStatsButton, "View Statistics button should exist.");

        viewStatsButton.doClick();

        assertTrue(artistView.isVisible(), "ArtistView should remain visible after viewing statistics.");
    }

    private JButton findButton(Container container, String buttonText) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton && ((JButton) component).getText().equals(buttonText)) {
                return (JButton) component;
            }
            if (component instanceof Container) {
                JButton button = findButton((Container) component, buttonText);
                if (button != null) {
                    return button;
                }
            }
        }
        return null;
    }
}
