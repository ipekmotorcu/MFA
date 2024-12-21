package GUI;

import org.junit.jupiter.api.*;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListenerViewTest {


    @Test
    void testSearchFunctionality() throws InterruptedException {
        // Initialize ListenerView
        ListenerView listenerView = new ListenerView(1);

        // Get the search bar and search button
        JTextField searchBar = listenerView.searchBar;
        JButton searchButton = findButton(listenerView.getContentPane(), "Search");
        assertNotNull(searchBar, "Search bar should exist.");
        assertNotNull(searchButton, "Search button should exist.");

        // Simulate a search query
        String query = "Song One";
        searchBar.setText(query);

        // Measure time for search result to appear
        long startTime = System.currentTimeMillis();
        searchButton.doClick();

        // Wait for potential delay and assume results should appear
        Thread.sleep(5000);

        // Check the search result
        boolean resultsDisplayed = checkSearchResults(listenerView, query);
        long endTime = System.currentTimeMillis();

        // Assert results displayed within 5 seconds
        assertTrue(resultsDisplayed, "Search results should be displayed correctly.");
        assertTrue((endTime - startTime) <= 5101, "Search should complete within 5 seconds.");
    }

    private boolean checkSearchResults(ListenerView listenerView, String query) {
        // Simulate verifying search results in the ListenerView UI (use listener logic if connected to a database)
        DefaultListModel<String> likedSongsModel = (DefaultListModel<String>) listenerView.likedSongsList.getModel();
        return likedSongsModel.contains(query);
    }

    private JButton findButton(Container container, String buttonName) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton && ((JButton) component).getText().equals(buttonName)) {
                return (JButton) component;
            }
            if (component instanceof Container) {
                JButton button = findButton((Container) component, buttonName);
                if (button != null) {
                    return button;
                }
            }
        }
        return null;
    }
}
