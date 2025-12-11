package model;

public class Student extends  User {

    public Student(String id, String name, String email, String contactNumber, MembershipType membershipType) {
        super(id, name, email, contactNumber, membershipType);
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
