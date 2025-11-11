package BLOCK9;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileManager {
    private static final String BASE_DIR = "library_data";
    private static final String BACKUP_DIR = BASE_DIR + "/backups";
    private static final String REPORTS_DIR = BASE_DIR + "/reports";
    private static final String BOOKS_FILE = BASE_DIR + "/books.dat";
    private static final String MEMBERS_FILE = BASE_DIR + "/members.dat";
    private static final String LOANS_CSV = BASE_DIR + "/loans.csv";
    private static final String TRANSACTIONS_LOG = BASE_DIR + "/transactions.log";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static {
        createDirectories();
    }

    private static void createDirectories() {
        try {
            Files.createDirectories(Paths.get(BASE_DIR));
            Files.createDirectories(Paths.get(BACKUP_DIR));
            Files.createDirectories(Paths.get(REPORTS_DIR));
        } catch (IOException e) {
            System.err.println("Ошибка создания директорий: " + e.getMessage());
        }
    }

    // === Serialization Methods ===

    public static void saveBooks(Map<String, Book> books) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKS_FILE))) {
            oos.writeObject(books);
            logTransaction("SAVE_BOOKS", "Saved " + books.size() + " books");
        } catch (IOException e) {
            System.err.println("Ошибка сохранения книг: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Book> loadBooks() {
        File file = new File(BOOKS_FILE);
        if (!file.exists()) return new HashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка загрузки книг: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public static void saveMembers(Map<String, Member> members) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MEMBERS_FILE))) {
            oos.writeObject(members);
            logTransaction("SAVE_MEMBERS", "Saved " + members.size() + " members");
        } catch (IOException e) {
            System.err.println("Ошибка сохранения членов: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Member> loadMembers() {
        File file = new File(MEMBERS_FILE);
        if (!file.exists()) return new HashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, Member>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка загрузки членов: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // === CSV Methods for Loans ===

    public static void saveLoans(List<Loan> loans) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(LOANS_CSV))) {
            writer.write("id,bookId,memberId,borrowDate,dueDate,returnDate,fineAmount,returned");
            writer.newLine();

            for (Loan loan : loans) {
                String line = String.format("%s,%s,%s,%s,%s,%s,%.2f,%s",
                        loan.getId(),
                        loan.getBookId(),
                        loan.getMemberId(),
                        loan.getBorrowDate(),
                        loan.getDueDate(),
                        loan.getReturnDate() != null ? loan.getReturnDate().toString() : "",
                        loan.getFineAmount(),
                        loan.isReturned()
                );
                writer.write(line);
                writer.newLine();
            }
            logTransaction("SAVE_LOANS", "Saved " + loans.size() + " loans to CSV");
        } catch (IOException e) {
            System.err.println("Ошибка сохранения займов: " + e.getMessage());
        }
    }

    public static List<Loan> loadLoans() {
        List<Loan> loans = new ArrayList<>();
        Path path = Paths.get(LOANS_CSV);

        if (!Files.exists(path)) return loans;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    Loan loan = new Loan();
                    loan.setId(parts[0]);
                    loan.setBookId(parts[1]);
                    loan.setMemberId(parts[2]);
                    loan.setBorrowDate(LocalDate.parse(parts[3]));
                    loan.setDueDate(LocalDate.parse(parts[4]));
                    loan.setReturnDate(parts[5].isEmpty() ? null : LocalDate.parse(parts[5]));
                    loan.setFineAmount(Double.parseDouble(parts[6]));
                    loan.setReturned(Boolean.parseBoolean(parts[7]));
                    loans.add(loan);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки займов: " + e.getMessage());
        }
        return loans;
    }

    // === Logging ===

    public static void logTransaction(String action, String details) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(TRANSACTIONS_LOG),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            String logEntry = String.format("[%s] %s: %s",
                    LocalDate.now().toString(),
                    action,
                    details);
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Ошибка записи в лог: " + e.getMessage());
        }
    }

    // === Backup Methods ===

    public static void createBackup() {
        String backupName = String.format("backup_%s.zip", LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        Path backupPath = Paths.get(BACKUP_DIR, backupName);

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(backupPath.toFile()))) {
            addFileToZip(zos, BOOKS_FILE, "books.dat");
            addFileToZip(zos, MEMBERS_FILE, "members.dat");
            addFileToZip(zos, LOANS_CSV, "loans.csv");
            addFileToZip(zos, TRANSACTIONS_LOG, "transactions.log");

            logTransaction("BACKUP_CREATED", "Backup created: " + backupName);
            System.out.println("Бэкап создан: " + backupName);
        } catch (IOException e) {
            System.err.println("Ошибка создания бэкапа: " + e.getMessage());
        }
    }

    private static void addFileToZip(ZipOutputStream zos, String filePath, String entryName) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            ZipEntry entry = new ZipEntry(entryName);
            zos.putNextEntry(entry);
            Files.copy(file.toPath(), zos);
            zos.closeEntry();
        }
    }

    // === Report Methods ===

    public static void savePopularBooksReport(List<Book> popularBooks) {
        String reportName = String.format("popular_books_%s.txt", LocalDate.now().getYear());
        Path reportPath = Paths.get(REPORTS_DIR, reportName);

        try (BufferedWriter writer = Files.newBufferedWriter(reportPath)) {
            writer.write("=== ОТЧЕТ ПО ПОПУЛЯРНЫМ КНИГАМ ===");
            writer.newLine();
            writer.write("Сгенерирован: " + LocalDate.now());
            writer.newLine();
            writer.newLine();

            for (int i = 0; i < popularBooks.size(); i++) {
                Book book = popularBooks.get(i);
                writer.write(String.format("%d. %s - %s (Выдач: %d)",
                        i + 1, book.getTitle(), book.getAuthor(), book.getBorrowCount()));
                writer.newLine();
            }

            logTransaction("REPORT_GENERATED", "Popular books report: " + reportName);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения отчета: " + e.getMessage());
        }
    }

    public static void saveMemberStatsReport(List<Member> activeMembers) {
        String reportName = "member_stats.txt";
        Path reportPath = Paths.get(REPORTS_DIR, reportName);

        try (BufferedWriter writer = Files.newBufferedWriter(reportPath)) {
            writer.write("=== СТАТИСТИКА ЧЛЕНОВ БИБЛИОТЕКИ ===");
            writer.newLine();
            writer.write("Сгенерирован: " + LocalDate.now());
            writer.newLine();
            writer.newLine();

            writer.write("Активные члены:");
            writer.newLine();
            for (Member member : activeMembers) {
                if (member.isActive()) {
                    writer.write(String.format("- %s (Книг на руках: %d, До: %s)",
                            member.getName(), member.getBooksBorrowed(), member.getExpirationDate()));
                    writer.newLine();
                }
            }

            logTransaction("REPORT_GENERATED", "Member stats report");
        } catch (IOException e) {
            System.err.println("Ошибка сохранения отчета: " + e.getMessage());
        }
    }

    public static List<String> getBackupList() {
        List<String> backups = new ArrayList<>();
        try {
            Files.list(Paths.get(BACKUP_DIR))
                    .filter(path -> path.toString().endsWith(".zip"))
                    .forEach(path -> backups.add(path.getFileName().toString()));
        } catch (IOException e) {
            System.err.println("Ошибка чтения списка бэкапов: " + e.getMessage());
        }
        return backups;
    }
}
