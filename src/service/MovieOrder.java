package service;

import model.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MovieOrder implements Order {

    /**
     * Over-ridden method responsible for completing checkout
     * of movies from cart.
     * @param cart: Cart which has to be checked out
     * @return: value that is used to print receipt for order
     */
    @Override
    public String checkout(Cart cart) {
        AtomicReference<Double> totalAmount = new AtomicReference<>((double) 0);
        AtomicInteger frequentEnterPoints = new AtomicInteger();
        StringBuilder result = new StringBuilder("Rental Record for " + cart.getCustomer().getName() + "\n");
        for (CartItem item : cart.getCartItems().values()) {
            Movie movie = (Movie) item.getItem();
            MovieType type = movie.getType();
            double amount = type.calculateRentals(item.getQuantity());
            frequentEnterPoints.getAndIncrement();
            if (type == MovieType.NEW && item.getQuantity() > 2) frequentEnterPoints.getAndIncrement();
            result.append("\t").append(movie.getTitle()).append("\t").append(amount).append("\n");
            totalAmount.updateAndGet(v -> v + amount);
        }
        result.append("Amount owed is ").append(totalAmount.get()).append("\n");
        result.append("You earned ").append(frequentEnterPoints.get()).append(" frequent points\n");
        cart.getCartItems().clear();
        return result.toString();
    }

}
