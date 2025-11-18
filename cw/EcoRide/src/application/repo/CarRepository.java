package application.repo;

import application.model.Car;
import application.model.Enums.AvailabilityStatus;
import application.model.Enums.Category;

import java.util.*;


// Simple in-memory repository for cars.

public class CarRepository {
    private final Map<String, Car> cars = new HashMap<>();

    public void addCar(Car c) { cars.put(c.getCarId(), c); }
    public Car getCar(String id) { return cars.get(id); }
    public void removeCar(String id) { cars.remove(id); }
    public List<Car> listAll() { return new ArrayList<>(cars.values()); }

    public List<Car> listAvailable() {
        List<Car> out = new ArrayList<>();
        for (Car c : cars.values()) {
            if (c.getStatus() == AvailabilityStatus.AVAILABLE) out.add(c);
        }
        return out;
    }

    // Seed sample car data
    public void seedData() {
        addCar(new Car("CAA-1001", "Toyota Aqua", Category.HYBRID, 7500, AvailabilityStatus.AVAILABLE));
        addCar(new Car("CBA-1002", "Nissan Leaf", Category.ELECTRIC, 10000, AvailabilityStatus.AVAILABLE));
        addCar(new Car("CBD-1003", "BMW X5", Category.LUXURY_SUV, 15000, AvailabilityStatus.AVAILABLE));
        addCar(new Car("CCD-1004", "Suzuki Alto", Category.COMPACT_PETROL, 5000, AvailabilityStatus.AVAILABLE));
    }
}
