package model;

public class Movie extends Product {
    private final String movieId;
    private final String title;
    private final MovieType type;

    public Movie(String movieId, String title, MovieType type) {
        super(movieId);
        this.movieId = movieId;
        this.title = title;
        this.type = type;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public MovieType getType() {
        return type;
    }

    @Override
    public double calculatePrice(int quantity) {
        return type.calculateRentals(quantity);
    }
}
