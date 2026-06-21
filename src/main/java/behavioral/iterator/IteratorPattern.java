// File: IteratorPattern.java
// This file demonstrates the Iterator Pattern in detail
// The user can traverse different collections of data

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * =====================================================================
 * ITERATOR PATTERN
 * =====================================================================
 * This design pattern allows sequential access to elements of a collection
 * without exposing its internal representation.
 */

// =========================================================================
// Section 1: Item Class
// =========================================================================

class Book {
    private String title;
    private String author;
    private int year;
    private String genre;

    public Book(String title, String author, int year, String genre) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.genre = genre;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public String getGenre() { return genre; }

    @Override
    public String toString() {
        return "'" + title + "' by " + author + " (" + year + ") - " + genre;
    }
}

// =========================================================================
// Section 2: Iterator Interface
// =========================================================================

interface Iterator<T> {
    boolean hasNext();
    T next();
    T current();
    void reset();
}

// =========================================================================
// Section 3: Concrete Iterators
// =========================================================================

class ForwardIterator<T> implements Iterator<T> {
    private List<T> collection;
    private int position;

    public ForwardIterator(List<T> collection) {
        this.collection = collection;
        this.position = 0;
    }

    @Override
    public boolean hasNext() {
        return position < collection.size();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements");
        }
        return collection.get(position++);
    }

    @Override
    public T current() {
        if (position == 0 || position > collection.size()) {
            throw new NoSuchElementException("No current element");
        }
        return collection.get(position - 1);
    }

    @Override
    public void reset() {
        position = 0;
        System.out.println("  [Iterator] ↩️ Reset to beginning");
    }
}

class BackwardIterator<T> implements Iterator<T> {
    private List<T> collection;
    private int position;

    public BackwardIterator(List<T> collection) {
        this.collection = collection;
        this.position = collection.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return position >= 0;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements");
        }
        return collection.get(position--);
    }

    @Override
    public T current() {
        if (position < 0 || position >= collection.size() - 1) {
            throw new NoSuchElementException("No current element");
        }
        return collection.get(position + 1);
    }

    @Override
    public void reset() {
        position = collection.size() - 1;
        System.out.println("  [Iterator] ↩️ Reset to end");
    }
}

class FilterIterator<T> implements Iterator<T> {
    private List<T> collection;
    private java.util.function.Predicate<T> filter;
    private int position;
    private List<T> filtered;

    public FilterIterator(List<T> collection, java.util.function.Predicate<T> filter) {
        this.collection = collection;
        this.filter = filter;
        this.filtered = new ArrayList<>();

        for (T item : collection) {
            if (filter.test(item)) {
                filtered.add(item);
            }
        }
        this.position = 0;
    }

    @Override
    public boolean hasNext() {
        return position < filtered.size();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more filtered elements");
        }
        return filtered.get(position++);
    }

    @Override
    public T current() {
        if (position == 0 || position > filtered.size()) {
            throw new NoSuchElementException("No current element");
        }
        return filtered.get(position - 1);
    }

    @Override
    public void reset() {
        position = 0;
        System.out.println("  [Iterator] ↩️ Reset to beginning");
    }
}

// =========================================================================
// Section 4: Aggregate Class
// =========================================================================

class BookCollection {
    private String name;
    private List<Book> books;

    public BookCollection(String name) {
        this.name = name;
        this.books = new ArrayList<>();
        initializeBooks();

        System.out.println("\n==========================================");
        System.out.println("📚 " + name + " created");
        System.out.println("==========================================");
    }

