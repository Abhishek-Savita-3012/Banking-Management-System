package util;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReceiptGenerator {

    public static void generateReceipt(long accountNumber, String transactionType, double amount, double balanceAfter) {

        try {
            File folder = new File("receipts");

            if (!folder.exists()) {
                folder.mkdir();
            }

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String fileName = "receipts/receipt_" + accountNumber + "_" + timestamp + ".txt";

            FileWriter writer = new FileWriter(fileName);

            writer.write("====================================\n");
            writer.write("         BANK TRANSACTION RECEIPT    \n");
            writer.write("====================================\n");
            writer.write("Account Number   : " + accountNumber + "\n");
            writer.write("Transaction Type : " + transactionType + "\n");
            writer.write("Amount           : ₹" + amount + "\n");
            writer.write("Balance After    : ₹" + balanceAfter + "\n");
            writer.write("Date & Time      : " + LocalDateTime.now() + "\n");
            writer.write("====================================\n");
            writer.write("Thank you for banking with us!\n");

            writer.close();

            System.out.println("Receipt generated: " + fileName);

        } catch (Exception e) {
            System.out.println("Error while generating receipt!");
            e.printStackTrace();
        }
    }
}