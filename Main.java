import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner;
    private static final BookStore bookStore = BookStore.getInstance();
    private static final AuthService authService = new AuthService();
    private static final PaymentHistory paymentHistory = PaymentHistory.getInstance();
    private static final PaymentProcessor paymentProcessor = new PaymentProcessor();

    public static void main(String[] args) {
        try (Scanner inputScanner = new Scanner(System.in)) {
            scanner = inputScanner;
            boolean running = true;

            while (running) {
                showStartupMenu();
                int choice = readInt("Enter your choice: ");

                switch (choice) {
                    case 1 -> login();
                    case 2 -> registerUserAccount();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }

            System.out.println("Exiting Online Bookstore Management System. Goodbye!");
        }
    }

    private static void showStartupMenu() {
        System.out.println("\n===== Online Bookstore Management System =====");
        System.out.println("1. Login");
        System.out.println("2. Create User Account");
        System.out.println("0. Exit");
    }

    private static void registerUserAccount() {
        System.out.println("\n--- Create User Account ---");
        String username = readLine("Create username: ");
        String password = readLine("Create password: ");

        boolean registered = authService.registerUser(username, password);
        if (registered) {
            System.out.println("User account created successfully.");
        } else {
            System.out.println("Registration failed. Username may already exist or fields are empty.");
        }
    }

    private static void login() {
        System.out.println("\n--- Login ---");
        String username = readLine("Username: ");
        String password = readLine("Password: ");

        String role = authService.login(username, password);

        switch (role) {
            case "ADMIN" -> {
                System.out.println("Admin login successful.");
                runAdminMenu();
            }
            case "USER" -> {
                System.out.println("User login successful.");
                runUserMenu(username);
            }
            default -> System.out.println("Invalid credentials.");
        }
    }

    private static void runAdminMenu() {
        boolean inAdminMenu = true;
        while (inAdminMenu) {
            showAdminMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> addBook();
                case 2 -> viewBooks();
                case 3 -> updateBook();
                case 4 -> deleteBook();
                case 5 -> viewTodayPayments();
                case 0 -> inAdminMenu = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void runUserMenu(String username) {
        List<CartItem> cart = new ArrayList<>();
        boolean inUserMenu = true;
        while (inUserMenu) {
            showUserMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> viewBooks();
                case 2 -> viewCategories();
                case 3 -> viewBooksByCategory();
                case 4 -> addBookToCart(cart);
                case 5 -> viewCart(cart);
                case 6 -> removeBookFromCart(cart);
                case 7 -> checkoutCart(username, cart);
                case 0 -> inUserMenu = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showAdminMenu() {
        System.out.println("\n===== Admin Menu =====");
        System.out.println("1. Add a new book");
        System.out.println("2. View all books");
        System.out.println("3. Update book details");
        System.out.println("4. Delete a book");
        System.out.println("5. View today's payments report");
        System.out.println("0. Logout");
    }

    private static void showUserMenu() {
        System.out.println("\n===== User Menu =====");
        System.out.println("1. View all books");
        System.out.println("2. View all categories");
        System.out.println("3. Select category and view books");
        System.out.println("4. Add book to cart");
        System.out.println("5. View cart");
        System.out.println("6. Remove book from cart");
        System.out.println("7. Checkout (Card / Cash on Delivery)");
        System.out.println("0. Logout");
    }

    private static void addBook() {
        System.out.println("\n--- Add Book ---");
        int id = readInt("Enter book ID: ");

        if (bookStore.findBookById(id) != null) {
            System.out.println("Book with this ID already exists.");
            return;
        }

        String isbn = readLine("Enter ISBN number: ");
        if (isbn.trim().isEmpty()) {
            System.out.println("ISBN cannot be empty.");
            return;
        }

        if (bookStore.findBookByIsbn(isbn) != null) {
            System.out.println("Book with this ISBN already exists.");
            return;
        }

        String title = readLine("Enter title: ");
        String author = readLine("Enter author: ");
        double price = readDouble("Enter price: ");
        int stock = readInt("Enter stock quantity: ");
        String category = selectCategory();

        if (stock < 0) {
            System.out.println("Stock cannot be negative.");
            return;
        }

        Book book = new Book(id, isbn.trim(), title, author, price, category, stock);
        bookStore.addBook(book);
        System.out.println("Book added successfully.");
    }

    private static void viewBooks() {
        System.out.println("\n--- View All Books ---");
        List<Book> books = bookStore.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        for (Book book : books) {
            System.out.println(book);
        }
    }

    private static void updateBook() {
        System.out.println("\n--- Update Book ---");
        int id = readInt("Enter book ID to update: ");

        Book existing = bookStore.findBookById(id);
        if (existing == null) {
            System.out.println("Book not found.");
            return;
        }

        String newTitle = readLine("Enter new title: ");
        String newAuthor = readLine("Enter new author: ");
        double newPrice = readDouble("Enter new price: ");
        int newStock = readInt("Enter new stock quantity: ");

        if (newStock < 0) {
            System.out.println("Stock cannot be negative.");
            return;
        }

        System.out.println("Do you want to change category? (y/n)");
        String changeCategory = readLine("Enter choice: ");
        boolean updated;

        switch (changeCategory.trim().toLowerCase()) {
            case "y" -> {
                String newCategory = selectCategory();
                updated = bookStore.updateBook(id, newTitle, newAuthor, newPrice, newCategory, newStock);
            }
            default -> {
                String currentCategory = existing.getCategory();
                updated = bookStore.updateBook(id, newTitle, newAuthor, newPrice, currentCategory, newStock);
            }
        }

        System.out.println(updated ? "Book updated successfully." : "Update failed.");
    }

    private static void deleteBook() {
        System.out.println("\n--- Delete Book ---");
        int id = readInt("Enter book ID to delete: ");

        boolean deleted = bookStore.deleteBook(id);
        System.out.println(deleted ? "Book deleted successfully." : "Book not found.");
    }

    private static void viewCategories() {
        System.out.println("\n--- Available Categories ---");
        List<String> categories = bookStore.getCategories();
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }
    }

    private static void addBookToCart(List<CartItem> cart) {
        System.out.println("\n--- Add Book to Cart ---");
        List<Book> books = bookStore.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        System.out.println("Select a book:");
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            String availability = book.isAvailable() ? "In Stock (" + book.getStock() + ")" : "Out of Stock";
            System.out.println((i + 1) + ". " + book.getTitle() + " | ISBN: " + book.getIsbn() + " - Rs " + String.format("%.2f", book.getPrice()) + " - " + availability);
        }

        String isbn = readLine("Enter ISBN to add to cart: ");
        Book book = bookStore.findBookByIsbn(isbn);
        if (book == null) {
            System.out.println("Book not found for this ISBN.");
            return;
        }
        int quantity = readInt("Enter quantity: ");

        if (quantity <= 0) {
            System.out.println("Quantity must be at least 1.");
            return;
        }

        if (book.getStock() <= 0) {
            System.out.println("This book is out of stock.");
            return;
        }

        CartItem existingItem = findCartItem(cart, book.getIsbn());
        int currentQtyInCart = existingItem == null ? 0 : existingItem.getQuantity();

        if (currentQtyInCart + quantity > book.getStock()) {
            System.out.println("Not enough stock. Available: " + book.getStock());
            return;
        }

        if (existingItem == null) {
            cart.add(new CartItem(book, quantity));
        } else {
            existingItem.setQuantity(currentQtyInCart + quantity);
        }

        System.out.println("Book added to cart.");
    }

    private static void viewCart(List<CartItem> cart) {
        System.out.println("\n--- Your Cart ---");
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        double total = 0.0;
        for (CartItem item : cart) {
            double lineTotal = item.getLineTotal();
            total += lineTotal;
            System.out.println(
                "ID: " + item.getBook().getId()
                    + " | Title: " + item.getBook().getTitle()
                    + " | Qty: " + item.getQuantity()
                    + " | Line Total: Rs " + String.format("%.2f", lineTotal)
            );
        }

        System.out.println("Cart Total: Rs " + String.format("%.2f", total));
    }

    private static void removeBookFromCart(List<CartItem> cart) {
        System.out.println("\n--- Remove Book from Cart ---");
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        String isbn = readLine("Enter ISBN to remove: ");
        CartItem item = findCartItem(cart, isbn);

        if (item == null) {
            System.out.println("Book not found in cart.");
            return;
        }

        cart.remove(item);
        System.out.println("Book removed from cart.");
    }

    private static CartItem findCartItem(List<CartItem> cart, String isbn) {
        for (CartItem item : cart) {
            if (item.getBook().getIsbn().equalsIgnoreCase(isbn)) {
                return item;
            }
        }
        return null;
    }

    private static void checkoutCart(String username, List<CartItem> cart) {
        System.out.println("\n--- Checkout ---");
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        for (CartItem item : cart) {
            Book currentBook = bookStore.findBookById(item.getBook().getId());
            if (currentBook == null || currentBook.getStock() < item.getQuantity()) {
                System.out.println("Out of stock or insufficient stock for: " + item.getBook().getTitle());
                return;
            }
        }

        viewCart(cart);
        String address = readLine("Enter delivery address: ");

        if (address.trim().isEmpty()) {
            System.out.println("Address is required for delivery.");
            return;
        }

        System.out.println("Select payment method:");
        System.out.println("1. Credit Card");
        System.out.println("2. Cash on Delivery");
        int methodChoice = readInt("Enter payment choice: ");

        PaymentStrategy paymentStrategy;
        String methodName;
        switch (methodChoice) {
            case 1 -> {
                paymentStrategy = new CreditCardPayment();
                methodName = "Credit Card";
            }
            case 2 -> {
                paymentStrategy = new CashOnDelivery();
                methodName = "Cash on Delivery";
            }
            default -> {
                System.out.println("Invalid payment method.");
                return;
            }
        }

        double totalAmount = 0.0;
        int totalItems = 0;
        for (CartItem item : cart) {
            totalAmount += item.getLineTotal();
            totalItems += item.getQuantity();
        }

        for (CartItem item : cart) {
            boolean reduced = bookStore.reduceBookStock(item.getBook().getId(), item.getQuantity());
            if (!reduced) {
                System.out.println("Checkout failed due to stock changes. Please try again.");
                return;
            }
        }

        paymentProcessor.setPaymentStrategy(paymentStrategy);
        paymentProcessor.process(totalAmount);
        paymentHistory.recordPayment(username, methodName, totalAmount, totalItems, address);
        cart.clear();
        System.out.println("Order placed successfully. Delivery to: " + address);
    }

    private static void viewTodayPayments() {
        System.out.println("\n--- Today's Payments Report ---");
        List<PaymentRecord> todayPayments = paymentHistory.getTodayPayments();

        if (todayPayments.isEmpty()) {
            System.out.println("No payments recorded today.");
            return;
        }

        int orderCount = todayPayments.size();
        int totalBooksBought = 0;
        int cardCount = 0;
        int codCount = 0;
        double totalRevenue = 0.0;

        for (PaymentRecord record : todayPayments) {
            totalBooksBought += record.getTotalItems();
            totalRevenue += record.getAmount();

            if ("Credit Card".equals(record.getMethod())) {
                cardCount++;
            } else if ("Cash on Delivery".equals(record.getMethod())) {
                codCount++;
            }

            System.out.println(
                "User: " + record.getUsername()
                    + " | Method: " + record.getMethod()
                    + " | Amount: Rs " + String.format("%.2f", record.getAmount())
                    + " | Books Bought: " + record.getTotalItems()
                    + " | Address: " + record.getAddress()
            );
        }

        System.out.println("\nSummary:");
        System.out.println("Total Orders: " + orderCount);
        System.out.println("Total Books Bought: " + totalBooksBought);
        System.out.println("Credit Card Orders: " + cardCount);
        System.out.println("Cash on Delivery Orders: " + codCount);
        System.out.println("Total Revenue: Rs " + String.format("%.2f", totalRevenue));
    }

    private static void viewBooksByCategory() {
        System.out.println("\n--- Select Category ---");
        String category = selectCategory();

        List<Book> books = bookStore.getBooksByCategory(category);
        if (books.isEmpty()) {
            System.out.println("No books found for category: " + category);
            return;
        }

        System.out.println("\nBooks in category: " + category);
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private static String selectCategory() {
        List<String> categories = bookStore.getCategories();
        System.out.println("Select category:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }

        int categoryChoice = readInt("Enter category number: ");
        if (categoryChoice < 1 || categoryChoice > categories.size()) {
            System.out.println("Invalid category. Using 'ChildrenAll' by default.");
            return "ChildrenAll";
        }

        return categories.get(categoryChoice - 1);
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String inputValue = scanner.nextLine();
            try {
                return Integer.parseInt(inputValue);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String message) {
        while (true) {
            System.out.print(message);
            String inputValue = scanner.nextLine();
            try {
                return Double.parseDouble(inputValue);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
}
