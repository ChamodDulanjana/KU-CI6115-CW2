package model;

public abstract class User {
    protected String id;
    protected String name;
    protected String email;
    protected String contactNumber;
    protected MembershipType membershipType;

    public User(String id, String name, String email, String contactNumber, MembershipType membershipType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.membershipType = membershipType;
    }

    public String getId() {
        return id;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public String getName() {
        return name;
    }

    public abstract int getBorrowLimit();
    public abstract int getBorrowDays();

    @Override
    public String toString() {
        return "[" + id + "] " + name + " (" + membershipType + ")";
    }
}
