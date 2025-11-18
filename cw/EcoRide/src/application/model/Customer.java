package application.model;


// Customer entity has NIC/Passport, name and contact details.

public class Customer {
    private String id;       // NIC or passport
    private String name;
    private String contact;
    private String email;

    public Customer(String id, String name, String contact, String email) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return id + " | " + name + " | " + contact + " | " + email;
    }
}