    private void initializeBooks() {
        books.add(new Book("African Heritage", "Belete Mekonnen", 2015, "History"));
        books.add(new Book("Ethiopia", "Hadis Alemayehu", 2018, "History"));
        books.add(new Book("Journey of the Heart", "Ester Wondimu", 2020, "Fiction"));
        books.add(new Book("Psychology", "Dr. Tarekegn Desalegn", 2019, "Science"));
        books.add(new Book("Love Never Dies", "Asnakech Tilahun", 2021, "Fiction"));
        books.add(new Book("Basic Computer Knowledge", "Abera Bekele", 2022, "Computer"));
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public Iterator<Book> getForwardIterator() {
        return new ForwardIterator<>(books);
    }

    public Iterator<Book> getBackwardIterator() {
        return new BackwardIterator<>(books);
    }

    public Iterator<Book> getAuthorIterator(String author) {
        return new FilterIterator<>(books, book -> book.getAuthor().contains(author));
    }

    public Iterator<Book> getGenreIterator(String genre) {
        return new FilterIterator<>(books, book -> book.getGenre().equalsIgnoreCase(genre));
    }

    public Iterator<Book> getYearIterator(int year) {
        return new FilterIterator<>(books, book -> book.getYear() == year);
    }

    public void displayAllBooks() {
        System.out.println("\n📋 Books in " + name + " (" + books.size() + "):");
        for (int i = 0; i < books.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + books.get(i));
        }
    }
}

// =========================================================================
// Section 5: Main Class - With User Input
// =========================================================================

public class IteratorPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║              ITERATOR PATTERN DEMO                     ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        BookCollection library = new BookCollection("Marta Library");

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. View all books");
            System.out.println("2. Use forward iterator");
            System.out.println("3. Use backward iterator");
            System.out.println("4. Filter by author");
            System.out.println("5. Filter by genre");
            System.out.println("6. Filter by year");
            System.out.println("7. Add new book");
            System.out.println("8. Exit");
            System.out.print("Enter your choice (1-8): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    library.displayAllBooks();
                    break;

                case 2:
                    Iterator<Book> forward = library.getForwardIterator();
                    System.out.println("\n📖 Forward Iterator:");
                    int count = 1;
                    while (forward.hasNext()) {
                        Book book = forward.next();
                        System.out.println("   " + count++ + ". " + book);
                    }
                    break;

                case 3:
                    Iterator<Book> backward = library.getBackwardIterator();
                    System.out.println("\n📖 Backward Iterator:");
                    int num = 1;
                    while (backward.hasNext()) {
                        Book book = backward.next();
                        System.out.println("   " + num++ + ". " + book);
                    }
                    break;

                case 4:
                    System.out.print("Enter author name: ");
                    String author = scanner.nextLine();

                    Iterator<Book> authorIter = library.getAuthorIterator(author);
                    System.out.println("\n📖 Books by " + author + ":");
                    boolean found = false;
                    while (authorIter.hasNext()) {
                        System.out.println("   • " + authorIter.next());
                        found = true;
                    }
                    if (!found) {
                        System.out.println("   No books found");
                    }
                    break;

                case 5:
                    System.out.print("Enter genre (History/Fiction/Science/Computer): ");
                    String genre = scanner.nextLine();

                    Iterator<Book> genreIter = library.getGenreIterator(genre);
                    System.out.println("\n📖 Books in " + genre + " genre:");
                    boolean foundGenre = false;
                    while (genreIter.hasNext()) {
                        System.out.println("   • " + genreIter.next());
                        foundGenre = true;
                    }
                    if (!foundGenre) {
                        System.out.println("   No books found");
                    }
                    break;

                case 6:
                    System.out.print("Enter year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();

                    Iterator<Book> yearIter = library.getYearIterator(year);
                    System.out.println("\n📖 Books published in " + year + ":");
                    boolean foundYear = false;
                    while (yearIter.hasNext()) {
                        System.out.println("   • " + yearIter.next());
                        foundYear = true;
                    }
                    if (!foundYear) {
                        System.out.println("   No books found");
                    }
                    break;

                case 7:
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String auth = scanner.nextLine();
                    System.out.print("Enter year: ");
                    int yr = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter genre: ");
                    String gen = scanner.nextLine();

                    Book newBook = new Book(title, auth, yr, gen);
                    library.addBook(newBook);
                    System.out.println("✅ New book added successfully!");
                    break;

                case 8:
                    continueRunning = false;
                    System.out.println("\nThank you for using the program! Goodbye.");
                    break;

                default:
                    System.out.println("Error: Please enter a valid choice (1-8)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              Program terminated! Thank you              ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}