package main.application;

import main.data.CustomerData;
import main.data.MovieData;
import main.model.Cart;
import main.model.Customer;
import main.model.Movie;
import main.model.MovieType;
import main.service.MovieOrder;
import main.session.CustomerSessionData;
import main.utils.IOUtils;

import java.util.*;

import static main.application.ApplicationState.*;

/**
 * State machine class used to manage application run based on user inputs
 */
public class ApplicationStateMachine {
    public static final ApplicationState initialState = ApplicationState.LANDING_MENU;
    public final Map<ApplicationState, List<ApplicationState>> stateMachineMap;
    private final MovieOrder movieOrder;
    protected Customer customer;
    private ApplicationState currentState = initialState;
    private ApplicationState previousState = null;

    public ApplicationStateMachine() {
        this.stateMachineMap = createStateMachineMap();
        this.movieOrder = new MovieOrder();
        printMenu();
    }

    /**
     * Defines possible states for specific state as a map
     * which is used for application run
     *
     * @return Map of state with possible states
     */
    private Map<ApplicationState, List<ApplicationState>> createStateMachineMap() {
        Map<ApplicationState, List<ApplicationState>> map = new EnumMap<>(ApplicationState.class);

        map.put(initialState, List.of(CUSTOMER_MENU, ADMIN_MENU, EXIT));
        map.put(CUSTOMER_MENU, List.of(CREATE_CUSTOMER, CUSTOMER_LOGIN, initialState, EXIT));
        map.put(CREATE_CUSTOMER, List.of(CUSTOMER_MAIN_MENU));
        map.put(CUSTOMER_LOGIN, List.of(CUSTOMER_MAIN_MENU));
        map.put(CUSTOMER_MAIN_MENU, List.of(CUSTOMER_BROWSE_MOVIES, GET_CART_INFO, CHECKOUT, LOGOUT));
        map.put(CUSTOMER_BROWSE_MOVIES, List.of(CUSTOMER_BROWSE_MOVIES, ADD_MOVIE_TO_CART, UPDATE_MOVIE_TO_CART, CUSTOMER_MAIN_MENU));
        map.put(GET_CART_INFO, List.of(CUSTOMER_MAIN_MENU, UPDATE_MOVIE_TO_CART, REDUCE_RENTAL_DURATION, DELETE_MOVIE_FROM_CART, CHECKOUT));
        map.put(CHECKOUT, List.of(CUSTOMER_MAIN_MENU));
        map.put(ADD_MOVIE_TO_CART, List.of(CUSTOMER_BROWSE_MOVIES, ADD_MOVIE_TO_CART, CUSTOMER_MAIN_MENU));
        map.put(REDUCE_RENTAL_DURATION, List.of(CUSTOMER_BROWSE_MOVIES, GET_CART_INFO, CHECKOUT, CUSTOMER_MAIN_MENU));
        map.put(UPDATE_MOVIE_TO_CART, List.of(CUSTOMER_BROWSE_MOVIES, CUSTOMER_MAIN_MENU));
        map.put(DELETE_MOVIE_FROM_CART, List.of(CUSTOMER_BROWSE_MOVIES, CUSTOMER_MAIN_MENU));
        map.put(ADMIN_MENU, List.of(ADMIN_MAIN_MENU, EXIT, initialState));
        map.put(ADMIN_MAIN_MENU, List.of(ADMIN_BROWSE_MOVIE, ADD_MOVIE, LOGOUT));
        map.put(ADMIN_BROWSE_MOVIE, List.of(UPDATE_MOVIE, ENABLE_MOVIE, DELETE_MOVIE, ADMIN_MAIN_MENU));
        map.put(ADD_MOVIE, List.of(ADMIN_BROWSE_MOVIE, ADMIN_MAIN_MENU));
        map.put(ENABLE_MOVIE, List.of(ADMIN_BROWSE_MOVIE, ADMIN_MAIN_MENU));
        map.put(UPDATE_MOVIE, List.of(ADMIN_BROWSE_MOVIE, ADMIN_MAIN_MENU));
        map.put(DELETE_MOVIE, List.of(ADMIN_BROWSE_MOVIE, ADMIN_MAIN_MENU));
        map.put(LOGOUT, List.of(initialState));
        return map;
    }

