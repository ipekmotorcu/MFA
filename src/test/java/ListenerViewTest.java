import GUI.ListenerView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListenerViewTest {

    private ListenerView listenerView;

    //SONG SEARCH TEST
    @BeforeEach
    void setUp() {
        // Initialize the ListenerView with a test user ID
        listenerView = new ListenerView(2);

        // Mock some pre-set data for testing
        listenerView.songNameLabel.setText("");


        listenerView.searchBar.setText("");
    }

    @Test
    void testSearchSong_ValidInput() {
        // Arrange
        listenerView.searchBar.setText("Song One");

        // Act
        listenerView.searchSong();

        // Assert
        assertEquals("Song Name: Song One", listenerView.songNameLabel.getText());


    }

    @Test
    void testSearchSong_InvalidInput() {
        // Arrange
        listenerView.searchBar.setText("Nonexistent Song");

        // Act
        listenerView.searchSong();

        // Assert
        assertEquals("", listenerView.songNameLabel.getText()); // No song name

    }

    @Test
    void testSearchSong_EmptyInput() {
        // Arrange
        listenerView.searchBar.setText("");

        // Act
        listenerView.searchSong();

        // Assert

        assertEquals("", listenerView.songNameLabel.getText()); // No song name

    }
}
