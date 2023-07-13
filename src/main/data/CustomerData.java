package main.data;


import main.model.Customer;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data class holding the customer data
 */
public class CustomerData {

    private static CustomerData instance;

    private final ConcurrentHashMap<String, Customer> customerMap;

    public static synchronized CustomerData getInstance() {
        if (instance == null) {
            instance = new CustomerData();
        }
        return instance;
    }

    private CustomerData() {
        customerMap = new ConcurrentHashMap<>();
    }

    public Optional<Customer> findCustomerByUserName(String username) {
        return Optional.ofNullable(customerMap.get(username));
    }

    public boolean usernameExists(String username) {
        return customerMap.containsKey(username);
    }

    public void save(Customer customer) {
        customerMap.put(customer.getUsername(), customer);
    }
}
