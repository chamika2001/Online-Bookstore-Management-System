public class CashOnDelivery implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Order placed with Cash on Delivery for Rs " + String.format("%.2f", amount) + ".");
    }
}
