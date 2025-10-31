package BankAccount;

import javax.management.OperationsException;

public abstract class Operation {
    protected double amount;
    //Выполняет банковскую операцию
    public abstract void doWork( ) throws OperationsException;
}
