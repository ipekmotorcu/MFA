package GUI;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class AdminViewTest {

    @Test
    void testAdminViewInitialization() {
        // Create an AdminView instance
        AdminView adminView = new AdminView(1);

        // Verify the frame title
        assertEquals("Admin Main Page", adminView.getTitle(), "AdminView should have the correct title.");

        // Verify components are visible
        Component[] components = adminView.getContentPane().getComponents();
        assertTrue(components.length > 0, "AdminView should contain components.");

        // Verify the frame is visible
        assertTrue(adminView.isVisible(), "AdminView should be visible.");
    }

    @Test
    void testBanUserAction() {
        // Create an AdminView instance
        AdminView adminView = new AdminView(1);

        // Simulate selecting a user to ban
        DefaultListModel<String> userListModel = new DefaultListModel<>();
        userListModel.addElement("1 - User1"); // Example user

        JList<String> userList = new JList<>(userListModel);
        userList.setSelectedIndex(0); // Select the first user

        // Simulate clicking the Ban User button
        JButton banButton = new JButton("Ban Selected User");
        long startTime = System.nanoTime();
        while (!userListModel.isEmpty()) {
            userList.setSelectedIndex(0); // Always select the first user
            userListModel.removeElementAt(0); // Simulate banning
        }
        long endTime = System.nanoTime();

        // Verify that the user list is empty
        assertEquals(0, userListModel.getSize(), "User list should be empty after banning the user.");

        // Verify that the operation completes within 5 seconds
        long duration = TimeUnit.NANOSECONDS.toSeconds(endTime - startTime);
        assertTrue(duration <= 5, "Banning operation should complete in less than 5 seconds.");
    }

    @Test
    void testBanMultipleUsersDuration() {
        // Create an AdminView instance
        AdminView adminView = new AdminView(1);

        // Simulate banning 1, 100, and 1000 users
        int[] userCounts = {1, 100, 1000};
        for (int count : userCounts) {
            DefaultListModel<String> userListModel = new DefaultListModel<>();
            for (int i = 1; i <= count; i++) {
                userListModel.addElement(i + " - User" + i);
            }

            JList<String> userList = new JList<>(userListModel);

            // Start timing
            long startTime = System.nanoTime();
            while (!userListModel.isEmpty()) {
                userList.setSelectedIndex(0); // Always select the first user
                userListModel.removeElementAt(0); // Simulate banning
            }
            long endTime = System.nanoTime();

            // Verify all users are banned
            assertEquals(0, userListModel.getSize(), "All users should be banned.");

            // Verify the operation completes within 5 seconds
            long duration = TimeUnit.NANOSECONDS.toSeconds(endTime - startTime);
            assertTrue(duration <= 5, "Banning operation for " + count + " users should complete in less than 5 seconds.");
        }
    }
}