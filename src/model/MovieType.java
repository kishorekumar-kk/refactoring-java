package model;

/**
 * Enum class that defines the category of movies rented
 * from application. As rent is calculated based on
 * category of movie, method added here in this class
 * to calculate rent for movie.
 */
public enum MovieType {
    NEW(0, 0, 3),
    REGULAR(2, 2, 1.5),
    CHILDREN(1.5, 3, 1.5);

    private final double basePrice;
    private final double baseDays;
    private final double tariffRate;

    MovieType(double basePrice, double baseDays, double tariffRate) {
        this.basePrice = basePrice;
        this.baseDays = baseDays;
        this.tariffRate = tariffRate;
    }

    /**
     * Method to calculate the movie rent based
     * on no of days it is rented for
     * @param days: No of days movie is rented
     * @return Calculated rent based on type of movie
     */
    public double calculateRentals(int days) {
        double rental = this.basePrice;
        if ((days - this.baseDays) > 0) {
            rental += this.tariffRate * (days - this.baseDays);
        }
        return rental;
    }

}
