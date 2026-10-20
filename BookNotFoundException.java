/**
 * BookNotFoundException.java
 * Custom checked exception thrown when a requested Book ID does not exist
 * in the library. Demonstrates basic (custom) exception handling.
 */
public class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}
