package service;

import model.MembershipType;

public class FineStrategyFactory {

    public static FineStrategy getStrategy(MembershipType type) {
        if (type == null) throw new IllegalArgumentException("MembershipType cannot be null");
        return switch (type) {
            case STUDENT -> new StudentFineStrategy();
            case FACULTY -> new FacultyFineStrategy();
            case GUEST -> new GuestFineStrategy();
        };
    }
}
