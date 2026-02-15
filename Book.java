public class Book {
    private int id;
    private String isbn;
    private String title;
    private String author;
    private double price;
    private String category;
    private int stock;

    // Model class: stores data for one book
    public Book(int id, String title, String author, double price) {
        this.id = id;
        this.isbn = "ISBN-" + id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.category = "Uncategorized";
        this.stock = 0;
    }

    public Book(int id, String title, String author, double price, String category) {
        this.id = id;
        this.isbn = "ISBN-" + id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.category = category;
        this.stock = 0;
    }

    public Book(int id, String title, String author, double price, String category, int stock) {
        this.id = id;
        this.isbn = "ISBN-" + id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.category = category;
        this.stock = stock;
    }

    public Book(int id, String isbn, String title, String author, double price, String category, int stock) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.price = price;
        this.category = category;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isAvailable() {
        return stock > 0;
    }

    @Override
    public String toString() {
        String availability = isAvailable() ? "In Stock (" + stock + " left)" : "Out of Stock";
        return "ID: " + id + " | ISBN: " + isbn + " | Title: " + title + " | Author: " + author + " | Category: " + category + " | Price: Rs " + String.format("%.2f", price) + " | Availability: " + availability;
    }
}
