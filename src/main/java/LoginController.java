import java.io.IOException;
import java.util.ArrayList;
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

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    /**
     * Receives the valid users loaded at startup.
     */
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    /**
     * Checks both credentials against the loaded users.
     * Displays an error within the login screen if no match exists.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        for (User user : users) {
            if (user.getName().equals(username)
                    && user.getPassword().equals(password)) {
                showWelcome();
                return;
            }
        }

        errorLabel.setText("Invalid username or password.");
        errorLabel.setVisible(true);
    }

    /**
     * Replaces the login screen with the Welcome screen
     * in the same window.
     */
    private void showWelcome() {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/Welcome.fxml")
            );

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GCM Welcome");

        } catch (IOException e) {
            errorLabel.setText("Unable to open Welcome screen.");
            errorLabel.setVisible(true);
            e.printStackTrace();
        }
    }
}