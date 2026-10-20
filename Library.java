
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Library.java
 * Core engine of the Library Management System.
 * MySQL is now the primary data store.
 */
public class Library {

    private final ArrayList<Book> books = new ArrayList<>();
    private final BookDAO bookDAO = new BookDAO();

    // ==============================
    // CONSTRUCTOR
    // ==============================

    public Library() {
        loadFromDatabase();
    }

    // ==============================
    // LOAD BOOKS FROM MYSQL
    // ==============================

    private boolean loadFromDatabase() {
        try {
            ArrayList<Book> databaseBooks =
                    new ArrayList<>(bookDAO.getAllBooks());

            books.clear();
            books.addAll(databaseBooks);

            return true;

        } catch (SQLException e) {
            System.err.println("Error loading books from MySQL.");
            e.printStackTrace();
            return false;
        }
    }

    // ==============================
    // ADD BOOK
    // ==============================

    public void addBook(Book book) {

        try {
            boolean added = bookDAO.addBook(book);

            if (added) {
                books.add(book);

                System.out.println(
                    "Book added to MySQL: " + book.getTitle()
                );
            } else {
                System.out.println("Book was not added.");
            }

        } catch (SQLException e) {
            System.err.println(
                "Could not add book. Check the ID and database."
            );
            e.printStackTrace();
        }
    }

    // ==============================
    // SEARCH BOOK
    // ==============================

    public void searchBook(String keyword) {

        if (!loadFromDatabase()) {
            System.out.println("Could not load books from MySQL.");
            return;
        }

        boolean found = false;
        String search = keyword.toLowerCase();

        for (Book b : books) {
            if (String.valueOf(b.getBookId()).equals(keyword)
                    || b.getTitle().toLowerCase().contains(search)
                    || b.getAuthor().toLowerCase().contains(search)) {

                System.out.println(b);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching books found.");
        }
    }

    // ==============================
    // VIEW ALL BOOKS
    // ==============================

    public void viewAllBooks() {

        if (!loadFromDatabase()) {
            System.out.println("Could not load books from MySQL.");
            return;
        }

        if (books.isEmpty()) {
            System.out.println("No books in the library yet.");
            return;
        }

        System.out.println("\n---- Library Book List ----");

        for (Book b : books) {
            System.out.println(b);
        }
    }

    // ==============================
    // ISSUE BOOK
    // ==============================

    public void issueBook(int bookId, String studentName)
            throws BookNotFoundException {

        if (!loadFromDatabase()) {
            System.out.println("Database unavailable. Issue cancelled.");
            return;
        }

        Book book = findBookById(bookId);

        if (book == null) {
            throw new BookNotFoundException(
                "Book with ID " + bookId + " not found."
            );
        }

        if (book.isIssued()) {
            System.out.println(
                "Book is already issued to " + book.getIssuedTo()
            );
            return;
        }

        if (studentName == null || studentName.isBlank()) {
            System.out.println("Student name cannot be empty.");
            return;
        }

        try {
            boolean updated = bookDAO.setIssuedStatus(
                bookId, true, studentName.trim()
            );

            if (updated) {
                book.setIssued(true);
                book.setIssuedTo(studentName.trim());

                System.out.println(
                    "Book issued successfully to " + studentName
                );
            } else {
                System.out.println("Book status was not updated.");
            }

        } catch (SQLException e) {
            System.err.println("Could not issue book in MySQL.");
            e.printStackTrace();
        }
    }

    // ==============================
    // RETURN BOOK
    // ==============================

    public void returnBook(int bookId)
            throws BookNotFoundException {

        if (!loadFromDatabase()) {
            System.out.println("Database unavailable. Return cancelled.");
            return;
        }

        Book book = findBookById(bookId);

        if (book == null) {
            throw new BookNotFoundException(
                "Book with ID " + bookId + " not found."
            );
        }

        if (!book.isIssued()) {
            System.out.println("This book was not issued.");
            return;
        }

        try {
            boolean updated = bookDAO.setIssuedStatus(
                bookId, false, "N/A"
            );

            if (updated) {
                book.setIssued(false);
                book.setIssuedTo("N/A");

                System.out.println("Book returned successfully.");
            } else {
                System.out.println("Book status was not updated.");
            }

        } catch (SQLException e) {
            System.err.println("Could not return book in MySQL.");
            e.printStackTrace();
        }
    }

    // ==============================
    // REMOVE BOOK
    // ==============================

    public void removeBook(int bookId)
            throws BookNotFoundException {

        if (!loadFromDatabase()) {
            System.out.println("Database unavailable. Removal cancelled.");
            return;
        }

        Book book = findBookById(bookId);

        if (book == null) {
            throw new BookNotFoundException(
                "Book with ID " + bookId + " not found."
            );
        }

        try {
            boolean deleted = bookDAO.deleteBook(bookId);

            if (deleted) {
                books.remove(book);
                System.out.println("Book removed from MySQL successfully.");
            } else {
                System.out.println("Book was not deleted.");
            }

        } catch (SQLException e) {
            System.err.println("Could not remove book from MySQL.");
            e.printStackTrace();
        }
    }

    // ==============================
    // UPDATE BOOK DETAILS
    // ==============================

    public void updateBook(Book book) {

        try {
            boolean updated = bookDAO.updateBook(book);

            if (updated) {
                loadFromDatabase();
                System.out.println("Book updated successfully.");
            } else {
                System.out.println("Book ID not found.");
            }

        } catch (SQLException e) {
            System.err.println("Could not update book in MySQL.");
            e.printStackTrace();
        }
    }

    // ==============================
    // FIND BOOK BY ID
    // ==============================

    private Book findBookById(int bookId) {

        for (Book b : books) {
            if (b.getBookId() == bookId) {
                return b;
            }
        }

        return null;
    }

    // ==============================
    // GET BOOKS FOR GUI
    // ==============================

    public ArrayList<Book> getBooks() {

        if (!loadFromDatabase()) {
            System.err.println(
                "Warning: displaying the last successfully loaded book list."
            );
        }

        return new ArrayList<>(books);
    }
}
