package main;


import main.application.ApplicationState;
import main.application.ApplicationStateMachine;
import main.data.MovieData;
import main.model.Cart;
import main.model.Customer;
import main.service.MovieOrder;
import main.utils.IOUtils;

import java.util.Optional;
import java.util.UUID;

public class CommandLineApplication {

    public static void main(String[] args) {
        defaultValidation();
        System.out.println("<<<<<<<<<<<<<<< Starting application >>>>>>>>>>>>>>>");
        ApplicationStateMachine applicationStateMachine = new ApplicationStateMachine();
        do {
            System.out.println("Select option from above");
            Optional<String> userInput = IOUtils.getInstance().getStringInput(0, true);
            applicationStateMachine.processUserInput(userInput.orElse("a"));
        } while (applicationStateMachine.getCurrentState() != ApplicationState.EXIT);
        System.out.println("Bye!!!");
    }

    static void defaultValidation() {
        String expected = "Rental Record for C. U. Stomer\n\tYou've Got Mail\t3.5\n\tMatrix\t2.0\nAmount owed is 5.5\nYou earned 2 frequent points\n";

        Customer customer = new Customer("C. U. Stomer", UUID.randomUUID().toString());

        MovieOrder movieOrder = new MovieOrder();
        Cart cart = movieOrder.createCart(customer);
        movieOrder.addProductToCart(cart, MovieData.getInstance().findMovieByMovieId("F001").get(), 3);
        movieOrder.addProductToCart(cart, MovieData.getInstance().findMovieByMovieId("F002").get(), 1);
        movieOrder.addProductToCart(cart, MovieData.getInstance().findMovieByMovieId("F002").get(), 3);
        movieOrder.removeProductFromCart(cart, MovieData.getInstance().findMovieByMovieId("F002").get(), 3);
        String result = movieOrder.checkout(cart);

        if (!result.equals(expected)) {
            throw new AssertionError("Expected: " + System.lineSeparator() + String.format(expected) + System.lineSeparator() + System.lineSeparator() + "Got: " + System.lineSeparator() + result);
        }

        System.out.println("Success");
        System.out.println(result);
        System.out.println("Primary validation passed");
    }

}