    /**
     * Method used to process user input with
     * user intended action
     *
     * @param userOption: Input mapping with application state
     */
    public void processUserInput(String userOption) {
        Optional<ApplicationState> inputState = ApplicationState.getApplicationState(userOption);
        if (inputState.isPresent() && isValidTransition(currentState, inputState.get())) {
            previousState = currentState;
            currentState = inputState.get();
            switch (currentState) {
                case LOGOUT:
                    currentState = LANDING_MENU;
                    if (customer != null) {
                        CustomerSessionData.getInstance().clearCartForCustomer(customer.getCustomerGUID());
                        customer = null;
                    }
                    break;
                case CUSTOMER_MENU:
                    System.out.println("Here you can go through the customer options");
                    break;
                case CREATE_CUSTOMER:
                    createCustomer();
                    break;
                case CUSTOMER_LOGIN:
                    authenticateCustomer();
                    break;
                case CUSTOMER_BROWSE_MOVIES:
                    listMovies();
                    break;
                case ADMIN_BROWSE_MOVIE:
                    listMoviesForAdmin();
                    break;
                case ADD_MOVIE_TO_CART:
                    addMovieToCart();
                    break;
                case DELETE_MOVIE_FROM_CART:
                    deleteMovieFromCart();
                    break;
                case UPDATE_MOVIE_TO_CART:
                    updateMovieToCart();
                    break;
                case REDUCE_RENTAL_DURATION:
                    reduceRentalDuration();
                    break;
                case GET_CART_INFO:
                    getCartInfo();
                    break;
                case CHECKOUT:
                    checkout();
                    break;
                case ADD_MOVIE:
                    addMovie();
                    break;
                case UPDATE_MOVIE:
                    updateMovie();
                    break;
                case DELETE_MOVIE:
                    deleteMovie();
                    break;
                case ENABLE_MOVIE:
                    enableMovie();
                    break;
                default:
                    break;
            }
        } else {
            System.out.println("Invalid option, Select option from below");
        }
        printMenu();
    }

    /**
     * Method to handle add movie option.
     * This option is specifically used with admin
     */
    private void addMovie() {
        System.out.println("Enter movie name");
        Optional<String> name = IOUtils.getInstance().getStringInput(0, true);
        System.out.println("Enter movie type");
        Optional<String> type = IOUtils.getInstance().getStringInput(0, true);
        System.out.println("Enter movie Id");
        Optional<String> movieId = IOUtils.getInstance().getStringInput(0, true);
        if (name.isPresent() && type.isPresent() && movieId.isPresent()
                && !MovieData.getInstance().containsMovie(movieId.get())) {
            Movie movie = new Movie(movieId.get(), name.get(), MovieType.NEW);
            MovieData.getInstance().addMovie(movie);
        } else {
            System.out.println("Input(s) not valid");
        }
    }

    /**
     * Method used to update existing movie in system.
     * This option is specifically used with admin
     */
    private void updateMovie() {
        Optional<Movie> movieOptional = getMovieWithUserInput();
        if (movieOptional.isPresent()) {
            try {
                System.out.println("Enter name to be updated");
                Optional<String> name = IOUtils.getInstance().getStringInput(0, false);
                name.ifPresent(s -> movieOptional.get().setTitle(s));
            } catch (InputMismatchException exception) {
                System.out.printf("Not a valid movie name; %s%n", exception.getLocalizedMessage());
                currentState = previousState;
                previousState = initialState;
            }
        } else {
            System.out.println("Movie not found");
        }
    }

    /**
     * Method used to mark movie as deleted.
     * This option is specifically used with admin
     */
    private void deleteMovie() {
        Optional<Movie> movieOptional = getMovieWithUserInput();
        if (movieOptional.isPresent()) {
            movieOptional.get().setDeleted(true);
        } else {
            System.out.println("Movie not found");
        }
    }

    /**
     * Method used to undo delete of movie or enable the movie.
     * This option is specifically used with admin
     */
    private void enableMovie() {
        Optional<Movie> movieOptional = getMovieWithUserInput();
        if (movieOptional.isPresent()) {
            movieOptional.get().setDeleted(false);
        } else {
            System.out.println("Movie not found");
        }
    }

    /**
     * Method used to modify rental duration of movie added to cart
     */
    private void reduceRentalDuration() {
        Optional<Movie> movieOptional = getMovieWithUserInput();
        Optional<Cart> cartOptional = CustomerSessionData.getInstance().getCartForCustomer(customer.getCustomerGUID());
        if (cartOptional.isPresent() && movieOptional.isPresent()) {
            System.out.println("Enter no of days to be reduced");
            Optional<Integer> days = IOUtils.getInstance().getNumericInput(0);
            if (days.isPresent()) {
                movieOrder.removeProductFromCart(cartOptional.get(), movieOptional.get(), days.get());
                System.out.println("Movie updated successfully to Cart");
            }
        } else {
            System.out.println("Cart/Movie doesn't exist");
        }
    }

