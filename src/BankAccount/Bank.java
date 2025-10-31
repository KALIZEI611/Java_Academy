package BankAccount;

import javax.management.OperationsException;
import java.util.Vector;

public class Bank {
    private static int accountNum = 1;
    public class Account{
        private double balance;
        private final String number;
        private final Client client;
        public Account(Client client,double startBalance){
            this(client);
            balance = startBalance;
        }
        public Account(Client client){
            this.client = client;
            balance = 0;
            number = "Acc "+accountNum++;
        }
        public double getBalance() {
            return balance;
        }
        public void setBalance(double balance) {
            this.balance = balance;
        }
        public String toString(){
            return "Счет № "+accountNum+" Владелец:"+client.toString()+" Баланс:"+balance;
        }
    }
    private Vector<Client> clients = new Vector<Client>();
    private Vector<Account> accounts = new Vector<Account>();
    private Vector<Operation> operations = new Vector<Operation>();
    public void addOperation(Operation operation){
        operations.add(operation);
    }
    public void runOperations( ){
        for (int i=0; i<operations.size(); i++){
            try {
                operations.get(i).doWork();
            }catch(OperationsException ex){
                System.out.println(ex.getMessage());
            }
        }
        operations.clear();
    }
    public Bank.Account createAccount(String clientName,String passport,
                                      double initialBalance){
        Client cl = null;
        for (int i=0;i<clients.size();i++)
            if (clients.get(i).getPassport() == passport){
                cl = clients.get(i);
                break;
            }
        if (cl == null) {
            cl = new Client(clientName, passport);
            clients.add(cl);
        }
        Account acc= new Account(cl,initialBalance);
        accounts.add(acc);
        return acc;
    }
    public Bank.Account createAccount(String clientName,String passport){
        return createAccount(clientName,passport,0);
    }
    public String toString(){
        StringBuilder builder = new StringBuilder("Состояния счетов \n-------------------------\n");
        for (int i=0; i < accounts.size(); i++){
            builder.append(accounts.get(i));
            builder.append("\n");
        }
        return builder.toString();
    }
}

