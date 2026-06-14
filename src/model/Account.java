package model;

public class Account {
    private int accountId;
    private long accountNumber;
    private int customerId;
    private String accountType;
    private double balance;
    private int pin;
    private String status;

    public Account() {
    }

    public Account(long accountNumber, int customerId, String accountType, double balance, int pin, String status) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = balance;
        this.pin = pin;
        this.status = status;
    }

    public int getAccountId() {
        return accountId;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public int getPin() {
        return pin;
    }

    public String getStatus() {
        return status;
    }
}
