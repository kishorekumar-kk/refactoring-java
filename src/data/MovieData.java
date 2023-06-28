package data;


import model.Movie;
import model.MovieType;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data class holding map of movies used wide across this application
 */
public class MovieData {

    private static MovieData instance;

    private final ConcurrentHashMap<String, Movie> moviesMap;

    private MovieData() {
        List<Movie> movies = Arrays.asList(
                new Movie("F001", "You've Got Mail", MovieType.REGULAR),
                new Movie("F002", "Matrix", MovieType.REGULAR),
                new Movie("F003", "Cars", MovieType.CHILDREN),
                new Movie("F004", "Fast & Furious X", MovieType.NEW));
        moviesMap = new ConcurrentHashMap<>();
        movies.forEach(it -> moviesMap.put(it.getMovieId(), it));
    }

    public static synchronized MovieData getInstance() {
        if (instance == null) {
            instance = new MovieData();
        }
        return instance;
    }

    public Movie getMovie(String movieId) {
        return moviesMap.get(movieId);
    }

}
