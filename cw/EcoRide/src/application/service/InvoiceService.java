package application.service;

import application.model.*;
import application.model.Enums.Category;

import java.util.*;

/**
 * Invoice generation rules:
 * - free km / day, extra km charge, and tax rate vary by category (table in problem)
 * - 10% discount for rentals >= 7 days applied to base price (before tax)
 * - deposit is deducted when computing final payable
 */
public class InvoiceService {
    private static final Map<Category, Integer> FREE_KM = new HashMap<>();
    private static final Map<Category, Double> EXTRA_CHARGE = new HashMap<>();
    private static final Map<Category, Double> TAX_RATE = new HashMap<>();

    static {
        FREE_KM.put(Category.COMPACT_PETROL, 100);
        FREE_KM.put(Category.HYBRID, 150);
        FREE_KM.put(Category.ELECTRIC, 200);
        FREE_KM.put(Category.LUXURY_SUV, 250);

        EXTRA_CHARGE.put(Category.COMPACT_PETROL, 50.0);
        EXTRA_CHARGE.put(Category.HYBRID, 60.0);
        EXTRA_CHARGE.put(Category.ELECTRIC, 40.0);
        EXTRA_CHARGE.put(Category.LUXURY_SUV, 75.0);

        TAX_RATE.put(Category.COMPACT_PETROL, 0.10);
        TAX_RATE.put(Category.HYBRID, 0.12);
        TAX_RATE.put(Category.ELECTRIC, 0.08);
        TAX_RATE.put(Category.LUXURY_SUV, 0.15);
    }

    public static Invoice generateInvoice(Reservation r, int actualUsedKm) {
        Category cat = r.getCar().getCategory();
        double dailyRate = r.getCar().getDailyRate();
        int days = r.getDays();

        double basePrice = dailyRate * days; // base
        double discount = 0;
        if (days >= 7) discount = basePrice * 0.10;  // 10% before tax

        int freeTotal = FREE_KM.get(cat) * days;
        double extra = 0;
        if (actualUsedKm > freeTotal) {
            extra = (actualUsedKm - freeTotal) * EXTRA_CHARGE.get(cat);
        }

        double priceAfter = basePrice - discount + extra;
        double tax = priceAfter * TAX_RATE.get(cat);
        double deposit = Reservation.DEPOSIT;
        double finalPayable = priceAfter + tax - deposit;
        if (finalPayable < 0) finalPayable = 0;

        // create invoice id
        String invoiceId = "I" + UUID.randomUUID().toString().substring(0,8).toUpperCase();

        return new Invoice(invoiceId, r.getReservationId(), r.getCar().getCarId(),
                r.getCar().getModel(), cat.name(), dailyRate, days, actualUsedKm,
                basePrice, extra, discount, tax, deposit, finalPayable);
    }
}
