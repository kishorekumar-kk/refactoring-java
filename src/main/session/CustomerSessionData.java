package main.session;


import main.model.Cart;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class which holds the session information of customer
 */
public class CustomerSessionData {

    private static CustomerSessionData instance;

    private final ConcurrentHashMap<String, Cart> customerSessionMap;

    public static synchronized CustomerSessionData getInstance() {
        if (instance == null) {
            instance = new CustomerSessionData();
        }
        return instance;
    }
    private CustomerSessionData() {
        this.customerSessionMap = new ConcurrentHashMap<>();
    }

    public Boolean cartExists(String customerGUID) {
        return customerSessionMap.containsKey(customerGUID);
    }

    public void addCartForCustomer(String customerGUID, Cart cart) {
        customerSessionMap.put(customerGUID, cart);
    }

    public void clearCartForCustomer(String customerGUID) {
        customerSessionMap.remove(customerGUID);
    }

    public Optional<Cart> getCartForCustomer(String customerGUID) {
        return Optional.ofNullable(customerSessionMap.get(customerGUID));
    }
}
