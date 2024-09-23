package lms;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LibrarySystem {
    private DatabaseConnector dbConnector;
    private Scanner scanner;

    public LibrarySystem() {
        dbConnector = new DatabaseConnector();
        scanner = new Scanner(System.in);
    }
    
    //main loop to run lms
    public void run() throws SQLException {
        while (true) {
        	//display menu
            System.out.println("\n===============================================");
            System.out.println("              Library Management System         ");
            System.out.println("===============================================");
            System.out.println("1. Add item");
            System.out.println("2. Search for item");
            System.out.println("3. Borrow item");
            System.out.println("4. Return item");
            System.out.println("5. Exit");
            System.out.println("===============================================");
            System.out.print("Enter your choice: ");
            try {
            	//user input get
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume the newline

                switch (choice) {
                    case 1:
                        addItem(); //add new item
                        break;
                    case 2:
                        searchForItem(); //search item
                        break;
                    case 3:
                        borrowItem(); //borrow an item
                        break;
                    case 4:
                        returnItem(); //return an item
                        break;
                    case 5:
                        System.out.println("\n===============================================");
                        System.out.println("              Exiting the Library System         ");
                        System.out.println("===============================================");
                        System.out.println("\n----------------| Done By Ajeeth |---------------");
                        return;
                    default:
                        System.out.println("\n===============================================");
                        System.out.println("              Invalid choice. Please try again. ");
                        System.out.println("===============================================");
                }
            } 
            //input type wrong exception
            catch (InputMismatchException e) {
                System.out.println("\n===============================================");
                System.out.println("              Invalid input. Please enter a number. ");
                System.out.println("===============================================");
                scanner.nextLine(); // clear invalid input
            }
        }
    }
    
    //add item
    private void addItem() throws SQLException {
        System.out.println("\n===============================================");
        System.out.println("              Add Item to Library              ");
        System.out.println("===============================================");
        System.out.print("Enter the title of the item: ");
        String title = scanner.nextLine();

        System.out.print("Enter the type of the item (Book or DVD): ");
        String type = scanner.nextLine();

        Item item;
        
        //book or dvd
            if (type.equalsIgnoreCase("Book")) {
            System.out.print("Enter the author of the book: ");
            String author = scanner.nextLine();

            System.out.print("Enter the genre of the book: ");
            String genre = scanner.nextLine();

            System.out.print("Enter the ISBN of the book: ");
            String isbn = scanner.nextLine();

            item = new Book(0, title, author, genre, isbn);
        } else if (type.equalsIgnoreCase("DVD")) {
            System.out.print("Enter the director of the DVD: ");
            String director = scanner.nextLine();

            System.out.print("Enter the duration of the DVD (in minutes): ");
            int duration = scanner.nextInt();
            scanner.nextLine(); // consume the newline

            item = new DVD(0, title, director, duration);
        } else {
            System.out.println("\n===============================================");
            System.out.println("              Invalid item type. Please try again. ");
            System.out.println("===============================================");
            return;
        }

         //add item to db
        dbConnector.addItem(item);
        System.out.println("\n===============================================");
        System.out.println("              Item added successfully.          ");
        System.out.println("===============================================");
    }

    //searching item
    private void searchForItem() {
        System.out.println("\n===============================================");
        System.out.println("              Search for Item in Library      ");
        System.out.println("===============================================");
        System.out.print("Enter the title or ID of the item to search for: ");
        String query = scanner.nextLine();

        Item item = dbConnector.searchForItem(query);
        if (item != null) {
            System.out.println("\n===============================================");
            System.out.println("              Item found:                     ");
            System.out.println("===============================================");
            System.out.println(item.toString());
        } else {
            System.out.println("\n===============================================");
            System.out.println("              Item not found.                ");
            System.out.println("===============================================");
        }
    }

    //borrowing items
    private void borrowItem() throws SQLException {
        System.out.println("\n===============================================");
        System.out.println("              Borrow Item from Library        ");
        System.out.println("===============================================");
        try {
            System.out.print("Enter the ID of the item to borrow: ");
            int itemId = scanner.nextInt();

            System.out.print("Enter the ID of the user borrowing the item: ");
            int userId = scanner.nextInt();
            scanner.nextLine(); // consume the newline

            dbConnector.borrowItem(itemId, userId);
            System.out.println("\n===============================================");
            System.out.println("              Item borrowed successfully.    ");
            System.out.println("===============================================");
        } catch (InputMismatchException e) {
            System.out .println("\n===============================================");
            System.out.println("              Invalid input. Please enter a number. ");
            System.out.println("===============================================");
            scanner.nextLine(); // clear invalid input
        }
    }

    //returning items
    private void returnItem() throws SQLException {
        System.out.println("\n===============================================");
        System.out.println("              Return Item to Library          ");
        System.out.println("===============================================");
        try {
            System.out.print("Enter the ID of the item to return: ");
            int itemId = scanner.nextInt();

            System.out.print("Enter the ID of the user returning the item: ");
            int userId = scanner.nextInt();
            scanner.nextLine(); // consume the newline

            dbConnector.returnItem(itemId, userId);
            System.out.println("\n===============================================");
            System.out.println("              Item returned successfully.    ");
            System.out.println("===============================================");
        } catch (InputMismatchException e) {
            System.out.println("\n===============================================");
            System.out.println("              Invalid input. Please enter a number. ");
            System.out.println("===============================================");
            scanner.nextLine(); // clear invalid input
        }
    }

    //run the code
    public static void main(String[] args) throws SQLException {
        LibrarySystem librarySystem = new LibrarySystem();
        DatabaseConnector dbConnector = new DatabaseConnector();
        librarySystem.run();
        dbConnector.closeConnection();
        
    }
}