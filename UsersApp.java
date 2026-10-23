import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class UsersApp {

    public static void main(String[] args)
            throws FileNotFoundException {

        
        ArrayList<User> users = new ArrayList<>();

       
        File file = new File("users.txt");

        try (Scanner reader = new Scanner(file)) {

            while (reader.hasNextLine()) {

                String line = reader.nextLine();

                String[] parts = line.trim().split("\\s+");

              
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

        
        Collections.sort(users,
            (u1, u2) -> u1.getName().compareTo(u2.getName())
        );

       
       try (PrintWriter writer = new PrintWriter("out.txt")) {
            for (User user : users) {
                writer.println(user.getName() + " " + user.getPassword());
                writer.println(user.getName() + " " + user.getPassword());
            }
        }
    }
}