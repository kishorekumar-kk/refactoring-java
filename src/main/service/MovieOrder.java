package main.service;

import main.data.MovieOrderData;
import main.exception.OrderProcessingException;
import main.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static main.util.ValidationUtils.validateCartForCheckout;

public class MovieOrder implements Order {

    /**
     * Over-ridden method responsible for completing checkout
     * of movies from cart.
     * @param cart: Cart which has to be checked out
     * @return: value that is used to print receipt for order
     */
    @Override
    public String checkout(Cart cart) {
        if(validateCartForCheckout(cart)) {
            throw new OrderProcessingException(String.format("Invalid cart: %s", cart));
        }
        AtomicReference<Double> totalAmount = new AtomicReference<>((double) 0);
        AtomicInteger frequentEnterPoints = new AtomicInteger();

        StringBuilder result = new StringBuilder("Rental Record for " + cart.getCustomer().getName() + "\n");

        List<MovieRental> rentalsCheckedOut = new ArrayList<>();
        for (CartItem item : cart.getCartItems().values()) {
            Movie movie = (Movie) item.getItem();
            MovieType type = movie.getType();
            double amount = type.calculateRentals(item.getQuantity());
            frequentEnterPoints.getAndIncrement();
            if (type == MovieType.NEW && item.getQuantity() > 2) frequentEnterPoints.getAndIncrement();
            result.append("\t").append(movie.getTitle()).append("\t").append(amount).append("\n");
            totalAmount.updateAndGet(v -> v + amount);
            rentalsCheckedOut.add(new MovieRental(movie.getMovieId(), item.getQuantity()));
        }
        result.append("Amount owed is ").append(totalAmount.get()).append("\n");
        result.append("You earned ").append(frequentEnterPoints.get()).append(" frequent points\n");
        cart.getCartItems().clear();
        cart.getCustomer().addLoyaltyPoints(frequentEnterPoints.get());
        MovieOrderData.getInstance().addMovieOrderData(cart.getCustomer().getCustomerGUID(), rentalsCheckedOut);
        return result.toString();
    }

}
