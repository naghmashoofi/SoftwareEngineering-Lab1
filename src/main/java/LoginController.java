import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    private ArrayList<User> users = new ArrayList<>();
    private boolean loginInProgress = false;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    @FXML
    private void handleLogin() {
        if (loginInProgress) {
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();

        for (User user : users) {
            if (user.getName().equals(username)) {
                loginInProgress = true;
                errorLabel.setVisible(false);

                if (user.getPassword().equals(password)) {
                    checkAccess(user);
                } else {
                    recordFailure(user);
                }

                return;
            }
        }

        showError("Invalid username or password.");
    }

    private void checkAccess(User user) {
        Thread thread = new Thread(() -> {
            boolean blocked = user.isBlocked(GcmApp.getBlockSeconds());

            Platform.runLater(() -> {
                try {
                    if (blocked) {
                        showBlockedMessage();
                    } else {
                        showWelcome();
                    }
                } finally {
                    loginInProgress = false;
                }
            });
        }, "LoginBlockCheck");

        thread.setDaemon(true);
        thread.start();
    }

    private void recordFailure(User user) {
        Thread thread = new Thread(() -> {
            boolean blocked = user.registerFailedAttempt(
                GcmApp.getMaxAttempts(),
                GcmApp.getBlockSeconds()
            );

            Platform.runLater(() -> {
                try {
                    if (blocked) {
                        showBlockedMessage();
                    } else {
                        showError("Invalid username or password.");
                    }
                } finally {
                    loginInProgress = false;
                }
            });
        }, "FailedAttemptUpdate");

        thread.setDaemon(true);
        thread.start();
    }

    private void showBlockedMessage() {
        showError(
            "This account is blocked for "
            + GcmApp.getBlockSeconds()
            + " seconds from the last allowed failed attempt."
        );
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showWelcome() {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/Welcome.fxml")
            );

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GCM Welcome");

        } catch (IOException e) {
            showError("Unable to open Welcome screen.");
            e.printStackTrace();
        }
    }
}