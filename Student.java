/**
 * Student.java
 * Represents a Student user of the library.
 * Demonstrates: Inheritance (extends User), Constructor chaining (super()).
 */
public class Student extends User {
    private String rollNumber;

    public Student(String userId, String name, String rollNumber) {
        super(userId, name); // calls User's constructor
        this.rollNumber = rollNumber;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    @Override
    public String getRole() {
        return "Student";
    }
}
