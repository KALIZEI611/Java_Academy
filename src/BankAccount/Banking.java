package BankAccount;

public class Banking {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Bank.Account acc = bank.createAccount("Иванов", "АК123456");
        Bank.Account acc1 = bank.createAccount("Петров", "АЕ123456",50);
        AccountOperation oper = new AccountOperation(acc,100);
        CrossAccountOperation oper2 = new CrossAccountOperation(acc,
                acc1,10);
        bank.addOperation(oper);
        bank.addOperation(oper2);
        bank.runOperations();
        System.out.print(bank);
    }
}
