# Online Bookstore Management System (Console - Java)

## How to run
1. Open terminal in this folder.
2. Compile:
   ```bash
   javac *.java
   ```
3. Run:
   ```bash
   java Main
   ```

## Design Patterns Used
- **Singleton Pattern**: `BookStore` has a private constructor and a static `getInstance()` method.
  This ensures one shared book manager instance for the whole application.
- **Strategy Pattern**: `PaymentStrategy` interface defines `pay(amount)`.
  `CreditCardPayment`, `PayPalPayment`, and `CashOnDelivery` each provide a different payment behavior.

## SOLID (Single Responsibility Principle)
- `Book`: book data only.
- `BookStore`: CRUD and collection management only.
- Payment classes: payment behavior only.
- `Main`: user input, menu, and application flow only.

## Text-based UML Class Diagram
```text
+-------------------+
|       Book        |
+-------------------+
| - id: int         |
| - title: String   |
| - author: String  |
| - price: double   |
+-------------------+
| + getters/setters |
| + toString()      |
+-------------------+

+-----------------------------+
|          BookStore          |
+-----------------------------+
| - instance: BookStore       |
| - books: List<Book>         |
+-----------------------------+
| - BookStore()               |
| + getInstance(): BookStore  |
| + addBook(book): void       |
| + getAllBooks(): List<Book> |
| + findBookById(id): Book    |
| + updateBook(...): boolean  |
| + deleteBook(id): boolean   |
+-----------------------------+

          uses
Main -----------------------> BookStore
Main -----------------------> Book
Main -----------------------> PaymentStrategy

+-------------------------------+
|       PaymentStrategy         |
+-------------------------------+
| + pay(amount: double): void   |
+-------------------------------+
              ^
              |
  +------------------------+  +------------------+  +---------------------+
  |   CreditCardPayment    |  |  PayPalPayment   |  |   CashOnDelivery    |
  +------------------------+  +------------------+  +---------------------+
  | + pay(amount): void    |  | + pay(amount)    |  | + pay(amount): void |
  +------------------------+  +------------------+  +---------------------+
```
