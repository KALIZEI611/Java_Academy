package BLOCK9;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String email;
    private String phone;
    private LocalDate membershipDate;
    private LocalDate expirationDate;
    private boolean active;
    private int booksBorrowed;

    public Member() {}

    public Member(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.membershipDate = LocalDate.now();
        this.expirationDate = LocalDate.now().plusYears(1);
        this.active = true;
        this.booksBorrowed = 0;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getMembershipDate() { return membershipDate; }
    public void setMembershipDate(LocalDate membershipDate) { this.membershipDate = membershipDate; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public int getBooksBorrowed() { return booksBorrowed; }
    public void setBooksBorrowed(int booksBorrowed) { this.booksBorrowed = booksBorrowed; }
    public void incrementBooksBorrowed() { this.booksBorrowed++; }
    public void decrementBooksBorrowed() { this.booksBorrowed = Math.max(0, booksBorrowed - 1); }

    public boolean canBorrowMoreBooks(int maxBooks) {
        return active && booksBorrowed < maxBooks && expirationDate.isAfter(LocalDate.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(email, member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return String.format("Member{id='%s', name='%s', email='%s', phone='%s', membershipDate=%s, expirationDate=%s, active=%s, booksBorrowed=%d}",
                id, name, email, phone, membershipDate, expirationDate, active, booksBorrowed);
    }
}

