import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GcmApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Load valid users before opening the login screen.
        ArrayList<User> users = UsersApp.loadUsers("Users.txt");

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/Login.fxml")
        );
        Parent root = loader.load();

        // Pass the users to the controller created by the FXML loader.
        LoginController controller = loader.getController();
        controller.setUsers(users);

        stage.setTitle("GCM Login");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}