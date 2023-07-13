package main.model;

public class Movie extends Product {
    private final String movieId;
    private String title;
    private MovieType type;
    private Boolean isDeleted;

    public Movie(String movieId, String title, MovieType type) {
        super(movieId);
        this.movieId = movieId;
        this.title = title;
        this.type = type;
        this.isDeleted = false;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieType getType() {
        return type;
    }

    public void setType(MovieType type) {
        this.type = type;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public double calculatePrice(int quantity) {
        return type.calculateRentals(quantity);
    }

    @Override
    public int getLoyaltyPoints(int quantity) {
        if (quantity > 2 && type == MovieType.NEW) {
            return 2;
        }
        return super.getLoyaltyPoints(quantity);
    }
}
