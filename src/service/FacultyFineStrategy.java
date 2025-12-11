package service;

public class FacultyFineStrategy implements  FineStrategy {
    private static final double PER_DAY = 20.0; // per day for faculty

    @Override
    public double calculateFine(long daysLate) {
        return daysLate > 0 ? daysLate * PER_DAY : 0.0;
    }
}
