package GUI;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginViewTest {

    @Test
    void testSuccessfulLogin() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            // Create LoginView instance
            LoginView loginView = new LoginView();

            // Simulate user input
            loginView.userText.setText("listener1");
            loginView.passwordText.setText("pass123");

            // Trigger login validation (simulating button press)
            loginView.loginButton.doClick();

            // Assuming a successful login opens another window
            // Check that the LoginView has been disposed
            assertTrue(!loginView.isVisible(), "LoginView should be closed after successful login.");
        });
    }

    @Test
    void testLoginDuration() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            LoginView loginView = new LoginView();

            // Simulate user input (login for 3 users)
            loginView.userText.setText("listener1");
            loginView.passwordText.setText("pass123");

            loginView.userText.setText("artist1");
            loginView.passwordText.setText("pass789");

            loginView.userText.setText("admin1");
            loginView.passwordText.setText("password123");


            long startTime = System.currentTimeMillis();
            loginView.loginButton.doClick();
            long endTime = System.currentTimeMillis();

            // Calculate duration
            long duration = endTime - startTime;

            // Assert that duration is <= 2000ms (2 seconds)
            assertTrue(duration <= 2000, "Login duration should be less than or equal to 2 seconds.");
        });
    }


}
