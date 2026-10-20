
import java.sql.*;
import java.util.ArrayList;

public class BookDAO {

    // Add a book
    public boolean addBook(Book book) throws SQLException {
        String sql = """
            INSERT INTO books
                (book_id, title, author, category, is_available, issued_to)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, book.getBookId());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getGenre());
            ps.setBoolean(5, !book.isIssued());
            ps.setString(6, book.getIssuedTo());

            return ps.executeUpdate() > 0;
        }
    }

    // Get all books
    public ArrayList<Book> getAllBooks() throws SQLException {
        ArrayList<Book> books = new ArrayList<>();

        String sql = """
            SELECT book_id, title, author, category,
                   is_available, issued_to
            FROM books
            ORDER BY book_id
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Book book = new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category")
                );

                book.setIssued(!rs.getBoolean("is_available"));

                String issuedTo = rs.getString("issued_to");
                book.setIssuedTo(
                    issuedTo == null || issuedTo.isBlank()
                        ? "N/A" : issuedTo
                );

                books.add(book);
            }
        }

        return books;
    }

    // Delete a book
    public boolean deleteBook(int bookId) throws SQLException {
        String sql = "DELETE FROM books WHERE book_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            return ps.executeUpdate() > 0;
        }
    }

    // Issue or return a book
    public boolean setIssuedStatus(
            int bookId, boolean issued, String studentName)
            throws SQLException {

        String sql = """
            UPDATE books
            SET is_available = ?, issued_to = ?
            WHERE book_id = ?
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, !issued);
            ps.setString(2, issued ? studentName : "N/A");
            ps.setInt(3, bookId);

            return ps.executeUpdate() > 0;
        }
    }

    // Update book details
    public boolean updateBook(Book book) throws SQLException {
        String sql = """
            UPDATE books
            SET title = ?, author = ?, category = ?
            WHERE book_id = ?
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getGenre());
            ps.setInt(4, book.getBookId());

            return ps.executeUpdate() > 0;
        }
    }
}
