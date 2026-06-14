package dao;

import config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TransactionDAO {

    public void addTransaction(long accountNumber, String transactionType, double amount, double balanceAfter) {
        String query = "INSERT INTO transactions(account_number, transaction_type, amount, balance_after) VALUES (?, ?, ?, ?)";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setLong(1, accountNumber);
            ps.setString(2, transactionType);
            ps.setDouble(3, amount);
            ps.setDouble(4, balanceAfter);

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error while saving transaction!");
            e.printStackTrace();
        }
    }

    public void viewTransactionHistory(long accountNumber) {
        String query = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setLong(1, accountNumber);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n----- Transaction History -----");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("Transaction ID   : " + rs.getInt("transaction_id"));
                System.out.println("Account Number   : " + rs.getLong("account_number"));
                System.out.println("Transaction Type : " + rs.getString("transaction_type"));
                System.out.println("Amount           : ₹" + rs.getDouble("amount"));
                System.out.println("Balance After    : ₹" + rs.getDouble("balance_after"));
                System.out.println("Date             : " + rs.getTimestamp("transaction_date"));
                System.out.println("--------------------------------");
            }

            if (!found) {
                System.out.println("No transactions found!");
            }

        } catch (Exception e) {
            System.out.println("Error while viewing transaction history!");
            e.printStackTrace();
        }
    }
}