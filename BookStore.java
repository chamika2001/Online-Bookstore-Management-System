import java.util.List;

public class BookStore {
    // Singleton instance (created once)
    private static BookStore instance;

    // Storage dependency for book collection management
    private final BookStorage bookStorage;

    // Private constructor prevents direct object creation from outside
    private BookStore() {
        bookStorage = new BookStorage();
    }

    // Global access point to the single instance
    public static BookStore getInstance() {
        if (instance == null) {
            instance = new BookStore();
        }
        return instance;
    }

    // CREATE
    public void addBook(Book book) {
        bookStorage.add(book);
    }

    // READ
    public List<Book> getAllBooks() {
        return bookStorage.getAll();
    }

    public Book findBookById(int id) {
        return bookStorage.findById(id);
    }

    public Book findBookByIsbn(String isbn) {
        return bookStorage.findByIsbn(isbn);
    }

    // UPDATE
    public boolean updateBook(int id, String newTitle, String newAuthor, double newPrice) {
        return bookStorage.update(id, newTitle, newAuthor, newPrice);
    }

    public boolean updateBook(int id, String newTitle, String newAuthor, double newPrice, String newCategory) {
        return bookStorage.update(id, newTitle, newAuthor, newPrice, newCategory);
    }

    public boolean updateBook(int id, String newTitle, String newAuthor, double newPrice, String newCategory, int newStock) {
        return bookStorage.update(id, newTitle, newAuthor, newPrice, newCategory, newStock);
    }

    // DELETE
    public boolean deleteBook(int id) {
        return bookStorage.delete(id);
    }

    public List<Book> getBooksByCategory(String category) {
        return bookStorage.findByCategory(category);
    }

    public List<String> getCategories() {
        return bookStorage.getCategories();
    }

    public boolean reduceBookStock(int bookId, int quantity) {
        return bookStorage.reduceStock(bookId, quantity);
    }
}
