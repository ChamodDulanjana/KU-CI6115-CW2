import model.*;
import service.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // initialize services
        NotificationService notificationService = new NotificationService();
        BookService bookService = new BookService();
        UserService userService = new UserService(notificationService);
        FineCalculator fineCalculator = new FineCalculator();
        TransactionService transactionService = new TransactionService(bookService, userService, fineCalculator, notificationService);

        Scanner scanner = new Scanner(System.in);
        int choice;
        int memberShipChoice;

        do {
            System.out.println("\n===== SMART LIBRARY SYSTEM =====");
            System.out.println("1. Add Book");
            System.out.println("2. List Books");
            System.out.println("3. Register User");
            System.out.println("4. List Users");
            System.out.println("5. Borrow Book");
            System.out.println("6. Return Book");
            System.out.println("7. Reserve Book");
            System.out.println("8. List Transactions");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            choice = readInt(scanner);

            try {
                switch (choice) {
                    case 1:
                        addBookFlow(scanner, bookService);
                        break;
                    case 2:
                        listBooksFlow(bookService);
                        break;
                    case 3:
                        System.out.println("Select membership type:");
                        System.out.println("1. Student");
                        System.out.println("2. Faculty");
                        System.out.println("3. Guest");
                        System.out.print("Enter choice: ");

                        memberShipChoice = readInt(scanner);

                        registerUserFlow(scanner, userService, memberShipChoice);
                        break;
                    case 4:
                        listUsersFlow(userService);
                        break;
                    case 5:
                        borrowFlow(scanner, transactionService);
                        break;
                    case 6:
                        returnFlow(scanner, transactionService);
                        break;
                    case 7:
                        reserveFlow(scanner, transactionService);
                        break;
                    case 8:
                        listTransactionsFlow(transactionService);
                        break;
                    case 0:
                        System.out.println("Exiting system...");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (IllegalStateException ex) {
                System.out.println("Error: " + ex.getMessage());
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

    // --- flows reused from previous Main (add/list book, register/list user) ---
    private static void addBookFlow(Scanner scanner, BookService bookService) {
        scanner.nextLine();
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
        System.out.print("Enter Edition: ");
        String edition = scanner.nextLine();
        System.out.print("Enter Summery: ");
        String summery = scanner.nextLine();
        System.out.print("Enter Tag: ");
        String tag = scanner.nextLine();

        Book b = new BookBuilder(id, title, author, category, isbn)
                .edition(edition)
                .summary(summery)
                .addTag(tag)
                .build();
        bookService.addBook(b);
    }

    private static void listBooksFlow(BookService bookService) {
        System.out.println("\n=== Books ===");
        bookService.listAll().forEach(System.out::println);
    }

    private static void registerUserFlow(Scanner scanner, UserService userService, int memberShipChoice) {
        scanner.nextLine();
        System.out.print("Enter User ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter User Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Contact Number: ");
        String contact = scanner.nextLine();

        User user = switch (memberShipChoice) {
            case 1 -> new Student(id, name, email, contact);
            case 2 -> new Faculty(id, name, email, contact);
            case 3 -> new Guest(id, name, email, contact);
            default -> throw new IllegalStateException("Invalid membership type");
        };

        userService.registerUser(user);
    }

    private static void listUsersFlow(UserService userService) {
        System.out.println("\n=== Users ===");
        userService.listAll().forEach(System.out::println);
    }

    // --- new flows: borrow / return / reserve ---

    private static void borrowFlow(Scanner scanner, TransactionService transactionService) {
        scanner.nextLine();
        System.out.print("Enter User ID: ");
        String uid = scanner.nextLine();
        System.out.print("Enter Book ID: ");
        String bid = scanner.nextLine();
        BorrowTransaction tx = transactionService.borrowBook(uid, bid);
        System.out.println("Transaction created: " + tx);
    }

    private static void returnFlow(Scanner scanner, TransactionService transactionService) {
        scanner.nextLine();
        System.out.print("Enter User ID: ");
        String uid = scanner.nextLine();
        System.out.print("Enter Book ID: ");
        String bid = scanner.nextLine();
        transactionService.returnBook(uid, bid);
    }

    private static void reserveFlow(Scanner scanner, TransactionService transactionService) {
        scanner.nextLine();
        System.out.print("Enter User ID: ");
        String uid = scanner.nextLine();
        System.out.print("Enter Book ID: ");
        String bid = scanner.nextLine();
        transactionService.reserveBook(uid, bid);
    }

    private static void listTransactionsFlow(TransactionService transactionService) {
        System.out.println("\n=== Transactions ===");
        transactionService.listAllTransactions().forEach(System.out::println);
    }
}