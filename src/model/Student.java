package model;

public class Student extends  User {

    public Student(String id, String name, String email, String contactNumber) {
        super(id, name, email, contactNumber, MembershipType.STUDENT);
    }

    @Override
    public int getBorrowLimit() {
        return 0;
    }

    @Override
    public int getBorrowDays() {
        return 0;
    }
}
