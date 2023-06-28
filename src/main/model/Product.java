package main.model;

/**
 * Abstract class of product which is used
 * across the various kinds of products in
 * this application and currently we have
 * only one category of product i.e., Movie
 */
public abstract class Product {

    private final String id;

    protected Product(String id) {
        this.id = id;
    }

    abstract double calculatePrice(int quantity);

    public int getLoyaltyPoints(int quantity){
        return 1;
    }

    public String getId() {
        return id;
    }
}