    /**
     * Method used to modify the movie added to cart.
     */
    private void updateMovieToCart() {
        Optional<Movie> movieOptional = getMovieWithUserInput();
        Optional<Cart> cartOptional = CustomerSessionData.getInstance().getCartForCustomer(customer.getCustomerGUID());
        if (cartOptional.isPresent() && movieOptional.isPresent()) {
            System.out.println("Enter no of days to rent");
            Optional<Integer> days = IOUtils.getInstance().getNumericInput(0);
            if (days.isPresent()) {
                movieOrder.updateProductToCart(cartOptional.get(), movieOptional.get(), days.get());
                System.out.println("Movie updated successfully to Cart");
            }
        } else {
            System.out.println("Cart/Movie doesn't exist");
        }
    }

    /**
     * Method to delete movie from cart.
     */
    private void deleteMovieFromCart() {
        Optional<Movie> movieOptional = getMovieWithUserInput();
        Optional<Cart> cartOptional = CustomerSessionData.getInstance().getCartForCustomer(customer.getCustomerGUID());
        if (cartOptional.isPresent() && movieOptional.isPresent()) {
            movieOrder.deleteProductFromCart(cartOptional.get(), movieOptional.get());
            System.out.println("Movie deleted successfully from Cart");
        } else {
            System.out.println("Cart/Movie doesn't exist");
        }
    }

    /**
     * Method used to get Movie based on user input and
     * get the corresponding movie mapped with the movie
     * code taken as input from user
     *
     * @return Optional movie object based on movie code
     */
    private Optional<Movie> getMovieWithUserInput() {
        System.out.println("Enter movie code");
        Optional<Movie> movieOptional = Optional.empty();
        try {
            Optional<String> movieCode = IOUtils.getInstance().getStringInput(0, false);
            if (movieCode.isPresent()) {
                movieOptional = MovieData.getInstance().findMovieByMovieId(movieCode.get());
            }
        } catch (InputMismatchException exception) {
            System.out.printf("Invalid movie code input; %s %n", exception.getLocalizedMessage());
            currentState = previousState;
            previousState = initialState;
        }
        return movieOptional;
    }

    /**
     * Method used to perform checkout action
     * based on the cart of user
     */
    private void checkout() {
        Optional<Cart> cartOptional = CustomerSessionData.getInstance().getCartForCustomer(customer.getCustomerGUID());
        if (cartOptional.isPresent()) {
            System.out.println(movieOrder.checkout(cartOptional.get()));
            CustomerSessionData.getInstance().clearCartForCustomer(customer.getCustomerGUID());
        } else {
            System.out.println("Cart doesn't exist for customer");
        }
    }

    /**
     * Method to get cart information for a user
     */
    private void getCartInfo() {
        Optional<Cart> cartOptional = CustomerSessionData.getInstance().getCartForCustomer(customer.getCustomerGUID());
        if (cartOptional.isPresent()) {
            System.out.println(movieOrder.getCartInformation(cartOptional.get()));
        } else {
            System.out.println("Cart doesn't exist for customer");
        }
    }

    /**
     * Method used to add movie to cart.
     * This method takes in movie code from IOUtils,
     * parses the code to get movie then adds it to
     * cart with the days user intends to rent the
     * movie for.
     */
    private void addMovieToCart() {
        Optional<Movie> movieOptional = getMovieWithUserInput();
        if (movieOptional.isPresent() && !movieOptional.get().isDeleted()) {
            Cart cart;
            System.out.println("Enter no of days to rent");
            Optional<Integer> days = IOUtils.getInstance().getNumericInput(0);
            Optional<Cart> cartOptional = CustomerSessionData.getInstance().getCartForCustomer(customer.getCustomerGUID());
            if (days.isPresent()) {
                if (cartOptional.isEmpty()) {
                    cart = movieOrder.createCart(customer);
                    CustomerSessionData.getInstance().addCartForCustomer(customer.getCustomerGUID(), cart);
                } else {
                    cart = cartOptional.get();
                }
                movieOrder.addProductToCart(cart, movieOptional.get(), days.get());
                System.out.println("Movie added successfully to Cart");
            } else {
                System.out.println("Rental days has to be a numerical value");
            }
        } else {
            System.out.println("Movie not found, Select Movie code from below list");
            listMovies();
        }
    }

