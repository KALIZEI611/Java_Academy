package BankAccount;

public class Client {
    private String passport;
    private String name;
    private Bank.Account account;
    public Client(String name, String passport){
        this.passport = passport;
        this.name = name;
    }
    public String getPassport() {
        return passport;
    }
    public String getName() {
        return name;
    }
    public String toString(){
        return this.name+" "+this.passport;
    }
}
