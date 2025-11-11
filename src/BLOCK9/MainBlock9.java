package BLOCK9;

import java.util.Scanner;

import java.util.List;
import java.util.UUID;

public class MainBlock9 {
    private Library library;
    private Scanner scanner;

    public MainBlock9() {
        this.library = new Library(5, 10.0); // max 5 books, 10 rub fine per day
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== СИСТЕМА УПРАВЛЕНИЯ БИБЛИОТЕКОЙ ===");

        while (true) {
            showMainMenu();
            int choice = getIntInput("Выберите опцию: ");

            switch (choice) {
                case 1 -> manageBooks();
                case 2 -> manageMembers();
                case 3 -> manageLoans();
                case 4 -> showStatistics();
                case 5 -> systemOperations();
                case 0 -> {
                    System.out.println("Выход из программы...");
                    return;
                }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n=== ГЛАВНОЕ МЕНЮ ===");
        System.out.println("1. Управление книгами");
        System.out.println("2. Управление членами");
        System.out.println("3. Управление займами");
        System.out.println("4. Статистика и отчеты");
        System.out.println("5. Системные операции");
        System.out.println("0. Выход");
    }

    private void manageBooks() {
        while (true) {
            System.out.println("\n=== УПРАВЛЕНИЕ КНИГАМИ ===");
            System.out.println("1. Добавить книгу");
            System.out.println("2. Показать все книги");
            System.out.println("3. Найти книгу");
            System.out.println("4. Обновить книгу");
            System.out.println("5. Удалить книгу");
            System.out.println("0. Назад");

            int choice = getIntInput("Выберите опцию: ");

            switch (choice) {
                case 1 -> addBook();
                case 2 -> showAllBooks();
                case 3 -> searchBooks();
                case 4 -> updateBook();
                case 5 -> deleteBook();
                case 0 -> { return; }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private void addBook() {
        System.out.println("\n--- Добавление книги ---");
        String id = UUID.randomUUID().toString().substring(0, 8);
        String title = getStringInput("Введите название: ");
        String author = getStringInput("Введите автора: ");
        String isbn = getStringInput("Введите ISBN: ");
        String genre = getStringInput("Введите жанр: ");
        int year = getIntInput("Введите год издания: ");

        Book book = new Book(id, title, author, isbn, genre, year);
        if (library.addBook(book)) {
            System.out.println("Книга успешно добавлена! ID: " + id);
        } else {
            System.out.println("Ошибка при добавлении книги");
        }
    }

    private void showAllBooks() {
        System.out.println("\n--- Все книги ---");
        List<Book> books = library.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("Книги не найдены");
        } else {
            books.forEach(book -> {
                String status = book.isAvailable() ? "Доступна" : "Выдана";
                System.out.printf("%s: '%s' - %s (%s) [%s]%n",
                        book.getId(), book.getTitle(), book.getAuthor(), book.getGenre(), status);
            });
        }
    }

    private void searchBooks() {
        String query = getStringInput("Введите поисковый запрос: ");
        List<Book> results = library.searchBooks(query);

        if (results.isEmpty()) {
            System.out.println("Книги не найдены");
        } else {
            System.out.println("Найдено книг: " + results.size());
            results.forEach(System.out::println);
        }
    }

    private void updateBook() {
        String id = getStringInput("Введите ID книги для обновления: ");
        Book book = library.getBook(id);

        if (book == null) {
            System.out.println("Книга не найдена");
            return;
        }

        System.out.println("Текущие данные: " + book);
        String title = getStringInput("Новое название [" + book.getTitle() + "]: ");
        String author = getStringInput("Новый автор [" + book.getAuthor() + "]: ");
        String genre = getStringInput("Новый жанр [" + book.getGenre() + "]: ");

        if (!title.isEmpty()) book.setTitle(title);
        if (!author.isEmpty()) book.setAuthor(author);
        if (!genre.isEmpty()) book.setGenre(genre);

        if (library.updateBook(book)) {
            System.out.println("Книга успешно обновлена");
        } else {
            System.out.println("Ошибка при обновлении книги");
        }
    }

    private void deleteBook() {
        String id = getStringInput("Введите ID книги для удаления: ");
        if (library.deleteBook(id)) {
            System.out.println("Книга успешно удалена");
        } else {
            System.out.println("Книга не найдена");
        }
    }

    private void manageMembers() {
        while (true) {
            System.out.println("\n=== УПРАВЛЕНИЕ ЧЛЕНАМИ ===");
            System.out.println("1. Добавить члена");
            System.out.println("2. Показать всех членов");
            System.out.println("3. Найти члена");
            System.out.println("4. Обновить члена");
            System.out.println("5. Удалить члена");
            System.out.println("0. Назад");

            int choice = getIntInput("Выберите опцию: ");

            switch (choice) {
                case 1 -> addMember();
                case 2 -> showAllMembers();
                case 3 -> searchMembers();
                case 4 -> updateMember();
                case 5 -> deleteMember();
                case 0 -> { return; }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private void addMember() {
        System.out.println("\n--- Добавление члена ---");
        String id = UUID.randomUUID().toString().substring(0, 8);
        String name = getStringInput("Введите имя: ");
        String email = getStringInput("Введите email: ");
        String phone = getStringInput("Введите телефон: ");

        Member member = new Member(id, name, email, phone);
        if (library.addMember(member)) {
            System.out.println("Член успешно добавлен! ID: " + id);
        } else {
            System.out.println("Ошибка при добавлении члена");
        }
    }

    private void showAllMembers() {
        System.out.println("\n--- Все члены ---");
        List<Member> members = library.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("Члены не найдены");
        } else {
            members.forEach(member -> {
                String status = member.isActive() ? "Активен" : "Неактивен";
                System.out.printf("%s: %s (%s) [%s] Книг: %d%n",
                        member.getId(), member.getName(), member.getEmail(), status, member.getBooksBorrowed());
            });
        }
    }

    private void searchMembers() {
        String query = getStringInput("Введите поисковый запрос: ");
        List<Member> results = library.searchMembers(query);

        if (results.isEmpty()) {
            System.out.println("Члены не найдены");
        } else {
            System.out.println("Найдено членов: " + results.size());
            results.forEach(System.out::println);
        }
    }

    private void updateMember() {
        String id = getStringInput("Введите ID члена для обновления: ");
        Member member = library.getMember(id);

        if (member == null) {
            System.out.println("Член не найден");
            return;
        }

        System.out.println("Текущие данные: " + member);
        String name = getStringInput("Новое имя [" + member.getName() + "]: ");
        String email = getStringInput("Новый email [" + member.getEmail() + "]: ");
        String phone = getStringInput("Новый телефон [" + member.getPhone() + "]: ");

        if (!name.isEmpty()) member.setName(name);
        if (!email.isEmpty()) member.setEmail(email);
        if (!phone.isEmpty()) member.setPhone(phone);

        if (library.updateMember(member)) {
            System.out.println("Член успешно обновлен");
        } else {
            System.out.println("Ошибка при обновлении члена");
        }
    }

    private void deleteMember() {
        String id = getStringInput("Введите ID члена для удаления: ");
        if (library.deleteMember(id)) {
            System.out.println("Член успешно удален");
        } else {
            System.out.println("Член не найден");
        }
    }

    private void manageLoans() {
        while (true) {
            System.out.println("\n=== УПРАВЛЕНИЕ ЗАЙМАМИ ===");
            System.out.println("1. Выдать книгу");
            System.out.println("2. Вернуть книгу");
            System.out.println("3. Показать просроченные займы");
            System.out.println("0. Назад");

            int choice = getIntInput("Выберите опцию: ");

            switch (choice) {
                case 1 -> borrowBook();
                case 2 -> returnBook();
                case 3 -> showOverdueLoans();
                case 0 -> { return; }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private void borrowBook() {
        System.out.println("\n--- Выдача книги ---");
        String bookId = getStringInput("Введите ID книги: ");
        String memberId = getStringInput("Введите ID члена: ");
        int period = getIntInput("Введите срок займа (дни): ");

        if (library.borrowBook(bookId, memberId, period)) {
            System.out.println("Книга успешно выдана");
        } else {
            System.out.println("Ошибка при выдаче книги");
        }
    }

    private void returnBook() {
        System.out.println("\n--- Возврат книги ---");
        String bookId = getStringInput("Введите ID книги: ");
        String memberId = getStringInput("Введите ID члена: ");

        if (library.returnBook(bookId, memberId)) {
            System.out.println("Книга успешно возвращена");
        } else {
            System.out.println("Ошибка при возврате книги");
        }
    }

    private void showOverdueLoans() {
        System.out.println("\n--- Просроченные займы ---");
        List<Loan> overdueLoans = library.getOverdueLoans();

        if (overdueLoans.isEmpty()) {
            System.out.println("Просроченных займов нет");
        } else {
            overdueLoans.forEach(loan -> {
                System.out.printf("Книга: %s, Член: %s, Просрочено дней: %d, Штраф: %.2f%n",
                        loan.getBookId(), loan.getMemberId(), loan.getDaysOverdue(), loan.getFineAmount());
            });
        }
    }

    private void showStatistics() {
        System.out.println("\n=== СТАТИСТИКА И ОТЧЕТЫ ===");

        List<Book> popularBooks = library.getPopularBooks(5);
        System.out.println("\n--- Топ-5 популярных книг ---");
        popularBooks.forEach(book -> {
            System.out.printf("'%s' - %s (Выдач: %d)%n",
                    book.getTitle(), book.getAuthor(), book.getBorrowCount());
        });

        List<Member> activeMembers = library.getActiveMembers();
        System.out.println("\n--- Активные члены ---");
        activeMembers.forEach(member -> {
            System.out.printf("%s (Книг на руках: %d)%n",
                    member.getName(), member.getBooksBorrowed());
        });

        System.out.println("\n--- Общая статистика ---");
        System.out.println("Всего книг: " + library.getBooks().size());
        System.out.println("Всего членов: " + library.getMembers().size());
        System.out.println("Активных займов: " +
                library.getLoans().stream().filter(loan -> !loan.isReturned()).count());
    }

    private void systemOperations() {
        while (true) {
            System.out.println("\n=== СИСТЕМНЫЕ ОПЕРАЦИИ ===");
            System.out.println("1. Создать бэкап");
            System.out.println("2. Сгенерировать отчеты");
            System.out.println("3. Показать список бэкапов");
            System.out.println("0. Назад");

            int choice = getIntInput("Выберите опцию: ");

            switch (choice) {
                case 1 -> library.createBackup();
                case 2 -> library.generateReports();
                case 3 -> showBackups();
                case 0 -> { return; }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private void showBackups() {
        System.out.println("\n--- Список бэкапов ---");
        List<String> backups = FileManager.getBackupList();
        if (backups.isEmpty()) {
            System.out.println("Бэкапы не найдены");
        } else {
            backups.forEach(System.out::println);
        }
    }

    // Вспомогательные методы для ввода
    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите число!");
            scanner.next();
            System.out.print(prompt);
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return input;
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static void main(String[] args) {
        MainBlock9 app = new MainBlock9();
        app.start();
    }
}
