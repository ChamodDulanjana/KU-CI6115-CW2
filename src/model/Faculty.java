package model;

public class Faculty extends User{
    public Faculty(String id, String name, String email, String contactNumber) {
        super(id, name, email, contactNumber, MembershipType.FACULTY);
    }

    @Override
    public int getBorrowLimit() {
        return 10;
    }

    @Override
    public int getBorrowDays() {
        return 30;
    }
}
