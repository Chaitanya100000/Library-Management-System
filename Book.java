/**
 * Book.java
 * Represents a single Book entity in the library.
 * Demonstrates: Encapsulation (private fields + public getters/setters),
 *               Constructors (default + parameterized), Method overriding (toString).
 */
public class Book {
    // ---- Private fields (Encapsulation) ----
    private int bookId;
    private String title;
    private String author;
    private String genre;
    private boolean isIssued;
    private String issuedTo;

    // ---- Default constructor ----
    public Book() {
        this.isIssued = false;
        this.issuedTo = "N/A";
    }

    // ---- Parameterized constructor ----
    public Book(int bookId, String title, String author, String genre) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isIssued = false;
        this.issuedTo = "N/A";
    }

    // ---- Getters & Setters ----
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isIssued() {
        return isIssued;
    }

    public void setIssued(boolean issued) {
        isIssued = issued;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(String issuedTo) {
        this.issuedTo = issuedTo;
    }

    // ---- Display format used by menu ----
    @Override
    public String toString() {
        String status = isIssued ? "Issued to " + issuedTo : "Available";
        return String.format("ID: %-5d | Title: %-25s | Author: %-20s | Genre: %-15s | Status: %s",
                bookId, title, author, genre, status);
    }

    // ---- Format used to persist a book as one line in the data file ----
    public String toFileString() {
        return bookId + "," + title + "," + author + "," + genre + "," + isIssued + "," + issuedTo;
    }
}
