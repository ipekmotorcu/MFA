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

}
