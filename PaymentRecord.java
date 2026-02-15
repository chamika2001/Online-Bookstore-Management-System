import java.time.LocalDate;

public class PaymentRecord {
    private final String username;
    private final String method;
    private final double amount;
    private final int totalItems;
    private final String address;
    private final LocalDate paymentDate;

    public PaymentRecord(String username, String method, double amount, int totalItems, String address, LocalDate paymentDate) {
        this.username = username;
        this.method = method;
        this.amount = amount;
        this.totalItems = totalItems;
        this.address = address;
        this.paymentDate = paymentDate;
    }

    public String getUsername() {
        return username;
    }

    public String getMethod() {
        return method;
    }

    public double getAmount() {
        return amount;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }
}
