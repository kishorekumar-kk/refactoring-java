package main;

import main.data.MovieData;
import main.data.MovieOrderData;
import main.exception.ApplicationException;
import main.model.Cart;
import main.model.Customer;
import main.model.MovieRental;
import main.service.MovieOrder;

import java.util.List;


public class Main {

    public static void main(String[] args) {
        try {
            Customer customer = new Customer("C. U. Stomer");
            MovieOrder movieOrder = new MovieOrder();
            Cart cart = movieOrder.createCart(customer);
            movieOrder.addProductToCart(cart, MovieData.getInstance().getMovie("F001"), 3);
            movieOrder.addProductToCart(cart, MovieData.getInstance().getMovie("F002"), 1);
            movieOrder.addProductToCart(cart, MovieData.getInstance().getMovie("F002"), 3);
            movieOrder.removeProductFromCart(cart, MovieData.getInstance().getMovie("F002"), 3);
            validateResults(movieOrder.checkout(cart), customer);
            System.out.println("Success");
        } catch (ApplicationException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    static void validateResults(String result, Customer customer) {
        String expected = "Rental Record for C. U. Stomer\n\tYou've Got Mail\t3.5\n\tMatrix\t2.0\nAmount owed is 5.5\nYou earned 2 frequent points\n";

        if (!result.equals(expected)) {
            throw new AssertionError("Expected: " + System.lineSeparator() + String.format(expected) + System.lineSeparator() + System.lineSeparator() + "Got: " + System.lineSeparator() + result);
        }

        List<MovieRental> rentals = MovieOrderData.getInstance().getOrderData(customer.getCustomerGUID());
        if (rentals.size() != 2) {
            throw new AssertionError(String.format("Expected: 2 rentals for customer %s, Got %d rentals", customer.getName(), rentals.size()));
        }

        if (customer.getLoyaltyPoints() != 2) {
            throw new AssertionError(String.format("Expected: 2 loyalty points for customer %s, Got %d", customer.getName(), customer.getLoyaltyPoints()));
        }

        System.out.println(result);
    }
}
