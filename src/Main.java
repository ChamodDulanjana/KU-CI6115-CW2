import model.Book;
import model.Student;
import service.BookService;
import service.UserService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Initialize services
        BookService bookService = new BookService();
        UserService userService = new UserService();

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== SMART LIBRARY SYSTEM =====");
            System.out.println("1. Add Book");
            System.out.println("2. List Books");
            System.out.println("3. Register Student");
            System.out.println("4. List Users");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            choice = readInt(scanner);

            switch (choice) {

                case 1:
                    addBookFlow(scanner, bookService);
                    break;

                case 2:
                    listBooksFlow(bookService);
                    break;

                case 3:
                    registerStudentFlow(scanner, userService);
                    break;

                case 4:
                    listUsersFlow(userService);
                    break;

                case 0:
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 0);

        scanner.close();
    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    // ---------------- FLOWS ----------------

    private static void addBookFlow(Scanner scanner, BookService bookService) {
        scanner.nextLine(); // consume leftover newline

        System.out.print("Enter Book ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Author: ");
        String author = scanner.nextLine();

        System.out.print("Enter Category: ");
        String category = scanner.nextLine();

        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        Book b = new Book(id, title, author, category, isbn);
        bookService.addBook(b);

        System.out.println("Book successfully added!");
    }

    private static void listBooksFlow(BookService bookService) {
        System.out.println("\n=== Available Books ===");
        bookService.listAll().forEach(System.out::println);
    }

    private static void registerStudentFlow(Scanner scanner, UserService userService) {
        scanner.nextLine(); // consume leftover newline

        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Contact Number: ");
        String contact = scanner.nextLine();

        Student s = new Student(id, name, email, contact);
        userService.registerUser(s);

        System.out.println("Student successfully registered!");
    }

    private static void listUsersFlow(UserService userService) {
        System.out.println("\n=== Registered Users ===");
        userService.listAll().forEach(System.out::println);
    }
}