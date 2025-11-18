package application.repo;

import application.model.Reservation;

import java.time.LocalDate;
import java.util.*;


// Simple in-memory reservation store.

public class ReservationRepository {
    private final Map<String, Reservation> reservations = new HashMap<>();

    public void add(Reservation r) { reservations.put(r.getReservationId(), r); }
    public Reservation get(String id) { return reservations.get(id); }
    public Collection<Reservation> all() { return reservations.values(); }

    public List<Reservation> findByName(String name) {
        List<Reservation> out = new ArrayList<>();
        for (Reservation r : reservations.values()) {
            if (r.getCustomer().getName().toLowerCase().contains(name.toLowerCase())) out.add(r);
        }
        return out;
    }

    public List<Reservation> findByRentalDate(LocalDate date) {
        List<Reservation> out = new ArrayList<>();
        for (Reservation r : reservations.values()) {
            if (r.getRentalStart().equals(date)) out.add(r);
        }
        return out;
    }
}
