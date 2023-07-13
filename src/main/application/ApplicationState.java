package main.application;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enum class defined with all the possible states of application
 */
public enum ApplicationState {
    LANDING_MENU("a", "Main Menu"),
    ADMIN_MENU("b", "Login as admin"),
    CUSTOMER_MENU("c", "Customer Menu"),
    CUSTOMER_LOGIN("d", "Login as customer"),
    CREATE_CUSTOMER("e", "Create customer"),
    CUSTOMER_MAIN_MENU("f", "Customer main menu"),
    ADMIN_MAIN_MENU("g", "Admin main menu"),
    CUSTOMER_BROWSE_MOVIES("h", "Browse movies"),
    GET_CART_INFO("i", "Get cart info"),
    CHECKOUT("j", "Checkout"),
    REDUCE_RENTAL_DURATION("k", "Fewer days for renting movies"),
    ADD_MOVIE_TO_CART("l", "Add movie to cart"),
    UPDATE_MOVIE_TO_CART("m", "Update movie to cart"),
    DELETE_MOVIE_FROM_CART("n", "Delete movie from cart"),
    ADMIN_BROWSE_MOVIE("o", "Browse movies"),
    ADD_MOVIE("p", "Add movie"),
    UPDATE_MOVIE("q", "Update movie"),
    DELETE_MOVIE("r", "Delete movie"),
    LOGOUT("s", "Logout"),
    ENABLE_MOVIE("t", "Enable movie"),
    EXIT("z", "Exit");

    private final String option;
    private final String menu;

    ApplicationState(String option, String menu) {
        this.option = option;
        this.menu = menu;
    }

    public String getOption() {
        return option;
    }

    public String getMenu() {
        return menu;
    }

    /**
     * Method used to map the user input with application state
     * @param option: User option
     * @return State that is mapped with user input
     */
    public static Optional<ApplicationState> getApplicationState(String option) {
        return Arrays.stream(ApplicationState.values()).filter(it -> it.getOption().equals(option)).findFirst();
    }
}
