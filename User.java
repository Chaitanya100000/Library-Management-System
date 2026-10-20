/**
 * User.java
 * Abstract base class representing any person who uses the library system.
 * Demonstrates: Inheritance (Student & Librarian extend this class),
 *               Abstraction (abstract getRole() method), Encapsulation.
 */
public abstract class User {
    protected String userId;
    protected String name;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    // Every subclass must define what "role" it plays in the system
    public abstract String getRole();

    @Override
    public String toString() {
        return "User ID: " + userId + " | Name: " + name + " | Role: " + getRole();
    }
}
