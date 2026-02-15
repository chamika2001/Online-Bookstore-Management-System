public class PaymentProcessor {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void process(double amount) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy is not selected.");
        }
        paymentStrategy.pay(amount);
    }
}
