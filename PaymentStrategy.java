public interface PaymentStrategy {
    // Strategy interface: all payment types implement this behavior
    void pay(double amount);
}
