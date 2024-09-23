package lms;

import java.sql.*;

public class DatabaseConnector {
    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/library2";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private Connection connection;

    // Constructor - this sets up the connection to the database
    public DatabaseConnector() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            // If there's a problem connecting, we'll print out the error message
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    // Add a new item (either a book or DVD) to the database
    public void addItem(Item item) {
        String query = "INSERT INTO Item (title, type, available) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Set the item properties (title, type, and availability)
            statement.setString(1, item.getTitle());
            statement.setString(2, item.getType());
            statement.setBoolean(3, item.isAvailable());

            // Execute the SQL insert and get the auto-generated item ID
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int itemId = generatedKeys.getInt(1);

                // Check if the item is a book or a DVD and add the right details
                if (item instanceof Book) {
                    Book book = (Book) item;
                    addBookDetails(itemId, book);
                } else if (item instanceof DVD) {
                    DVD dvd = (DVD) item;
                    addDVDDetails(itemId, dvd);
                }
            }
        } catch (SQLException e) {
            // In case something goes wrong during insertion, print the error
            System.err.println("Error adding item: " + e.getMessage());
        }
    }

    // Add details specific to books (e.g., author, genre, ISBN)
    private void addBookDetails(int itemId, Book book) throws SQLException {
        String query = "INSERT INTO Book (itemId, author, genre, isbn) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getGenre());
            statement.setString(4, book.getIsbn());
            statement.executeUpdate();
        }
    }

    // Add details specific to DVDs (e.g., director, duration)
    private void addDVDDetails(int itemId, DVD dvd) throws SQLException {
        String query = "INSERT INTO DVD (itemId, director, duration) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);
            statement.setString(2, dvd.getDirector());
            statement.setInt(3, dvd.getDuration());
            statement.executeUpdate();
        }
    }

    // Search for an item by title or item ID
    public Item searchForItem(String titleOrId) {
        Item item = null;
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT i.itemId, i.title, i.type, b.author, b.genre, b.isbn, d.director, d.duration " +
                        "FROM Item i " +
                        "LEFT JOIN Book b ON i.itemId = b.itemId " +
                        "LEFT JOIN DVD d ON i.itemId = d.itemId " +
                        "WHERE i.title = ? OR i.itemId = ?")) {
            // Setting the title or ID for search
            statement.setString(1, titleOrId);
            statement.setString(2, titleOrId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int itemId = resultSet.getInt("itemId");
                String title = resultSet.getString("title");
                String type = resultSet.getString("type");

                // If it's a book, retrieve the book details
                if (type.equalsIgnoreCase("Book")) {
                    String author = resultSet.getString("author");
                    String genre = resultSet.getString("genre");
                    String isbn = resultSet.getString("isbn");
                    item = new Book(itemId, title, author, genre, isbn);
                // If it's a DVD, retrieve the DVD details
                } else if (type.equalsIgnoreCase("DVD")) {
                    String director = resultSet.getString("director");
                    int duration = resultSet.getInt("duration");
                    item = new DVD(itemId, title, director, duration);
                }
            }
        } catch (SQLException e) {
            // If something goes wrong during the search, print the error
            System.err.println("Error searching for item: " + e.getMessage());
        }
        return item; // Return the found item (null if nothing is found)
    }

    // Borrow an item from the library
    public void borrowItem(int itemId, int userId) throws SQLException {
        // First, check if the user exists
        if (!userExists(userId)) {
            System.out.println("User does not exist. Please register.");
            throw new SQLException("User ID does not exist");
        }

        // Then, check if the item is available
        if (!isItemAvailable(itemId)) {
            throw new SQLException("Item is not available.");
        }

        // Update the item's availability to false (borrowed)
        updateItemAvailability(itemId, false);

        // Add the borrow record to the borrowing history
        insertBorrowingRecord(itemId, userId);
    }

    // Check if an item is available
    public boolean isItemAvailable(int itemId) throws SQLException {
        String query = "SELECT available FROM Item WHERE itemId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("available"); // Return availability status
                } else {
                    throw new SQLException("Item not found.");
                }
            }
        }
    }

    // Update item availability (true = available, false = borrowed)
    private void updateItemAvailability(int itemId, boolean isAvailable) throws SQLException {
        String query = "UPDATE Item SET available = ? WHERE itemId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, isAvailable);
            statement.setInt(2, itemId);
            statement.executeUpdate();
        }
    }

    // Insert a record into the borrowing history when an item is borrowed
    private void insertBorrowingRecord(int itemId, int userId) throws SQLException {
        String query = "INSERT INTO BorrowingHistory (itemId, userId, borrowDate) VALUES (?, ?, NOW())";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    // Check if the user exists in the system
    private boolean userExists(int userId) throws SQLException {
        String query = "SELECT * FROM User WHERE userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Return true if user is found
            }
        }
    }

    // Return an item to the library
    public void returnItem(int itemId, int userId) throws SQLException {
        // Check if the user exists
        if (!userExists(userId)) {
            System.out.println("User does not exist. Please register.");
            throw new SQLException("User ID does not exist");
        }

        // If the item is not available (meaning it's borrowed), proceed with return
        if (!isItemAvailable(itemId)) {
            // Make sure the item was borrowed by this user
            String query = "SELECT * FROM BorrowingHistory WHERE itemId = ? AND userId = ? AND returnDate IS NULL";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, itemId);
                statement.setInt(2, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        // Update borrowing history and mark item as returned
                        updateBorrowingRecord(itemId, userId);
                        updateItemAvailability(itemId, true);
                        System.out.println("Item successfully returned.");
                    } else {
                        System.out.println("No active borrow record found for this item.");
                    }
                }
            }
        } else {
            System.out.println("Item is not borrowed.");
        }
    }

    // Update the borrowing history record when an item is returned
    private void updateBorrowingRecord(int itemId, int userId) throws SQLException {
        String query = "UPDATE BorrowingHistory SET returnDate = NOW() WHERE itemId = ? AND userId = ? AND returnDate IS NULL";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);
            statement.setInt(2, userId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No borrowing record found or item already returned.");
            }
        }
    }
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("\nDatabase connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("\nError closing the database connection: " + e.getMessage());
        }
    }

}
