package application.model;


// having enums inside a single class file to simplify folder structure.
 
public class Enums {
    public enum Category {
        COMPACT_PETROL, HYBRID, ELECTRIC, LUXURY_SUV
    }

    public enum AvailabilityStatus {
        AVAILABLE, RESERVED, UNDER_MAINTENANCE
    }

    public enum ReservationStatus {
        ACTIVE, CANCELLED, COMPLETED
    }
}
