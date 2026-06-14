package service;

public class BankService {

    public boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    public boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

    public boolean isValidAmount(double amount) {
        return amount > 0;
    }

    public boolean isValidOpeningBalance(double balance) {
        return balance >= 0;
    }

    public boolean isValidPin(int pin) {
        return String.valueOf(pin).matches("\\d{4}");
    }

    public boolean isValidAccountType(String accountType) {
        return accountType.equalsIgnoreCase("Savings")
                || accountType.equalsIgnoreCase("Current");
    }

    public boolean isValidStatus(String status) {
        return status.equalsIgnoreCase("ACTIVE")
                || status.equalsIgnoreCase("BLOCKED")
                || status.equalsIgnoreCase("CLOSED");
    }

    public boolean isSameAccount(long sender, long receiver) {
        return sender == receiver;
    }
}