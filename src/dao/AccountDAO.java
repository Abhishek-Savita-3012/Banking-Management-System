package dao;

import config.DBConnection;
import model.Account;
import util.ReceiptGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class AccountDAO {

    TransactionDAO transactionDAO = new TransactionDAO();

    public long generateAccountNumber() {
        Random random = new Random();
        return 1000000000L + random.nextInt(900000000);
    }

    public void createAccount(Account account) {
        String query = "INSERT INTO accounts(account_number, customer_id, account_type, balance, pin, status) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setLong(1, account.getAccountNumber());
            ps.setInt(2, account.getCustomerId());
            ps.setString(3, account.getAccountType());
            ps.setDouble(4, account.getBalance());
            ps.setInt(5, account.getPin());
            ps.setString(6, account.getStatus());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Account created successfully!");
                System.out.println("Account Number: " + account.getAccountNumber());
            } else {
                System.out.println("Account creation failed!");
            }

        } catch (Exception e) {
            System.out.println("Error while creating account!");
            e.printStackTrace();
        }
    }

    public void viewAllAccounts() {
        String query = "SELECT * FROM accounts";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n----- Account List -----");

            while (rs.next()) {
                System.out.println("Account ID     : " + rs.getInt("account_id"));
                System.out.println("Account Number : " + rs.getLong("account_number"));
                System.out.println("Customer ID    : " + rs.getInt("customer_id"));
                System.out.println("Account Type   : " + rs.getString("account_type"));
                System.out.println("Balance        : " + rs.getDouble("balance"));
                System.out.println("Status         : " + rs.getString("status"));
                System.out.println("-------------------------");
            }

        } catch (Exception e) {
            System.out.println("Error while viewing accounts!");
            e.printStackTrace();
        }
    }

    public void searchAccountByNumber(long accountNumber) {
        String query = "SELECT * FROM accounts WHERE account_number = ?";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setLong(1, accountNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\nAccount Found!");
                System.out.println("Account ID     : " + rs.getInt("account_id"));
                System.out.println("Account Number : " + rs.getLong("account_number"));
                System.out.println("Customer ID    : " + rs.getInt("customer_id"));
                System.out.println("Account Type   : " + rs.getString("account_type"));
                System.out.println("Balance        : " + rs.getDouble("balance"));
                System.out.println("Status         : " + rs.getString("status"));
            } else {
                System.out.println("Account not found!");
            }

        } catch (Exception e) {
            System.out.println("Error while searching account!");
            e.printStackTrace();
        }
    }

    public void updateAccountStatus(long accountNumber, String status) {
        String query = "UPDATE accounts SET status = ? WHERE account_number = ?";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, status);
            ps.setLong(2, accountNumber);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Account status updated successfully!");
            } else {
                System.out.println("Account not found!");
            }

        } catch (Exception e) {
            System.out.println("Error while updating account status!");
            e.printStackTrace();
        }
    }

    public void closeAccount(long accountNumber) {
        String query = "UPDATE accounts SET status = 'CLOSED' WHERE account_number = ?";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setLong(1, accountNumber);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Account closed successfully!");
            } else {
                System.out.println("Account not found!");
            }

        } catch (Exception e) {
            System.out.println("Error while closing account!");
            e.printStackTrace();
        }
    }

    public void depositMoney(long accountNumber, double amount) {
        String query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ? AND status = 'ACTIVE'";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setDouble(1, amount);
            ps.setLong(2, accountNumber);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                double newBalance = getBalance(accountNumber);
                transactionDAO.addTransaction(accountNumber, "DEPOSIT", amount, newBalance);
                ReceiptGenerator.generateReceipt(accountNumber, "DEPOSIT", amount, newBalance);

                System.out.println("Amount deposited successfully!");
            } else {
                System.out.println("Account not found or inactive!");
            }

        } catch (Exception e) {
            System.out.println("Error while depositing money!");
            e.printStackTrace();
        }
    }

    public void withdrawMoney(long accountNumber, int pin, double amount) {
        String selectQuery = "SELECT balance, pin, status FROM accounts WHERE account_number = ?";

        try {
            Connection connection = DBConnection.getConnection();

            PreparedStatement selectPs = connection.prepareStatement(selectQuery);
            selectPs.setLong(1, accountNumber);

            ResultSet rs = selectPs.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                int storedPin = rs.getInt("pin");
                String status = rs.getString("status");

                if (!status.equalsIgnoreCase("ACTIVE")) {
                    System.out.println("Account is not active!");
                    return;
                }

                if (storedPin != pin) {
                    System.out.println("Invalid PIN!");
                    return;
                }

                if (currentBalance < amount) {
                    System.out.println("Insufficient balance!");
                    return;
                }

                String updateQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                PreparedStatement updatePs = connection.prepareStatement(updateQuery);

                updatePs.setDouble(1, amount);
                updatePs.setLong(2, accountNumber);

                int rows = updatePs.executeUpdate();

                if (rows > 0) {
                    double newBalance = getBalance(accountNumber);
                    transactionDAO.addTransaction(accountNumber, "WITHDRAW", amount, newBalance);
                    ReceiptGenerator.generateReceipt(accountNumber, "WITHDRAW", amount, newBalance);

                    System.out.println("Withdrawal successful!");
                }

            } else {
                System.out.println("Account not found!");
            }

        } catch (Exception e) {
            System.out.println("Error while withdrawing money!");
            e.printStackTrace();
        }
    }

    public void checkBalance(long accountNumber) {
        String query = "SELECT balance, status FROM accounts WHERE account_number = ?";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setLong(1, accountNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Account Status  : " + rs.getString("status"));
                System.out.println("Current Balance : ₹" + rs.getDouble("balance"));
            } else {
                System.out.println("Account not found!");
            }

        } catch (Exception e) {
            System.out.println("Error while checking balance!");
            e.printStackTrace();
        }
    }

    public void transferMoney(long senderAccount, long receiverAccount, int pin, double amount) {
        try {
            Connection connection = DBConnection.getConnection();

            String senderQuery = "SELECT balance, pin, status FROM accounts WHERE account_number = ?";
            PreparedStatement senderPs = connection.prepareStatement(senderQuery);
            senderPs.setLong(1, senderAccount);

            ResultSet senderRs = senderPs.executeQuery();

            if (!senderRs.next()) {
                System.out.println("Sender account not found!");
                return;
            }

            double senderBalance = senderRs.getDouble("balance");
            int storedPin = senderRs.getInt("pin");
            String senderStatus = senderRs.getString("status");

            if (!senderStatus.equalsIgnoreCase("ACTIVE")) {
                System.out.println("Sender account is not active!");
                return;
            }

            if (storedPin != pin) {
                System.out.println("Invalid PIN!");
                return;
            }

            if (senderBalance < amount) {
                System.out.println("Insufficient balance!");
                return;
            }

            String receiverQuery = "SELECT status FROM accounts WHERE account_number = ?";
            PreparedStatement receiverPs = connection.prepareStatement(receiverQuery);
            receiverPs.setLong(1, receiverAccount);

            ResultSet receiverRs = receiverPs.executeQuery();

            if (!receiverRs.next()) {
                System.out.println("Receiver account not found!");
                return;
            }

            String receiverStatus = receiverRs.getString("status");

            if (!receiverStatus.equalsIgnoreCase("ACTIVE")) {
                System.out.println("Receiver account is not active!");
                return;
            }

            PreparedStatement debitPs = connection.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE account_number = ?"
            );

            debitPs.setDouble(1, amount);
            debitPs.setLong(2, senderAccount);
            debitPs.executeUpdate();

            PreparedStatement creditPs = connection.prepareStatement(
                    "UPDATE accounts SET balance = balance + ? WHERE account_number = ?"
            );

            creditPs.setDouble(1, amount);
            creditPs.setLong(2, receiverAccount);
            creditPs.executeUpdate();

            double senderNewBalance = getBalance(senderAccount);
            double receiverNewBalance = getBalance(receiverAccount);

            transactionDAO.addTransaction(senderAccount, "TRANSFER_DEBIT", amount, senderNewBalance);
            transactionDAO.addTransaction(receiverAccount, "TRANSFER_CREDIT", amount, receiverNewBalance);

            ReceiptGenerator.generateReceipt(senderAccount, "TRANSFER_DEBIT", amount, senderNewBalance);
            ReceiptGenerator.generateReceipt(receiverAccount, "TRANSFER_CREDIT", amount, receiverNewBalance);

            System.out.println("Transfer successful!");

        } catch (Exception e) {
            System.out.println("Error while transferring money!");
            e.printStackTrace();
        }
    }

    public double getBalance(long accountNumber) {
        String query = "SELECT balance FROM accounts WHERE account_number = ?";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setLong(1, accountNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }

        } catch (Exception e) {
            System.out.println("Error while getting balance!");
            e.printStackTrace();
        }

        return 0.0;
    }
}