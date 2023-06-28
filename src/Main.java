import data.MovieData;
import model.Cart;
import model.Customer;
import service.MovieOrder;


public class Main {

    public static void main(String[] args) {
        String expected = "Rental Record for C. U. Stomer\n\tYou've Got Mail\t3.5\n\tMatrix\t2.0\nAmount owed is 5.5\nYou earned 2 frequent points\n";

        Customer customer = new Customer("C. U. Stomer");

        MovieOrder movieOrder = new MovieOrder();
        Cart cart = movieOrder.createCart(customer);
        movieOrder.addProductToCart(cart, MovieData.getInstance().getMovie("F001"), 3);
        movieOrder.addProductToCart(cart, MovieData.getInstance().getMovie("F002"), 1);
        movieOrder.addProductToCart(cart, MovieData.getInstance().getMovie("F002"), 3);
        movieOrder.removeProductFromCart(cart, MovieData.getInstance().getMovie("F002"), 3);
        String result = movieOrder.checkout(cart);

        if (!result.equals(expected)) {
            throw new AssertionError("Expected: " + System.lineSeparator() + String.format(expected) + System.lineSeparator() + System.lineSeparator() + "Got: " + System.lineSeparator() + result);
        }

        System.out.println("Success");
        System.out.println(result);
        System.out.println("Primary validation passed");
    }
}
