import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class UsersApp {

    /**
     * Loads users from a file, skipping malformed lines and users
     * whose credentials fail the validation in User.
     */
    public static ArrayList<User> loadUsers(String filename)
            throws FileNotFoundException {

        ArrayList<User> users = new ArrayList<>();

        try (Scanner reader = new Scanner(new File(filename))) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.trim().split("\\s+");

                if (parts.length != 2) {
                    System.err.println("Invalid line: " + line);
                    continue;
                }

                try {
                    users.add(new User(parts[0], parts[1]));
                } catch (IllegalArgumentException e) {
                    System.err.println(line + " - " + e.getMessage());
                }
            }
        }

        return users;
    }

    /**
     * Preserves the Lab 1 behavior: sorts valid users and writes them
     * to out.txt.
     */
    public static void main(String[] args)
            throws FileNotFoundException {

        ArrayList<User> users = loadUsers("users.txt");

        Collections.sort(users,
            (u1, u2) -> u1.getName().compareTo(u2.getName())
        );

        try (PrintWriter writer = new PrintWriter("out.txt")) {
            for (User user : users) {
                writer.println(
                    user.getName() + " " + user.getPassword()
                );
            }
        }
    }
}