import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GcmApp extends Application {

    private static int maxAttempts;
    private static int blockSeconds;

    public static int getMaxAttempts() {
        return maxAttempts;
    }

    public static int getBlockSeconds() {
        return blockSeconds;
    }

    @Override
    public void start(Stage stage) throws Exception {
        ArrayList<User> users = UsersApp.loadUsers("Users.txt");

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/Login.fxml")
        );

        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setUsers(users);

        stage.setTitle("GCM Login");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        maxAttempts = readPositiveInt(
            scanner, "Maximum incorrect login attempts: "
        );

        blockSeconds = readPositiveInt(
            scanner, "Block duration in seconds: "
        );

        launch(args);
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);

            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();

                if (value > 0) {
                    return value;
                }
            } else {
                scanner.next();
            }

            System.out.println("Please enter a positive whole number.");
        }
    }
}