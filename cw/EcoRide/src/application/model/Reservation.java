package application.model;

import java.time.LocalDate;
import java.util.UUID;
import application.model.Enums.ReservationStatus;


// Reservation entity contains booking.
// DEPOSIT is the refundable deposit charged on booking.

public class Reservation {
    public static final double DEPOSIT = 5000.0;

    private String reservationId;
    private Customer customer;
    private Car car;
    private LocalDate bookingDate;   // date reservation created
    private LocalDate rentalStart;   // date rental starts
    private int days;
    private int estimatedTotalKm;    // supplied at booking
    private ReservationStatus status;

    public Reservation(Customer customer, Car car, LocalDate bookingDate, LocalDate rentalStart, int days, int estimatedTotalKm) {
        this.reservationId = "R" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        this.customer = customer;
        this.car = car;
        this.bookingDate = bookingDate;
        this.rentalStart = rentalStart;
        this.days = days;
        this.estimatedTotalKm = estimatedTotalKm;
        this.status = ReservationStatus.ACTIVE;
    }

    // Getters and setters
    public String getReservationId() { return reservationId; }
    public Customer getCustomer() { return customer; }
    public Car getCar() { return car; }
    public LocalDate getBookingDate() { return bookingDate; }
    public LocalDate getRentalStart() { return rentalStart; }
    public int getDays() { return days; }
    public int getEstimatedTotalKm() { return estimatedTotalKm; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
}
