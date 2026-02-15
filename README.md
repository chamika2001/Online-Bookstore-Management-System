# Online Bookstore Management System (Console Java)

## How to Run
1. Open terminal in this folder.
2. Compile all files:
  ```bash
  javac *.java
  ```
3. Run:
  ```bash
  java Main
  ```

Note: Use `javac *.java` (not only `javac Main.java`) to compile all dependent classes.

## Login Information
- **Admin username:** `booksuwu`
- **Admin password:** `uwu2026`
- **User accounts:** created from the startup menu (`Create User Account`)

## Application Process Flow

### Startup
1. Start app.
2. Select:
  - `1. Login`
  - `2. Create User Account`
  - `0. Exit`

### Admin Process
After admin login, available actions:
1. Add a new book (includes ISBN and stock)
2. View all books
3. Update book details (price, category, stock)
4. Delete a book
5. View today's payments report

## CRUD Operations (Books)
- **Create**
  - Admin menu -> `Add a new book`
  - Adds a book with ID, ISBN, title, author, price, category, and stock.

- **Read**
  - Admin/User menu -> `View all books`
  - Admin/User menu -> `Select category and view books`
  - Displays current availability (`In Stock` / `Out of Stock`).

- **Update**
  - Admin menu -> `Update book details`
  - Updates title, author, price, category (optional), and stock.

- **Delete**
  - Admin menu -> `Delete a book`
  - Removes a book from inventory.

### User Process
After user login, available actions:
1. View all books (with availability)
2. View categories
3. View books by category
4. Add book to cart (by ISBN)
5. View cart
6. Remove book from cart (by ISBN)
7. Checkout
  - Enter delivery address
  - Select payment method: `Credit Card` or `Cash on Delivery`
  - Stock is auto-reduced after successful purchase

## Data Persistence
- `books_data.txt` -> stores books, ISBN, category, price, stock
- `user_accounts.txt` -> stores created user accounts

Both files are loaded at startup, so data remains after restart.

## Design Patterns Used
- **Singleton Pattern**
  - `BookStore` is a singleton manager.
  - Has private constructor and static `getInstance()`.

- **Strategy Pattern**
  - `PaymentStrategy` is the strategy interface.
  - `CreditCardPayment` and `CashOnDelivery` are concrete strategies.
  - `PaymentProcessor` is the strategy context used during checkout.

## SOLID (SRP) Summary
- `Book` -> book model data
- `BookStorage` -> persistence + storage operations
- `BookStore` -> singleton access layer to storage
- `AuthService` -> registration/login logic
- `PaymentProcessor` + strategy classes -> payment behavior
- `PaymentHistory` -> payment report records
- `Main` -> menu flow and input orchestration

## Updated Text-based UML Diagram
```text
+---------------------+
|        Main         |
+---------------------+
| + main(args)        |
| + menus/workflows   |
+----------+----------+
        | uses
        v
+---------------------+         +----------------------+
|      BookStore      |<>------>|     BookStorage      |
+---------------------+         +----------------------+
| - instance          |         | - books: List<Book>  |
| - bookStorage       |         | - books_data.txt I/O |
+---------------------+         +----------------------+
| + getInstance()     |         | + add/get/update/... |
| + CRUD + findByIsbn |         | + reduceStock()      |
+----------+----------+         +----------+-----------+
        |                               |
        | manages                       | contains
        v                               v
+-----------------------------+
|            Book             |
+-----------------------------+
| id, isbn, title, author     |
| price, category, stock      |
+-----------------------------+

+---------------------+         +----------------------+
|     AuthService     |<>------>|     UserAccount      |
+---------------------+         +----------------------+
| admin creds         |         | username, password   |
| users + file I/O    |         +----------------------+
| + register/login    |
+---------------------+

+---------------------+      uses strategy      +----------------------+
|   PaymentProcessor  |------------------------->|   PaymentStrategy     |
+---------------------+                         +----------------------+
| - paymentStrategy   |                         | + pay(amount): void   |
| + setStrategy()     |                         +----------+-----------+
| + process()         |                                    ^
+---------------------+                                    |
                              +--------------+--------------+
                              |                             |
                       +--------------------+       +--------------------+
                       | CreditCardPayment  |       |   CashOnDelivery   |
                       +--------------------+       +--------------------+

+---------------------+         +----------------------+
|    PaymentHistory   |<>------>|    PaymentRecord     |
+---------------------+         +----------------------+
| records             |         | user, method, amount |
| + recordPayment()   |         | items, address, date |
| + getTodayPayments()|         +----------------------+
+---------------------+

+---------------------+
|      CartItem       |
+---------------------+
| book, quantity      |
| + getLineTotal()    |
+---------------------+
```
