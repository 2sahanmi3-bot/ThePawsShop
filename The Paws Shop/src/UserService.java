import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserService {
    // Utility method for consistent file reading
    private static String[] parseLine(String line) {
        return line.split("\\|"); // Split by "|" instead of ","
    }

    // Authenticate method
    public static User authenticate(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = parseLine(line);
                if (details.length == 3) {
                    String fileUsername = details[0].trim();
                    String filePassword = details[1].trim();
                    String fileRole = details[2].trim();

                    // Compare passwords directly (no hashing here for simplicity)
                    if (fileUsername.equals(username) && filePassword.equals(password)) {

                        // Return appropriate subclass of User based on role
                        if (fileRole.equalsIgnoreCase("Manager")) {
                            return new Manager(fileUsername, filePassword);
                        } else if (fileRole.equalsIgnoreCase("Cashier")) {
                            return new Cashier(fileUsername, filePassword);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users file: " + e.getMessage());
        }
        return null;
    }
}
