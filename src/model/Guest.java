package model;

public class Guest extends  User {
    public Guest(String id, String name, String email, String contactNumber) {
        super(id, name, email, contactNumber, MembershipType.GUEST);
    }

    @Override
    public int getBorrowLimit() {
        return 1;
    }

    @Override
    public int getBorrowDays() {
        return 7;
    }
}
