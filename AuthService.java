import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private static final String ADMIN_USERNAME = "booksuwu";
    private static final String ADMIN_PASSWORD = "uwu2026";
    private static final Path USER_STORAGE_FILE = Paths.get("user_accounts.txt");

    private final List<UserAccount> users = new ArrayList<>();

    public AuthService() {
        loadUsersFromFile();
    }

    public boolean registerUser(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }

        String normalizedUsername = username.trim();
        if (isAdmin(normalizedUsername) || findUser(normalizedUsername) != null) {
            return false;
        }

        users.add(new UserAccount(normalizedUsername, password));
        saveUsersToFile();
        return true;
    }

    public String login(String username, String password) {
        String normalizedUsername = username == null ? "" : username.trim();

        if (isAdmin(normalizedUsername) && ADMIN_PASSWORD.equals(password)) {
            return "ADMIN";
        }

        UserAccount user = findUser(normalizedUsername);
        if (user != null && user.getPassword().equals(password)) {
            return "USER";
        }

        return "NONE";
    }

    private boolean isAdmin(String username) {
        return ADMIN_USERNAME.equals(username);
    }

    private UserAccount findUser(String username) {
        for (UserAccount user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private void loadUsersFromFile() {
        if (!Files.exists(USER_STORAGE_FILE)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(USER_STORAGE_FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|", 2);
                if (parts.length != 2) {
                    continue;
                }

                String username = parts[0].trim();
                String password = parts[1];
                if (!username.isEmpty() && findUser(username) == null && !isAdmin(username)) {
                    users.add(new UserAccount(username, password));
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not load saved user accounts.");
        }
    }

    private void saveUsersToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(USER_STORAGE_FILE)) {
            for (UserAccount user : users) {
                writer.write(user.getUsername() + "|" + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not save user accounts.");
        }
    }
}
