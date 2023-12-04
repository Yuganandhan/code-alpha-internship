import java.util.ArrayList;
import java.util.Scanner;

class Transaction {
    String type;
    double amount;

    Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return type + ": $" + amount;
    }
}

class BankAccount {
    private String accountNumber;
    private String accountHolder;
    private double balance;
    private ArrayList<Transaction> transactions;

    BankAccount(String accountNumber, String accountHolder) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    void deposit(double amount) {
        balance += amount;
        transactions.add(new Transaction("Deposit", amount));
    }

    void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactions.add(new Transaction("Withdrawal", amount));
        } else {
            System.out.println("Insufficient funds!");
        }
    }

    void transfer(BankAccount recipient, double amount) {
        if (amount <= balance) {
            balance -= amount;
            recipient.deposit(amount);
            transactions.add(new Transaction("Transfer to " + recipient.accountHolder, amount));
        } else {
            System.out.println("Insufficient funds!");
        }
    }

    void displayTransactionHistory() {
        System.out.println("Transaction History for Account " + accountNumber + ":");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
        System.out.println("Current Balance: $" + balance);
    }
}

public class OnlineBankingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Online Banking System!");

        // Create accounts
        BankAccount account1 = new BankAccount("123456", "John Doe");
        BankAccount account2 = new BankAccount("789012", "Jane Smith");

        // Simulate transactions
        account1.deposit(1000);
        account1.withdraw(200);
        account1.transfer(account2, 300);

        account2.deposit(500);
        account2.transfer(account1, 100);

        // Display transaction history
        account1.displayTransactionHistory();
        account2.displayTransactionHistory();

        scanner.close();
    }
}
