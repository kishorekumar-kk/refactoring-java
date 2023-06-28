package main.model;

public class CartItem {
    private final Product item;

    private Integer quantity;

    public CartItem(Product item, Integer quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(Integer quantity) {
        this.quantity += quantity;
    }

    public Product getItem() {
        return item;
    }
}
