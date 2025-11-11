package BLOCK9;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Library {
    private Map<String, Book> books;
    private Map<String, Member> members;
    private List<Loan> loans;
    private int maxBooksPerMember;
    private double dailyFine;

    public Library(int maxBooksPerMember, double dailyFine) {
        this.books = FileManager.loadBooks();
        this.members = FileManager.loadMembers();
        this.loans = FileManager.loadLoans();
        this.maxBooksPerMember = maxBooksPerMember;
        this.dailyFine = dailyFine;
    }

    // === Book CRUD Operations ===

    public boolean addBook(Book book) {
        if (books.containsKey(book.getId())) {
            return false;
        }
        books.put(book.getId(), book);
        FileManager.saveBooks(books);
        FileManager.logTransaction("ADD_BOOK", "Added book: " + book.getTitle());
        return true;
    }

    public Book getBook(String id) {
        return books.get(id);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public boolean updateBook(Book book) {
        if (!books.containsKey(book.getId())) {
            return false;
        }
        books.put(book.getId(), book);
        FileManager.saveBooks(books);
        FileManager.logTransaction("UPDATE_BOOK", "Updated book: " + book.getTitle());
        return true;
    }

    public boolean deleteBook(String id) {
        Book book = books.remove(id);
        if (book != null) {
            FileManager.saveBooks(books);
            FileManager.logTransaction("DELETE_BOOK", "Deleted book: " + book.getTitle());
            return true;
        }
        return false;
    }

    // === Member CRUD Operations ===

    public boolean addMember(Member member) {
        if (members.containsKey(member.getId())) {
            return false;
        }
        members.put(member.getId(), member);
        FileManager.saveMembers(members);
        FileManager.logTransaction("ADD_MEMBER", "Added member: " + member.getName());
        return true;
    }

    public Member getMember(String id) {
        return members.get(id);
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }

    public boolean updateMember(Member member) {
        if (!members.containsKey(member.getId())) {
            return false;
        }
        members.put(member.getId(), member);
        FileManager.saveMembers(members);
        FileManager.logTransaction("UPDATE_MEMBER", "Updated member: " + member.getName());
        return true;
    }

    public boolean deleteMember(String id) {
        Member member = members.remove(id);
        if (member != null) {
            FileManager.saveMembers(members);
            FileManager.logTransaction("DELETE_MEMBER", "Deleted member: " + member.getName());
            return true;
        }
        return false;
    }

    // === Loan Operations ===

    public boolean borrowBook(String bookId, String memberId, int loanPeriodDays) {
        Book book = books.get(bookId);
        Member member = members.get(memberId);

        if (book == null || member == null) {
            return false;
        }

        if (!book.isAvailable()) {
            return false;
        }

        if (!member.canBorrowMoreBooks(maxBooksPerMember)) {
            return false;
        }

        // Create loan
        String loanId = UUID.randomUUID().toString();
        Loan loan = new Loan(loanId, bookId, memberId, LocalDate.now(), loanPeriodDays);

        // Update book and member
        book.setAvailable(false);
        book.incrementBorrowCount();
        member.incrementBooksBorrowed();

        // Save changes
        loans.add(loan);
        books.put(bookId, book);
        members.put(memberId, member);

        FileManager.saveBooks(books);
        FileManager.saveMembers(members);
        FileManager.saveLoans(loans);

        FileManager.logTransaction("BORROW_BOOK",
                String.format("Book %s borrowed by member %s", bookId, memberId));

        return true;
    }

    public boolean returnBook(String bookId, String memberId) {
        Loan loan = loans.stream()
                .filter(l -> l.getBookId().equals(bookId) &&
                        l.getMemberId().equals(memberId) &&
                        !l.isReturned())
                .findFirst()
                .orElse(null);

        if (loan == null) {
            return false;
        }

        Book book = books.get(bookId);
        Member member = members.get(memberId);

        if (book == null || member == null) {
            return false;
        }

        // Update loan
        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());
        loan.calculateFine(dailyFine);

        // Update book and member
        book.setAvailable(true);
        member.decrementBooksBorrowed();

        // Save changes
        books.put(bookId, book);
        members.put(memberId, member);
        FileManager.saveBooks(books);
        FileManager.saveMembers(members);
        FileManager.saveLoans(loans);

        FileManager.logTransaction("RETURN_BOOK",
                String.format("Book %s returned by member %s. Fine: %.2f",
                        bookId, memberId, loan.getFineAmount()));

        return true;
    }

    // === Statistics ===

    public List<Book> getPopularBooks(int limit) {
        return books.values().stream()
                .sorted((b1, b2) -> Integer.compare(b2.getBorrowCount(), b1.getBorrowCount()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Member> getActiveMembers() {
        return members.values().stream()
                .filter(Member::isActive)
                .sorted((m1, m2) -> Integer.compare(m2.getBooksBorrowed(), m1.getBooksBorrowed()))
                .collect(Collectors.toList());
    }

    public List<Loan> getOverdueLoans() {
        return loans.stream()
                .filter(Loan::isOverdue)
                .collect(Collectors.toList());
    }

    public List<Book> searchBooks(String query) {
        String lowerQuery = query.toLowerCase();
        return books.values().stream()
                .filter(book ->
                        book.getTitle().toLowerCase().contains(lowerQuery) ||
                                book.getAuthor().toLowerCase().contains(lowerQuery) ||
                                book.getGenre().toLowerCase().contains(lowerQuery) ||
                                book.getIsbn().contains(query))
                .collect(Collectors.toList());
    }

    public List<Member> searchMembers(String query) {
        String lowerQuery = query.toLowerCase();
        return members.values().stream()
                .filter(member ->
                        member.getName().toLowerCase().contains(lowerQuery) ||
                                member.getEmail().toLowerCase().contains(lowerQuery) ||
                                member.getPhone().contains(query))
                .collect(Collectors.toList());
    }

    // === Getters ===

    public Map<String, Book> getBooks() { return books; }
    public Map<String, Member> getMembers() { return members; }
    public List<Loan> getLoans() { return loans; }
    public int getMaxBooksPerMember() { return maxBooksPerMember; }
    public double getDailyFine() { return dailyFine; }

    // === Backup and Reports ===

    public void createBackup() {
        FileManager.createBackup();
    }

    public void generateReports() {
        List<Book> popularBooks = getPopularBooks(10);
        List<Member> activeMembers = getActiveMembers();

        FileManager.savePopularBooksReport(popularBooks);
        FileManager.saveMemberStatsReport(activeMembers);

        System.out.println("Отчеты сгенерированы в директории library_data/reports/");
    }
}
