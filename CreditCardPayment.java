public class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Payment of Rs " + String.format("%.2f", amount) + " made using Credit Card.");
    }
}
