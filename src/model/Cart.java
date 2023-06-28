package model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
    private final Customer customer;

    private final Map<String, CartItem> cartItems;

    public Cart(Customer customer) {
        this.customer = customer;
        this.cartItems = new LinkedHashMap<>();
    }

    public Customer getCustomer() {
        return customer;
    }

    public Map<String, CartItem> getCartItems() {
        return cartItems;
    }

}
