public class PayPalPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Payment of Rs " + String.format("%.2f", amount) + " made using PayPal.");
    }
}
