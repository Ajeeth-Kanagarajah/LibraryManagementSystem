package lms;

public class Book extends Item {
    private String author;
    private String genre;
    private String isbn;

    public Book(int itemId, String title, String author, String genre, String isbn) {
        super(itemId, title);
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getIsbn() {
        return isbn;
    }

    @Override
    public String getType() {
        return "Book";
    }

    @Override
    public String toString() {
        return super.toString() + "\nAuthor: " + author + "\nGenre: " + genre + "\nISBN: " + isbn;
    }
}
