package main.data;


import main.exception.ApplicationException;
import main.model.Movie;
import main.model.MovieType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

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

    public boolean containsMovie(String movieId) {
        return moviesMap.containsKey(movieId);
    }

    public void addMovie(Movie movie) {
        moviesMap.put(movie.getMovieId(), movie);
    }

    public Stream<Movie> fetchUnDeletedMovies() {
        return moviesMap.values().stream().filter(movie -> !movie.isDeleted());
    }

    public Stream<Movie> fetchAllMovies() {
        return moviesMap.values().stream();
    }

    public Optional<Movie> findMovieByMovieId(String movieId) {
        return Optional.ofNullable(moviesMap.get(movieId));
    }

    public Movie getMovie(String movieId) {
        Movie movie = moviesMap.get(movieId);
        if (movie != null) {
            return movie;
        } else {
            throw new ApplicationException(String.format("Movie not found: %s", movieId));
        }
    }

}
