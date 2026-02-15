import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentHistory {
    private static PaymentHistory instance;
    private final List<PaymentRecord> records;

    private PaymentHistory() {
        records = new ArrayList<>();
    }

    public static PaymentHistory getInstance() {
        if (instance == null) {
            instance = new PaymentHistory();
        }
        return instance;
    }

    public void recordPayment(String username, String method, double amount, int totalItems, String address) {
        records.add(new PaymentRecord(username, method, amount, totalItems, address, LocalDate.now()));
    }

    public List<PaymentRecord> getTodayPayments() {
        LocalDate today = LocalDate.now();
        List<PaymentRecord> todayRecords = new ArrayList<>();
        for (PaymentRecord record : records) {
            if (record.getPaymentDate().equals(today)) {
                todayRecords.add(record);
            }
        }
        return todayRecords;
    }
}
