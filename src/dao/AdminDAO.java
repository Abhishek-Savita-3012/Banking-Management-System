package dao;

import config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAO {

    public void showDashboard() {
        try {
            Connection connection = DBConnection.getConnection();

            int totalCustomers = getCount(connection, "SELECT COUNT(*) FROM customers");
            int totalAccounts = getCount(connection, "SELECT COUNT(*) FROM accounts");
            int activeAccounts = getCount(connection, "SELECT COUNT(*) FROM accounts WHERE status = 'ACTIVE'");
            int closedAccounts = getCount(connection, "SELECT COUNT(*) FROM accounts WHERE status = 'CLOSED'");
            int blockedAccounts = getCount(connection, "SELECT COUNT(*) FROM accounts WHERE status = 'BLOCKED'");

            double totalBankBalance = getDoubleValue(connection, "SELECT IFNULL(SUM(balance), 0) FROM accounts");
            double averageBalance = getDoubleValue(connection, "SELECT IFNULL(AVG(balance), 0) FROM accounts");

            System.out.println("\n========== ADMIN DASHBOARD ==========");
            System.out.println("Total Customers     : " + totalCustomers);
            System.out.println("Total Accounts      : " + totalAccounts);
            System.out.println("Active Accounts     : " + activeAccounts);
            System.out.println("Closed Accounts     : " + closedAccounts);
            System.out.println("Blocked Accounts    : " + blockedAccounts);
            System.out.println("Total Bank Balance  : ₹" + totalBankBalance);
            System.out.println("Average Balance     : ₹" + averageBalance);
            System.out.println("=====================================");

            showHighestBalanceAccount(connection);
            showLowestBalanceAccount(connection);

        } catch (Exception e) {
            System.out.println("Error while loading admin dashboard!");
            e.printStackTrace();
        }
    }

    private int getCount(Connection connection, String query) {
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private double getDoubleValue(Connection connection, String query) {
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    private void showHighestBalanceAccount(Connection connection) {
        String query = "SELECT account_number, balance FROM accounts ORDER BY balance DESC LIMIT 1";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\nHighest Balance Account");
                System.out.println("Account Number : " + rs.getLong("account_number"));
                System.out.println("Balance        : ₹" + rs.getDouble("balance"));
            } else {
                System.out.println("\nNo account data found for highest balance.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLowestBalanceAccount(Connection connection) {
        String query = "SELECT account_number, balance FROM accounts ORDER BY balance ASC LIMIT 1";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\nLowest Balance Account");
                System.out.println("Account Number : " + rs.getLong("account_number"));
                System.out.println("Balance        : ₹" + rs.getDouble("balance"));
            } else {
                System.out.println("\nNo account data found for lowest balance.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}