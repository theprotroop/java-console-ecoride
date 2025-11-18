package application.model;

import java.text.DecimalFormat;


// Simply keep data for invoice breakdown and printable text.

public class Invoice {
    private String invoiceId;
    private String reservationId;
    private String carId;
    private String carModel;
    private String category;
    private double dailyRate;
    private int days;
    private int usedKm;
    private double basePrice;
    private double extraKmCharge;
    private double discount;
    private double tax;
    private double deposit;
    private double finalPayable;

    public Invoice(String invoiceId, String reservationId, String carId, String carModel, String category,
                   double dailyRate, int days, int usedKm,
                   double basePrice, double extraKmCharge, double discount, double tax, double deposit, double finalPayable) {
        this.invoiceId = invoiceId;
        this.reservationId = reservationId;
        this.carId = carId;
        this.carModel = carModel;
        this.category = category;
        this.dailyRate = dailyRate;
        this.days = days;
        this.usedKm = usedKm;
        this.basePrice = basePrice;
        this.extraKmCharge = extraKmCharge;
        this.discount = discount;
        this.tax = tax;
        this.deposit = deposit;
        this.finalPayable = finalPayable;
    }

    public String asText() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        StringBuilder sb = new StringBuilder();
        sb.append("----- EcoRide Invoice -----\n");
        sb.append("Invoice ID: ").append(invoiceId).append("\n");
        sb.append("Reservation: ").append(reservationId).append("\n");
        sb.append("Car: ").append(carId).append(" - ").append(carModel).append(" (").append(category).append(")\n");
        sb.append("Daily rate: LKR ").append(df.format(dailyRate)).append("\n");
        sb.append("Days: ").append(days).append("\n");
        sb.append("Base price: LKR ").append(df.format(basePrice)).append("\n");
        sb.append("Kilometres used: ").append(usedKm).append("\n");
        sb.append("Extra km charges: LKR ").append(df.format(extraKmCharge)).append("\n");
        sb.append("Discount: LKR -").append(df.format(discount)).append("\n");
        sb.append("Tax: LKR ").append(df.format(tax)).append("\n");
        sb.append("Deposit deducted: LKR ").append(df.format(deposit)).append("\n");
        sb.append("Final payable: LKR ").append(df.format(finalPayable)).append("\n");
        sb.append("---------------------------\n");
        return sb.toString();
    }
}
