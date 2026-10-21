import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class UsersApp {

    public static void main(String[] args)
            throws FileNotFoundException {

        // Create a list for valid users
        ArrayList<User> users = new ArrayList<>();

        // Open the input file
        File file = new File("users.txt");

        try (Scanner reader = new Scanner(file)) {

            while (reader.hasNextLine()) {

                String line = reader.nextLine();

                // Separate username and password
                String[] parts = line.trim().split("\\s+");

                // Check line format
                if (parts.length != 2) {
                    System.err.println("Invalid line: " + line);
                    continue;
                }

                String username = parts[0];
                String password = parts[1];

                // Create and validate user
                try {
                    User user = new User(username, password);
                    users.add(user);

                } catch (IllegalArgumentException e) {
                    System.err.println(
                        line + " - " + e.getMessage()
                    );
                }
            }
        }

        // Sort valid users by username
        Collections.sort(users,
            (u1, u2) -> u1.getName().compareTo(u2.getName())
        );

        // Print valid users
        for (User user : users) {
            System.out.println(
                user.getName() + " " + user.getPassword()
            );
        }
    }
}