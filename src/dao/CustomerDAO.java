package dao;

import config.DBConnection;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDAO {

    public void addCustomer(Customer customer) {
        String query = "INSERT INTO customers(name, email, phone, address) VALUES (?, ?, ?, ?)";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());
            ps.setString(4, customer.getAddress());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Customer added successfully!");
            } else {
                System.out.println("Customer not added!");
            }

        } catch (Exception e) {
            System.out.println("Error while adding customer!");
            e.printStackTrace();
        }
    }

    public void viewAllCustomers() {
        String query = "SELECT * FROM customers";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n----- Customer List -----");

            while (rs.next()) {
                System.out.println("Customer ID: " + rs.getInt("customer_id"));
                System.out.println("Name       : " + rs.getString("name"));
                System.out.println("Email      : " + rs.getString("email"));
                System.out.println("Phone      : " + rs.getString("phone"));
                System.out.println("Address    : " + rs.getString("address"));
                System.out.println("-------------------------");
            }

        } catch (Exception e) {
            System.out.println("Error while viewing customers!");
            e.printStackTrace();
        }
    }

    public void searchCustomerById(int customerId) {
        String query = "SELECT * FROM customers WHERE customer_id = ?";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\nCustomer Found!");
                System.out.println("Customer ID: " + rs.getInt("customer_id"));
                System.out.println("Name       : " + rs.getString("name"));
                System.out.println("Email      : " + rs.getString("email"));
                System.out.println("Phone      : " + rs.getString("phone"));
                System.out.println("Address    : " + rs.getString("address"));
            } else {
                System.out.println("Customer not found!");
            }

        } catch (Exception e) {
            System.out.println("Error while searching customer!");
            e.printStackTrace();
        }
    }

    public void updateCustomer(int customerId, String name, String email, String phone, String address) {
        String query = "UPDATE customers SET name = ?, email = ?, phone = ?, address = ? WHERE customer_id = ?";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setInt(5, customerId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Customer updated successfully!");
            } else {
                System.out.println("Customer not found!");
            }

        } catch (Exception e) {
            System.out.println("Error while updating customer!");
            e.printStackTrace();
        }
    }

    public void deleteCustomer(int customerId) {
        String checkQuery = "SELECT COUNT(*) FROM accounts WHERE customer_id = ?";
        String deleteQuery = "DELETE FROM customers WHERE customer_id = ?";

        try {
            Connection connection = DBConnection.getConnection();

            PreparedStatement checkPs = connection.prepareStatement(checkQuery);
            checkPs.setInt(1, customerId);

            ResultSet rs = checkPs.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Cannot delete customer! This customer has linked bank account(s).");
                System.out.println("Please close/delete the account first, then delete the customer.");
                return;
            }

            PreparedStatement deletePs = connection.prepareStatement(deleteQuery);
            deletePs.setInt(1, customerId);

            int rows = deletePs.executeUpdate();

            if (rows > 0) {
                System.out.println("Customer deleted successfully!");
            } else {
                System.out.println("Customer not found!");
            }

        } catch (Exception e) {
            System.out.println("Error while deleting customer!");
            e.printStackTrace();
        }
    }
}
