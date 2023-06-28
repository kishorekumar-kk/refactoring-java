package main.model;

import java.util.UUID;

public class Customer {
    private final String name;


    private final String customerGUID;

    private Integer loyaltyPoints;

    public Customer(String name) {
        this.name = name;
        this.loyaltyPoints = 0;
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
}
