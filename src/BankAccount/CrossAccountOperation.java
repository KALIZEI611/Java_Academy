package BankAccount;

import javax.management.OperationsException;

public class CrossAccountOperation  extends Operation{
    private Bank.Account outAccount;
    //Счет на который поступают денежные средства
    private Bank.Account inAccount;
    public CrossAccountOperation(Bank.Account inAccount,Bank.Account
            outAccount,double amount){
        this.inAccount = inAccount;
        this.outAccount = outAccount;
        super.amount = amount;
    }
    public void doWork() throws OperationsException {
        double balance = outAccount.getBalance();
        if (balance < amount)
            throw new OperationsException("Недостаточно денежных средств на счете "+outAccount);
        inAccount.setBalance(balance+amount);
        outAccount.setBalance(balance-amount);
    }
}
