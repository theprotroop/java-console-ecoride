package application.model;

import application.model.Enums.*;


// Car entity holds the vehicle data.

public class Car {
    private String carId;
    private String model;
    private Category category;
    private double dailyRate;
    private AvailabilityStatus status;

    public Car(String carId, String model, Category category, double dailyRate, AvailabilityStatus status) {
        this.carId = carId;
        this.model = model;
        this.category = category;
        this.dailyRate = dailyRate;
        this.status = status;
    }

    // Getters and setters
    public String getCarId() { return carId; }
    public String getModel() { return model; }
    public Category getCategory() { return category; }
    public double getDailyRate() { return dailyRate; }
    public AvailabilityStatus getStatus() { return status; }
    public void setStatus(AvailabilityStatus status) { this.status = status; }

    @Override
    public String toString() {
        return carId + " | " + model + " | " + category + " | LKR " + (long)dailyRate + " | " + status;
    }
}
