package main.model;

import java.util.UUID;

public class Customer {
    private final String name;

    private final String username;

    private final String customerGUID;

    private Integer loyaltyPoints;

    public Customer(String name, String username) {
        this.name = name;
        this.loyaltyPoints = 0;
        this.username = username;
        this.customerGUID = UUID.randomUUID().toString();

    }

    public String getName() {
        return name;
    }

    public void addLoyaltyPoints(Integer points) {
        this.loyaltyPoints += points;
    }

    public String getCustomerGUID() {
        return customerGUID;
    }

    public Integer getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "name:" + name;
    }
}
