package service;

import model.User;

public class FineCalculator {
    public double calculateFine(User user, long daysLate) {
        FineStrategy strategy = FineStrategyFactory.getStrategy(user.getMembershipType());
        return strategy.calculateFine(daysLate);
    }
}
