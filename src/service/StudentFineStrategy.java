package service;

public class StudentFineStrategy implements  FineStrategy {
    private static final double PER_DAY = 50.0; // per day for students

    @Override
    public double calculateFine(long daysLate) {
        return daysLate > 0 ? daysLate * PER_DAY : 0.0;
    }
}
