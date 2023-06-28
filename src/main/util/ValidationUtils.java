package main.util;

import main.model.Cart;

public class ValidationUtils {

    private ValidationUtils() {
        throw new IllegalStateException("Cannot create instance for utility class");
    }

    /**
     * Validate whether the cart is ready for checkout
     * @param cart: Cart object which has to be validated
     * @return Boolean value whether cart is valid
     */
    public static boolean validateCartForCheckout(Cart cart) {
        return !(cart != null && !cart.getCartItems().isEmpty() && cart.getCustomer() != null);
    }
}
