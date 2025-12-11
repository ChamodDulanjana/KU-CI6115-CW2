package service;

public class GuestFineStrategy implements  FineStrategy {
    private static final double PER_DAY = 100.0; // per day for guests

    @Override
    public double calculateFine(long daysLate) {
        return daysLate > 0 ? daysLate * PER_DAY : 0.0;
    }
}
