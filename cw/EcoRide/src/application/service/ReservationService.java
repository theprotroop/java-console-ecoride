package application.service;

import application.model.*;
import application.model.Enums.*;
import application.repo.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Business logic for reservations and admin car operations.
 * Enforces:
 * - booking must be made at least 3 days before rentalStart
 * - cannot cancel/update after 2 days from booking creation
 * - marking car as RESERVED when booked and AVAILABLE when completed/cancelled
 */
public class ReservationService {
    private final CarRepository carRepo;
    private final ReservationRepository resRepo;

    public ReservationService(CarRepository carRepo, ReservationRepository resRepo) {
        this.carRepo = carRepo;
        this.resRepo = resRepo;
    }

    // Create a reservation and mark car reserved
    public Reservation createReservation(Customer customer, String carId, LocalDate rentalStart, int days, int estimatedKm) throws Exception {
        Car car = carRepo.getCar(carId);
        if (car == null) throw new Exception("Car not found");
        if (car.getStatus() != AvailabilityStatus.AVAILABLE) throw new Exception("Car not available");
        LocalDate today = LocalDate.now();
        long diff = ChronoUnit.DAYS.between(today, rentalStart);
        if (diff < 3) throw new Exception("Booking must be made at least 3 days before rental start");

        Reservation r = new Reservation(customer, car, today, rentalStart, days, estimatedKm);
        resRepo.add(r);
        // mark car reserved
        car.setStatus(AvailabilityStatus.RESERVED);
        carRepo.addCar(car); // update map
        return r;
    }

    // Cancel only within 2 days from booking creation
    public void cancelReservation(String reservationId) throws Exception {
        Reservation r = resRepo.get(reservationId);
        if (r == null) throw new Exception("Reservation not found");
        long daysSinceBooking = ChronoUnit.DAYS.between(r.getBookingDate(), LocalDate.now());
        if (daysSinceBooking > 2) throw new Exception("Cannot cancel after 2 days from reservation");
        r.setStatus(ReservationStatus.CANCELLED);
        r.getCar().setStatus(AvailabilityStatus.AVAILABLE);
    }

    // Update rental days only within 2 days
    public void updateReservationDays(String reservationId, int newDays) throws Exception {
        Reservation r = resRepo.get(reservationId);
        if (r == null) throw new Exception("Reservation not found");
        long daysSinceBooking = ChronoUnit.DAYS.between(r.getBookingDate(), LocalDate.now());
        if (daysSinceBooking > 2) throw new Exception("Cannot update after 2 days from reservation");
        // For simplicity, create new Reservation with same booking/rentalStart/customer/car but different days
        Reservation updated = new Reservation(r.getCustomer(), r.getCar(), r.getBookingDate(), r.getRentalStart(), newDays, r.getEstimatedTotalKm());
        resRepo.add(updated);
        r.setStatus(ReservationStatus.CANCELLED);
    }

    public Reservation findById(String id) { return resRepo.get(id); }
    public List<Reservation> findByName(String name) { return resRepo.findByName(name); }
    public List<Reservation> findByRentalDate(LocalDate d) { return resRepo.findByRentalDate(d); }

    // Admin car management (exposed to app)
    public void addCar(Car c) { carRepo.addCar(c); }
    public void updateCarStatus(String carId, AvailabilityStatus s) {
        Car c = carRepo.getCar(carId);
        if (c != null) { c.setStatus(s); carRepo.addCar(c); }
    }
    public void removeCar(String carId) { carRepo.removeCar(carId); }
}