    /**
     * Method used to list down movies that are available for the user.
     */
    private void listMovies() {
        StringBuilder moviesBuilder = new StringBuilder();
        moviesBuilder.append(String.format("%-10s %-20s %-15s %-15s %-15s %-15s%n", "Code", "Title", "Type", "Base price", "Min rental", "Pro-rata rates"));
        MovieData.getInstance().fetchUnDeletedMovies()
                .forEach(movie -> moviesBuilder.append(String.format("%-10s %-20s %-15s %-15s %-15s %-15s%n", movie.getMovieId(), movie.getTitle(), movie.getType(), movie.getType().getBasePrice(), movie.getType().getBaseDays(), movie.getType().getTariffRate())));
        System.out.println(moviesBuilder);
    }

    /**
     * Method that is used to list down all the movies in the system
     * including the one which is marked as deleted by admin
     */
    private void listMoviesForAdmin() {
        StringBuilder moviesBuilder = new StringBuilder();
        moviesBuilder.append(String.format("%-10s %-20s %-15s %-15s %-15s %-15s %-15s%n", "Code", "Title", "Type", "Base price", "Min rental", "Pro-rata rates", "Is deleted"));
        MovieData.getInstance().fetchAllMovies()
                .forEach(movie -> moviesBuilder.append(String.format("%-10s %-20s %-15s %-15s %-15s %-15s %-15s%n", movie.getMovieId(), movie.getTitle(), movie.getType(), movie.getType().getBasePrice(), movie.getType().getBaseDays(), movie.getType().getTariffRate(), movie.isDeleted())));
        System.out.println(moviesBuilder);
    }

    /**
     * Method which takes care of creation of customer in system.
     * Primarily takes in user parameters such as name, username
     * with which user object is created in the system
     */
    private void createCustomer() {
        System.out.println("Enter name");
        try {
            Optional<String> name = IOUtils.getInstance().getStringInput(0, false);
            System.out.println("Enter username");
            Optional<String> username = IOUtils.getInstance().getStringInput(0, false);
            if (name.isPresent() && username.isPresent() && !CustomerData.getInstance().usernameExists(username.get())) {
                customer = new Customer(name.get(), username.get());
                currentState = CUSTOMER_MAIN_MENU;
                CustomerData.getInstance().save(customer);
                System.out.printf("Customer created successfully %nHello %s%n", name.get());
            }
        } catch (InputMismatchException exception) {
            System.out.println("Customer creation failed; name and username are mandatory parameters");
            currentState = previousState;
            previousState = initialState;
        }
    }

    /**
     * Method used to authenticate the user when user logs in to system.
     * This method just check for user existence as there's no implementation of
     * password check in current system
     */
    private void authenticateCustomer() {
        Optional<String> username;
        Optional<Customer> customerOptional;
        System.out.println("Enter your username");
        try {
            username = IOUtils.getInstance().getStringInput(0, false);
            if (username.isPresent()) {
                customerOptional = CustomerData.getInstance().findCustomerByUserName(username.get());
                if (customerOptional.isPresent()) {
                    customer = customerOptional.get();
                    currentState = CUSTOMER_MAIN_MENU;
                    System.out.printf("Hello %s%n", customer.getName());
                } else {
                    System.out.println("Customer not found");
                    currentState = previousState;
                    previousState = initialState;
                }
            }
        } catch (InputMismatchException ex) {
            currentState = previousState;
            previousState = initialState;
            System.out.println("Error logging in; Username is required parameter");
        }
    }

    /**
     * Application has got various states that are mapped to user input.
     * With each state there are only finite number of states, user may
     * move into. This method validates whether user can move to new state
     * from the current state
     *
     * @param currentState: Current state user is in
     * @param newState:     New state based on user input
     * @return Boolean value checking whether the transition is possible
     */
    private boolean isValidTransition(ApplicationState currentState, ApplicationState newState) {
        return stateMachineMap.get(currentState).contains(newState);
    }

    /**
     * Prints the list of menu based on the current state of user
     */
    private void printMenu() {
        if (currentState != EXIT) {
            StringBuilder stringBuilder = new StringBuilder();
            stateMachineMap.get(currentState).forEach(state -> stringBuilder.append(String.format("%s) %s%n", state.getOption(), state.getMenu())));
            System.out.println(stringBuilder);
        }
    }

    public ApplicationState getCurrentState() {
        return currentState;
    }

}
