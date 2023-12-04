import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class BankAccount {
    private String accountNumber;
    private String accountHolder;
    private double balance;
    private List<String> transactionHistory;

    public BankAccount(String accountNumber, String accountHolder) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add("Deposit: +$" + amount);
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            transactionHistory.add("Withdrawal: -$" + amount);
        } else {
            System.out.println("Insufficient funds!");
        }
    }

    public void transfer(BankAccount recipient, double amount) {
        if (balance >= amount) {
            balance -= amount;
            recipient.deposit(amount);
            transactionHistory.add("Transfer to " + recipient.getAccountNumber() + ": -$" + amount);
        } else {
            System.out.println("Insufficient funds!");
        }
    }
}

class OnlineBank {
    private Map<String, BankAccount> accounts;

    public OnlineBank() {
        this.accounts = new HashMap<>();
    }

    public void createAccount(String accountNumber, String accountHolder) {
        BankAccount account = new BankAccount(accountNumber, accountHolder);
        accounts.put(accountNumber, account);
        System.out.println("Account created successfully!");
    }

    public BankAccount getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void displayTransactionHistory(String accountNumber) {
        BankAccount account = getAccount(accountNumber);
        if (account != null) {
            System.out.println("Transaction History for Account " + accountNumber + ":");
            for (String transaction : account.getTransactionHistory()) {
                System.out.println(transaction);
            }
        } else {
            System.out.println("Account not found!");
        }
    }
}

public class OnlineBankSystem {
    public static void main(String[] args) {
        OnlineBank onlineBank = new OnlineBank();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. View Transaction History");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter account number: ");
                    String accountNumber = scanner.nextLine();
                    System.out.print("Enter account holder name: ");
                    String accountHolder = scanner.nextLine();
                    onlineBank.createAccount(accountNumber, accountHolder);
                    break;

                case 2:
                    performTransaction(onlineBank, scanner, "Deposit");
                    break;

                case 3:
                    performTransaction(onlineBank, scanner, "Withdraw");
                    break;

                case 4:
                    System.out.print("Enter your account number: ");
                    String senderAccountNumber = scanner.nextLine();
                    System.out.print("Enter recipient account number: ");
                    String recipientAccountNumber = scanner.nextLine();
                    System.out.print("Enter transfer amount: ");
                    double transferAmount = scanner.nextDouble();
                    BankAccount senderAccount = onlineBank.getAccount(senderAccountNumber);
                    BankAccount recipientAccount = onlineBank.getAccount(recipientAccountNumber);
                    if (senderAccount != null && recipientAccount != null) {
                        senderAccount.transfer(recipientAccount, transferAmount);
                    } else {
                        System.out.println("Invalid account numbers!");
                    }
                    break;

                case 5:
                    System.out.print("Enter your account number: ");
                    String viewHistoryAccountNumber = scanner.nextLine();
                    onlineBank.displayTransactionHistory(viewHistoryAccountNumber);
                    break;

                case 6:
                    System.out.println("Exiting the system. Thank you!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void performTransaction(OnlineBank onlineBank, Scanner scanner, String transactionType) {
        System.out.print("Enter your account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter " + transactionType.toLowerCase() + " amount: ");
        double amount = scanner.nextDouble();
        BankAccount account = onlineBank.getAccount(accountNumber);
        if (account != null) {
            if (transactionType.equals("Deposit")) {
                account.deposit(amount);
            } else if (transactionType.equals("Withdraw")) {
                account.withdraw(amount);
            }
        } else {
            System.out.println("Invalid account number!");
        }
    }
}
