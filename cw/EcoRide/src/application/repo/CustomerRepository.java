package application.repo;

import application.model.Customer;

import java.util.*;


// Simple in-memory customer repository with seed helper.

public class CustomerRepository {
    private final List<Customer> customers = new ArrayList<>();

    public void addCustomer(Customer c) { customers.add(c); }
    public List<Customer> all() { return new ArrayList<>(customers); }
    public Customer getById(String id) {
        for (Customer c : customers) if (c.getId().equalsIgnoreCase(id)) return c;
        return null;
    }
    public void deleteCustomer(String id) { customers.removeIf(c -> c.getId().equalsIgnoreCase(id)); }

    // Seed few customers
    public void seedData() {
        customers.add(new Customer("200012345V", "Nuwan Perera", "0771234567", "nuwan@gmail.com"));
        customers.add(new Customer("200065432V", "Samantha Silva", "0778765432", "samantha@yahoo.com"));
        customers.add(new Customer("N1234567", "Emily Clark", "+447890112233", "emily@outlook.com"));
    }
}
