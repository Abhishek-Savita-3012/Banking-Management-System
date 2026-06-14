import dao.AccountDAO;
import dao.CustomerDAO;
import model.Account;
import model.Customer;
import dao.TransactionDAO;
import dao.AdminDAO;
import service.BankService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();
        TransactionDAO transactionDAO = new TransactionDAO();
        AdminDAO adminDAO = new AdminDAO();
        BankService bankService = new BankService();

        int choice;

        do {
            System.out.println("\n===== Bank Management System =====");
            System.out.println("1. Add Customer");
            System.out.println("2. View All Customers");
            System.out.println("3. Search Customer By ID");
            System.out.println("4. Update Customer");
            System.out.println("5. Delete Customer");
            System.out.println("6. Create Account");
            System.out.println("7. View All Accounts");
            System.out.println("8. Search Account By Account Number");
            System.out.println("9. Update Account Status");
            System.out.println("10. Close Account");
            System.out.println("11. Deposit Money");
            System.out.println("12. Withdraw Money");
            System.out.println("13. Check Balance");
            System.out.println("14. Transfer Money");
            System.out.println("15. View Transaction History");
            System.out.println("16. Admin Dashboard");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter phone: ");
                    String phone = sc.nextLine();

                    System.out.print("Enter address: ");
                    String address = sc.nextLine();

                    if (!bankService.isValidName(name)) {
                        System.out.println("Name cannot be empty!");
                        break;
                    }

                    if (!bankService.isValidEmail(email)) {
                        System.out.println("Invalid email!");
                        break;
                    }

                    if (!bankService.isValidPhone(phone)) {
                        System.out.println("Phone number must be exactly 10 digits!");
                        break;
                    }

                    if (!bankService.isValidAddress(address)) {
                        System.out.println("Address cannot be empty!");
                        break;
                    }

                    Customer customer = new Customer(name, email, phone, address);
                    customerDAO.addCustomer(customer);
                    break;

                case 2:
                    customerDAO.viewAllCustomers();
                    break;

                case 3:
                    System.out.print("Enter customer ID: ");
                    int searchId = sc.nextInt();
                    customerDAO.searchCustomerById(searchId);
                    break;

                case 4:
                    System.out.print("Enter customer ID to update: ");
                    int updateId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter new name: ");
                    String newName = sc.nextLine();

                    System.out.print("Enter new email: ");
                    String newEmail = sc.nextLine();

                    System.out.print("Enter new phone: ");
                    String newPhone = sc.nextLine();

                    System.out.print("Enter new address: ");
                    String newAddress = sc.nextLine();

                    if (!bankService.isValidName(newName)) {
                        System.out.println("Name cannot be empty!");
                        break;
                    }

                    if (!bankService.isValidEmail(newEmail)) {
                        System.out.println("Invalid email!");
                        break;
                    }

                    if (!bankService.isValidPhone(newPhone)) {
                        System.out.println("Phone number must be exactly 10 digits!");
                        break;
                    }

                    if (!bankService.isValidAddress(newAddress)) {
                        System.out.println("Address cannot be empty!");
                        break;
                    }

                    customerDAO.updateCustomer(updateId, newName, newEmail, newPhone, newAddress);
                    break;

                case 5:
                    System.out.print("Enter customer ID to delete: ");
                    int deleteId = sc.nextInt();
                    customerDAO.deleteCustomer(deleteId);
                    break;

                case 6:
                    System.out.print("Enter customer ID: ");
                    int customerId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter account type (Savings/Current): ");
                    String accountType = sc.nextLine();

                    System.out.print("Enter opening balance: ");
                    double balance = sc.nextDouble();

                    System.out.print("Set 4-digit PIN: ");
                    int pin = sc.nextInt();

                    if (!bankService.isValidAccountType(accountType)) {
                        System.out.println("Account type must be Savings or Current!");
                        break;
                    }

                    if (!bankService.isValidOpeningBalance(balance)) {
                        System.out.println("Opening balance cannot be negative!");
                        break;
                    }

                    if (!bankService.isValidPin(pin)) {
                        System.out.println("PIN must be exactly 4 digits!");
                        break;
                    }

                    long accountNumber = accountDAO.generateAccountNumber();

                    Account account = new Account(
                            accountNumber,
                            customerId,
                            accountType,
                            balance,
                            pin,
                            "ACTIVE"
                    );

                    accountDAO.createAccount(account);
                    break;

                case 7:
                    accountDAO.viewAllAccounts();
                    break;

                case 8:
                    System.out.print("Enter account number: ");
                    long searchAccountNumber = sc.nextLong();
                    accountDAO.searchAccountByNumber(searchAccountNumber);
                    break;

                case 9:
                    System.out.print("Enter account number: ");
                    long updateAccountNumber = sc.nextLong();
                    sc.nextLine();

                    System.out.print("Enter new status (ACTIVE/BLOCKED/CLOSED): ");
                    String status = sc.nextLine();

                    if (!bankService.isValidStatus(status)) {
                        System.out.println("Status must be ACTIVE, BLOCKED, or CLOSED!");
                        break;
                    }

                    accountDAO.updateAccountStatus(updateAccountNumber, status);
                    break;

                case 10:
                    System.out.print("Enter account number to close: ");
                    long closeAccountNumber = sc.nextLong();
                    accountDAO.closeAccount(closeAccountNumber);
                    break;

                case 11:

                    System.out.print("Enter Account Number: ");
                    long depositAcc = sc.nextLong();

                    System.out.print("Enter Amount: ");
                    double depositAmount = sc.nextDouble();

                    if (!bankService.isValidAmount(depositAmount)) {
                        System.out.println("Deposit amount must be greater than 0!");
                        break;
                    }

                    accountDAO.depositMoney(
                            depositAcc,
                            depositAmount
                    );
                    break;

                case 12:

                    System.out.print("Enter Account Number: ");
                    long withdrawAcc = sc.nextLong();

                    System.out.print("Enter PIN: ");
                    int withdrawPin = sc.nextInt();

                    System.out.print("Enter Amount: ");
                    double withdrawAmount = sc.nextDouble();

                    if (!bankService.isValidAmount(withdrawAmount)) {
                        System.out.println("Withdrawal amount must be greater than 0!");
                        break;
                    }

                    if (!bankService.isValidPin(withdrawPin)) {
                        System.out.println("PIN must be exactly 4 digits!");
                        break;
                    }

                    accountDAO.withdrawMoney(
                            withdrawAcc,
                            withdrawPin,
                            withdrawAmount
                    );
                    break;

                case 13:

                    System.out.print("Enter Account Number: ");
                    long balanceAcc = sc.nextLong();

                    accountDAO.checkBalance(balanceAcc);
                    break;

                case 14:

                    System.out.print("Sender Account Number: ");
                    long sender = sc.nextLong();

                    System.out.print("Receiver Account Number: ");
                    long receiver = sc.nextLong();

                    System.out.print("Enter PIN: ");
                    int transferPin = sc.nextInt();

                    System.out.print("Enter Amount: ");
                    double transferAmount = sc.nextDouble();

                    if (bankService.isSameAccount(sender, receiver)) {
                        System.out.println("Sender and receiver account cannot be same!");
                        break;
                    }

                    if (!bankService.isValidPin(transferPin)) {
                        System.out.println("PIN must be exactly 4 digits!");
                        break;
                    }

                    if (!bankService.isValidAmount(transferAmount)) {
                        System.out.println("Transfer amount must be greater than 0!");
                        break;
                    }

                    accountDAO.transferMoney(sender, receiver, transferPin, transferAmount);
                    break;

                case 15:
                    System.out.print("Enter Account Number: ");
                    long historyAccount = sc.nextLong();

                    transactionDAO.viewTransactionHistory(historyAccount);
                    break;

                case 16:
                    adminDAO.showDashboard();
                    break;

                case 0:
                    System.out.println("Thank you for using Bank Management System!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 0);

        sc.close();
    }
}