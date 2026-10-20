import java.util.Scanner;

/**
 * Menu.java
 * Handles all console input/output — displays the menu and routes each
 * choice to the matching Library method. Keeps I/O separate from business
 * logic (Library.java), which is good practice for a college project.
 */
public class Menu {
    private final Library library;
    private final Scanner scanner;

    public Menu() {
        library = new Library();
        scanner = new Scanner(System.in);
    }

    public void start() {
        int choice;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");
            handleChoice(choice);
        } while (choice != 7);

        System.out.println("👋 Thank you for using the Library Management System!");
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n===== 📚 LIBRARY MANAGEMENT SYSTEM =====");
        System.out.println("1. 📚 Add a new book");
        System.out.println("2. 🔍 Search a book");
        System.out.println("3. 📋 View all books");
        System.out.println("4. 📖 Issue a book");
        System.out.println("5. ↩️  Return a book");
        System.out.println("6. 🗑️  Remove a book");
        System.out.println("7. 🚪 Exit");
        System.out.println("=========================================");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1 -> addBookMenu();
            case 2 -> searchBookMenu();
            case 3 -> library.viewAllBooks();
            case 4 -> issueBookMenu();
            case 5 -> returnBookMenu();
            case 6 -> removeBookMenu();
            case 7 -> { /* exit handled by loop condition */ }
            default -> System.out.println("❌ Invalid choice. Please try again.");
        }
    }

    private void addBookMenu() {
        int id = readInt("Enter Book ID: ");
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();

        Book book = new Book(id, title, author, genre);
        library.addBook(book);
    }

    private void searchBookMenu() {
        System.out.print("Enter Book ID / Title / Author to search: ");
        String keyword = scanner.nextLine();
        library.searchBook(keyword);
    }

    private void issueBookMenu() {
        int id = readInt("Enter Book ID to issue: ");
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        try {
            library.issueBook(id, name);
        } catch (BookNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void returnBookMenu() {
        int id = readInt("Enter Book ID to return: ");
        try {
            library.returnBook(id);
        } catch (BookNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void removeBookMenu() {
        int id = readInt("Enter Book ID to remove: ");
        try {
            library.removeBook(id);
        } catch (BookNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    // Robust integer input — re-prompts instead of crashing on bad input
    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("⚠️ Please enter a valid number.");
            scanner.next(); // discard invalid token
            System.out.print(prompt);
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume the trailing newline
        return value;
    }
}
