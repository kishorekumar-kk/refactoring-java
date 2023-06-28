package main.service;

import main.model.Cart;
import main.model.CartItem;
import main.model.Customer;
import main.model.Product;

import java.util.Map;

/**
 * Interface for creation of cart, updation of movie to cart and checking out products from application.
 */
public interface Order {

    /**
     * Method responsible for creation of customer
     * @param customer: Customer to which cart has to be created
     * @return Cart: new cart instance created for customer
     */
    default Cart createCart(Customer customer) {
        return new Cart(customer);
    }

    /**
     * Add product to cart. Here the logic is if product already exist in cart,
     * increment quantity by new value, if not add item to cart with quantity.
     * @param cart: Cart to which product has to be added
     * @param item: Product which has to be added to cart
     * @param quantity: Quantity of product to be added to cart
     */
   default void addProductToCart(Cart cart, Product item, int quantity){
       Map<String, CartItem> cartItems = cart.getCartItems();
       String itemId = item.getId();
       if (cartItems.containsKey(itemId)) {
           cartItems.get(itemId).addQuantity(quantity);
       } else {
           cartItems.put(item.getId(), new CartItem(item, quantity));
       }
   }

    /**
     * Delete product from cart
     * @param cart: Cart from which product has to be deleted
     * @param item: Product which has to be removed from cart
     */
    default void deleteProductFromCart(Cart cart, Product item) {
        cart.getCartItems().remove(item.getId());
    }


    /**
     * Remove product by quantity in cart
     * @param cart: Cart from which product has to be removed
     * @param item: Product which has to be removed
     * @param quantity: Quantity of product that has to be removed
     */
    default void removeProductFromCart(Cart cart, Product item, int quantity) {
        Map<String, CartItem> cartItems = cart.getCartItems();
        String itemId = item.getId();
        if (cartItems.containsKey(itemId)) {
            int updatedQuantity = cartItems.get(item.getId()).getQuantity() - quantity;
            if (updatedQuantity <= 0) {
                deleteProductFromCart(cart, item);
            } else {
                cartItems.get(itemId).setQuantity(updatedQuantity);
            }
        }
    }

    /**
     * Method responsible for completing checkout
     * of products from cart.
     * @param cart: Cart which has to be checked out
     * @return: value that is used to print receipt for order
     * Note: Cart will be cleared with checkout process
     */
    String checkout(Cart cart);
}
