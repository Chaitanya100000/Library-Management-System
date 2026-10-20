/**
 * Librarian.java
 * Represents a Librarian (staff) user of the library.
 * Demonstrates: Inheritance (extends User), Constructor chaining (super()).
 */
public class Librarian extends User {
    private String employeeId;

    public Librarian(String userId, String name, String employeeId) {
        super(userId, name); // calls User's constructor
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    @Override
    public String getRole() {
        return "Librarian";
    }
}
