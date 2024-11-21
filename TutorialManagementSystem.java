import java.sql.*;
import java.util.Scanner;

public class TutorialManagementSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/tutorial_management";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "root123"; // Replace with your MySQL password

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to the database successfully!");
            Scanner scanner = new Scanner(System.in);
            String choice;

            do {
                System.out.println("\nChoose an option:");
                System.out.println("1. Add Category");
                System.out.println("2. Add Tutorial");
                System.out.println("3. Add User");
                System.out.println("4. View Categories");
                System.out.println("5. View Tutorials");
                System.out.println("6. View Users");
                System.out.println("7. Exit");

                choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        System.out.print("Enter category name: ");
                        String categoryName = scanner.nextLine();
                        addCategory(connection, categoryName);
                        break;
                    case "2":
                        System.out.print("Enter tutorial title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter tutorial description: ");
                        String description = scanner.nextLine();
                        System.out.print("Enter category ID: ");
                        int categoryId = Integer.parseInt(scanner.nextLine());
                        addTutorial(connection, title, description, categoryId);
                        break;
                    case "3":
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        addUser(connection, username, password);
                        break;
                    case "4":
                        viewCategories(connection);
                        break;
                    case "5":
                        viewTutorials(connection);
                        break;
                    case "6":
                        viewUsers(connection);
                        break;
                    case "7":
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } while (!choice.equals("7"));

        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // Method to add a category
    private static void addCategory(Connection connection, String name) throws SQLException {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.executeUpdate();
            System.out.println("Category added successfully!");
        }
    }

    // Method to view categories
    private static void viewCategories(Connection connection) throws SQLException {
        String sql = "SELECT * FROM categories";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.println("ID: " + id + ", Name: " + name);
            }
        }
    }

    // Method to add a tutorial
    private static void addTutorial(Connection connection, String title, String description, int categoryId) throws SQLException {
        String sql = "INSERT INTO tutorials (title, description, category_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setInt(3, categoryId);
            statement.executeUpdate();
            System.out.println("Tutorial added successfully!");
        }
    }

    // Method to view tutorials
    private static void viewTutorials(Connection connection) throws SQLException {
        String sql = "SELECT t.id, t.title, t.description, c.name AS category_name FROM tutorials t JOIN categories c ON t.category_id = c.id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String categoryName = resultSet.getString("category_name");
                System.out.println("ID: " + id + ", Title: " + title + ", Description: " + description + ", Category: " + categoryName);
            }
        }
    }

    // Method to add a user
    private static void addUser(Connection connection, String username, String password) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password); // Note: In a real application use hashed passwords.
            statement.executeUpdate();
            System.out.println("User added successfully!");
        }
    }

    // Method to view users
    private static void viewUsers(Connection connection) throws SQLException {
        String sql = "SELECT * FROM users";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                System.out.println("ID: " + id + ", Username: " + username);
            }
        }
    }
}