package main.data;

import main.model.MovieRental;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MovieOrderData {

    private static MovieOrderData instance;

    private final Map<String, List<MovieRental>> orderData;

    private MovieOrderData() {
        orderData = new ConcurrentHashMap<>();
    }

    public static synchronized MovieOrderData getInstance() {
        if (instance == null) {
            instance = new MovieOrderData();
        }
        return instance;
    }

    public List<MovieRental> getOrderData(String customerGUID) {
        return orderData.get(customerGUID);
    }

    public void addMovieOrderData(String customerGUID, List<MovieRental> movieRental) {
        orderData.merge(customerGUID, movieRental, (existingList, newList) -> {
            existingList.addAll(newList);
            return existingList;
        });
    }
}
