package application.app;

import application.model.*;
import application.model.Enums.AvailabilityStatus;
import application.model.Enums.Category;
import application.repo.*;
import application.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;


// Main CLI runner.
// Provides menus for customers, admin and invoice generation.

public class app {
    static CarRepository carRepo = new CarRepository();
    static ReservationRepository resRepo = new ReservationRepository();
    static CustomerRepository custRepo = new CustomerRepository();
    static ReservationService rs = new ReservationService(carRepo, resRepo);
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // seed cars and customers
        carRepo.seedData();
        custRepo.seedData();
        mainMenu();
    }

    static void mainMenu() {
        while (true) {
            System.out.println("\n--- EcoRide CLI ---");
            System.out.println("1. List cars");
            System.out.println("2. Customer: Register & Book");
            System.out.println("3. Search reservation (by id / name)");
            System.out.println("4. View bookings by rental date");
            System.out.println("5. Admin menu");
            System.out.println("6. Generate invoice for a reservation (enter actual used km)");
            System.out.println("7. Exit");
            System.out.print("Choose: ");
            String opt = sc.nextLine().trim();
            try {
                switch (opt) {
                    case "1" -> listCars();
                    case "2" -> customerBook();
                    case "3" -> searchReservation();
                    case "4" -> viewByRentalDate();
                    case "5" -> adminMenu();
                    case "6" -> generateInvoiceFlow();
                    case "7" -> { System.out.println("Bye."); return; }
                    default -> System.out.println("Invalid.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    static void listCars() {
        System.out.println("\nCars:");
        for (Car c : carRepo.listAll()) System.out.println(c);
    }

    // Uses existing customer for booking if found, otherwise it creates new customer.
    static void customerBook() throws Exception {
        System.out.println("\n-- Booking --");
        System.out.print("Choose car id: ");
        String carId = sc.nextLine().trim();
        Car car = carRepo.getCar(carId);
        if (car == null) { System.out.println("No such car."); return; }
        if (car.getStatus() != AvailabilityStatus.AVAILABLE) { System.out.println("Car not available."); return; }

        // ask for NIC/Passport and check for existing customer
        System.out.print("NIC/Passport: ");
        String id = sc.nextLine();
        Customer cust = custRepo.getById(id);
        if (cust == null) {
            System.out.println("New customer. Enter details:");
            System.out.print("Name: "); String name = sc.nextLine();
            System.out.print("Contact: "); String contact= sc.nextLine();
            System.out.print("Email: "); String email = sc.nextLine();
            cust = new Customer(id, name, contact, email);
            custRepo.addCustomer(cust);
            System.out.println("Customer registered.");
        } else {
            System.out.println("Existing customer found: " + cust.getName());
        }

        System.out.print("Rental start (YYYY-MM-DD): ");
        LocalDate start = LocalDate.parse(sc.nextLine());
        System.out.print("Days: ");
        int days = Integer.parseInt(sc.nextLine());
        System.out.print("Estimated total km: ");
        int km = Integer.parseInt(sc.nextLine());

        Reservation r = rs.createReservation(cust, carId, start, days, km);

        System.out.println("Reserved. ID: " + r.getReservationId());
        System.out.println("Deposit LKR " + (long) Reservation.DEPOSIT + " charged (refundable).");
    }

    static void searchReservation() {
        System.out.print("Enter reservation ID or customer name: ");
        String q = sc.nextLine().trim();
        Reservation r = rs.findById(q);
        if (r != null) {
            printReservation(r);
            return;
        }
        List<Reservation> list = rs.findByName(q);
        if (list.isEmpty()) System.out.println("No reservations found.");
        else list.forEach(app::printReservation);
    }

    static void printReservation(Reservation r) {
        System.out.println("Reservation: " + r.getReservationId() + " | Customer: " + r.getCustomer().getName()
                + " | Car: " + r.getCar().getModel() + " | Start: " + r.getRentalStart() + " | Days: " + r.getDays()
                + " | Status: " + r.getStatus());
    }

    static void viewByRentalDate() {
        System.out.print("Enter rental date (YYYY-MM-DD): ");
        LocalDate d = LocalDate.parse(sc.nextLine());
        List<Reservation> list = rs.findByRentalDate(d);
        if (list.isEmpty()) System.out.println("No bookings on that date.");
        else list.forEach(app::printReservation);
    }

    static void adminMenu() {
        System.out.println("\n-- Admin --");
        System.out.println("1. Add car");
        System.out.println("2. Update car status");
        System.out.println("3. Remove car");
        System.out.println("4. List all reservations");
        System.out.println("5. Manage customers");
        System.out.print("Choose: ");
        String a = sc.nextLine().trim();
        try {
            switch (a) {
                case "1" -> {
                    System.out.print("Car id: "); String id = sc.nextLine();
                    System.out.print("Model: "); String model = sc.nextLine();
                    System.out.print("Category (COMPACT_PETROL,HYBRID,ELECTRIC,LUXURY_SUV): ");
                    Category cat = Category.valueOf(sc.nextLine().trim());
                    System.out.print("Daily rate: "); double rate = Double.parseDouble(sc.nextLine());
                    carRepo.addCar(new Car(id, model, cat, rate, AvailabilityStatus.AVAILABLE));
                    System.out.println("Vehicle added successfully");
                }
                case "2" -> {
                    System.out.print("Car id: "); String id = sc.nextLine();
                    System.out.print("Status (AVAILABLE,RESERVED,UNDER_MAINTENANCE): ");
                    AvailabilityStatus s = AvailabilityStatus.valueOf(sc.nextLine().trim());
                    rs.updateCarStatus(id, s);
                    System.out.println("Updated.");
                }
                case "3" -> {
                    System.out.print("Car id: "); String id = sc.nextLine();
                    rs.removeCar(id);
                    System.out.println("Removed (if existed).");
                }
                case "4" -> {
                    for (Reservation r : resRepo.all()) printReservation(r);
                }
                case "5" -> manageCustomers();
                default -> System.out.println("Invalid.");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    static void manageCustomers() {
        while (true) {
            System.out.println("\n-- Customer Management --");
            System.out.println("1. Add Customer");
            System.out.println("2. View All Customers");
            System.out.println("3. Delete Customer");
            System.out.println("4. Back");
            System.out.print("Choose: ");
            String opt = sc.nextLine().trim();

            switch (opt) {
                case "1" -> {
                    System.out.print("NIC/Passport: "); String id = sc.nextLine();
                    System.out.print("Name: "); String name = sc.nextLine();
                    System.out.print("Contact: "); String contact = sc.nextLine();
                    System.out.print("Email: "); String email = sc.nextLine();
                    custRepo.addCustomer(new Customer(id, name, contact, email));
                    System.out.println("Customer added.");
                }
                case "2" -> {
                    List<Customer> list = custRepo.all();
                    if (list.isEmpty()) System.out.println("No customers.");
                    else list.forEach(c -> System.out.println(c.toString()));
                }
                case "3" -> {
                    System.out.print("Enter customer ID to delete: ");
                    String id = sc.nextLine();
                    custRepo.deleteCustomer(id);
                    System.out.println("Deleted if existed.");
                }
                case "4" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void generateInvoiceFlow() {
        System.out.print("Enter reservation id: ");
        String id = sc.nextLine().trim();
        Reservation r = rs.findById(id);
        if (r == null) { System.out.println("Not found."); return; }
        System.out.print("Enter actual used kilometers: ");
        int km = Integer.parseInt(sc.nextLine());
        Invoice inv = InvoiceService.generateInvoice(r, km);
        System.out.println(inv.asText());
        // Mark reservation completed and car available
        r.setStatus(application.model.Enums.ReservationStatus.COMPLETED);
        r.getCar().setStatus(AvailabilityStatus.AVAILABLE);
    }
}
