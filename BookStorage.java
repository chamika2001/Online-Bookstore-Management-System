import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BookStorage {
    // Dedicated storage class: only manages in-memory book data
    private final List<Book> books = new ArrayList<>();
    private static final Path BOOK_STORAGE_FILE = Paths.get("books_data.txt");

    public BookStorage() {
        loadBooksFromFile();
        if (books.isEmpty()) {
            loadDefaultBooks();
            saveBooksToFile();
        }
    }

    // Seed books are stored here (not in Main)
    private void loadDefaultBooks() {
        addIfAbsent(new Book(1001, "Sepalika", "P. B. Jayasekara", 2100.00, "Romance", 15));
        addIfAbsent(new Book(1002, "IT ENDS WITH US", "alex", 2900.00, "Romance", 12));
        addIfAbsent(new Book(1003, "Verity", "alex", 3100.00, "Action & Adventure", 8));
        addIfAbsent(new Book(1004, "Onyx Storm", "alex", 3200.00, "Action & Adventure", 6));
        addIfAbsent(new Book(1005, "Jane Eyre", "Manusha Ravishan", 2600.00, "Translations", 10));
        addIfAbsent(new Book(1006, "Mudal Soyana Mole (Psychology of Money)", "Poth Ekathuwa", 2800.00, "Social Science", 7));
        addIfAbsent(new Book(1007, "Lavender", "Poth Ekathuwa", 2300.00, "Short Stories", 9));
        addIfAbsent(new Book(1008, "Rithuu", "Poth Ekathuwa", 2000.00, "ChildrenAll", 14));
        addIfAbsent(new Book(1009, "The Best American Humorous Short Stories", "alex", 2400.00, "Short Stories", 5));
        addIfAbsent(new Book(1010, "Amusements in Mathematics", "alex", 2250.00, "Study Aids & Test Prep", 11));
        addIfAbsent(new Book(1011, "Introduction to Numerical Methods", "alex", 2550.00, "Technology & Engineering", 10));
        addIfAbsent(new Book(1012, "An Elementary Study of Chemistry", "alex", 2450.00, "Study Aids & Test Prep", 10));
        addIfAbsent(new Book(1013, "Web Design Sinhala", "Danuma Education", 2600.00, "Technology & Engineering", 12));
        addIfAbsent(new Book(1014, "PHP Sinhalen", "Danuma Education", 2500.00, "Technology & Engineering", 9));
        addIfAbsent(new Book(1015, "Ebay Drop Shipping Sinhala", "Danuma Education", 2550.00, "Teaching Methods & Materials", 7));
        addIfAbsent(new Book(1016, "California Mexican-Spanish Cook Book", "alex", 2150.00, "Travel", 13));
        addIfAbsent(new Book(1017, "Duwana Rewula", "Kids Book", 1700.00, "ChildrenAll", 20));
        addIfAbsent(new Book(1018, "Apuru Soyuriyange Apuru Rahas", "Poth Ekathuwa", 2000.00, "Action & Adventure", 4));
        addIfAbsent(new Book(1019, "Persona", "Poth Ekathuwa", 2200.00, "Art", 6));
        addIfAbsent(new Book(1020, "Sports Mindset Basics", "alex", 2100.00, "Sports & Recreation", 8));
    }

    private void addIfAbsent(Book book) {
        if (findById(book.getId()) == null && findByIsbn(book.getIsbn()) == null) {
            books.add(book);
        }
    }

    public void add(Book book) {
        books.add(book);
        saveBooksToFile();
    }

    public List<Book> getAll() {
        return books;
    }

    public Book findById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    public Book findByIsbn(String isbn) {
        if (isbn == null) {
            return null;
        }

        for (Book book : books) {
            if (book.getIsbn().equalsIgnoreCase(isbn.trim())) {
                return book;
            }
        }
        return null;
    }

    public boolean update(int id, String newTitle, String newAuthor, double newPrice) {
        return update(id, newTitle, newAuthor, newPrice, null, null);
    }

    public boolean update(int id, String newTitle, String newAuthor, double newPrice, String newCategory) {
        return update(id, newTitle, newAuthor, newPrice, newCategory, null);
    }

    public boolean update(int id, String newTitle, String newAuthor, double newPrice, String newCategory, Integer newStock) {
        Book book = findById(id);
        if (book == null) {
            return false;
        }

        book.setTitle(newTitle);
        book.setAuthor(newAuthor);
        book.setPrice(newPrice);
        if (newCategory != null && !newCategory.trim().isEmpty()) {
            book.setCategory(newCategory);
        }
        if (newStock != null && newStock >= 0) {
            book.setStock(newStock);
        }
        saveBooksToFile();
        return true;
    }

    public boolean delete(int id) {
        Book book = findById(id);
        if (book == null) {
            return false;
        }

        books.remove(book);
        saveBooksToFile();
        return true;
    }

    public List<Book> findByCategory(String category) {
        List<Book> matchedBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getCategory().equalsIgnoreCase(category)) {
                matchedBooks.add(book);
            }
        }
        return matchedBooks;
    }

    public List<String> getCategories() {
        List<String> baseCategories = new ArrayList<>();
        baseCategories.add("Translations");
        baseCategories.add("Short Stories");
        baseCategories.add("Social Science");
        baseCategories.add("Sports & Recreation");
        baseCategories.add("Study Aids & Test Prep");
        baseCategories.add("Teaching Methods & Materials");
        baseCategories.add("Technology & Engineering");
        baseCategories.add("Travel");
        baseCategories.add("ChildrenAll");
        baseCategories.add("Action & Adventure");
        baseCategories.add("Romance");
        baseCategories.add("Art");

        Set<String> uniqueCategories = new LinkedHashSet<>(baseCategories);
        for (Book book : books) {
            uniqueCategories.add(book.getCategory());
        }

        return new ArrayList<>(uniqueCategories);
    }

    public boolean reduceStock(int bookId, int quantity) {
        Book book = findById(bookId);
        if (book == null || quantity <= 0 || book.getStock() < quantity) {
            return false;
        }

        book.setStock(book.getStock() - quantity);
        saveBooksToFile();
        return true;
    }

    private void loadBooksFromFile() {
        if (!Files.exists(BOOK_STORAGE_FILE)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(BOOK_STORAGE_FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|", 7);
                if (parts.length != 7) {
                    continue;
                }

                int id = Integer.parseInt(parts[0]);
                String isbn = parts[1];
                String title = parts[2];
                String author = parts[3];
                double price = Double.parseDouble(parts[4]);
                String category = parts[5];
                int stock = Integer.parseInt(parts[6]);

                addIfAbsent(new Book(id, isbn, title, author, price, category, stock));
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Warning: Could not load books data from file.");
        }
    }

    private void saveBooksToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(BOOK_STORAGE_FILE)) {
            for (Book book : books) {
                writer.write(
                    book.getId() + "|"
                        + safe(book.getIsbn()) + "|"
                        + safe(book.getTitle()) + "|"
                        + safe(book.getAuthor()) + "|"
                        + book.getPrice() + "|"
                        + safe(book.getCategory()) + "|"
                        + book.getStock()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not save books data.");
        }
    }

    private String safe(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("|", "/");
    }
}
