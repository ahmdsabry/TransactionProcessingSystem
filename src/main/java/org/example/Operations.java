package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Operations {
    public static int failedTransaction = 0;
    public static int transactionsCount = 0;


    public static void BankEntry(String filePath, ArrayList<UserAccount> userAccounts)  {
        UserAccount account1=new UserAccount();
        Scanner sc=new Scanner(System.in);

        System.out.println("enter account name!");
        String userName = sc.nextLine();
        try {
            account1.setAccountHolder(userName);
            account1.isValidName();
        }catch (UserAccountException e){
            System.out.println(e.getMessage());
            System.exit(0);
        }

        System.out.println("enter account number!");
        String userAccount= sc.next();
        try{
            account1.setAccountNumber(userAccount);
            account1.isValidAccountNumber();
        }
        catch (UserAccountException e)
        {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        for (UserAccount value : userAccounts) {
            if (value.getAccountNumber().equals(userAccount)) {
                System.out.println("this user account name already exists");
                System.exit(0);
            }
        }

        System.out.println("enter Initial balance for account !");
        double userBalance= sc.nextDouble();
        try {
            account1.setAccountBalance(userBalance);
            account1.isValidBalance();

        }catch (UserAccountException e){
            System.out.println(e.getMessage());
            System.exit(0);

        }
//        UserAccount  account=new UserAccount(userName,userAccount,userBalance);

        try (FileWriter fileWriter = new FileWriter(filePath,true);
             BufferedWriter br = new BufferedWriter(fileWriter)) {
            System.out.println("you added into accounts file successfully");
            userAccounts.add(account1);
            br.write(account1.toString());
            br.newLine();
        }
        catch (Exception e ) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public static void BankTps(ArrayList<UserAccount> userAccounts, String filePath) {

        Scanner sc=new Scanner(System.in);

        boolean c=true;
        while (c){
            System.out.println("---------------------");
            System.out.println("choose operation no!");
            ////entry done
            System.out.println("1-journal ");
            System.out.println("2-balance sheet ");
            System.out.println("3-transactions count ");
            System.out.println("4-accounts names with highest balances ");
            System.out.println("5-failed transactions count ");
            System.out.println("6- exit ");
            System.out.println("7- Close the program ");
            byte userInput= sc.nextByte();

            switch (userInput) {
                case 1:
                    journal(userAccounts, filePath);
                    break;
                case 2:
                    showBankSheet(filePath);
                    break;

                case 3:
                    successTransaction();
                    break;
                case 4:
                    accountsHighestBalances(userAccounts);
                    break;
                case 5:
                    failedTransaction();
                    break;
                case 6:
                    c =false;
                    break;
                default:
                    System.exit(1);
            }
        }
    }

    public static void showBankSheet(String filePath) {
        try (
                FileReader fr=new FileReader(filePath);
                BufferedReader br=new BufferedReader(fr)
        ){
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line+"\n");
            }
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void readListAndWriteToFile(ArrayList<UserAccount> accounts, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (UserAccount account : accounts) {
                bw.write(account.toString());
                bw.newLine();
            }
            System.out.println("Accounts written to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<UserAccount> readFileAndSaveToList(String filePath) {
        ArrayList<UserAccount> userAccounts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                String accountNumber = parts[0].split("=")[1];
                String holder = parts[1].split("=")[1];
                double balance = Double.parseDouble(parts[2].split("=")[1]);
                UserAccount account = new UserAccount(accountNumber, holder, balance);
                userAccounts.add(account);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userAccounts;
    }

    public static void accountsHighestBalances(ArrayList<UserAccount> userAccounts){
        List<UserAccount> sortedList = userAccounts.stream()
                .sorted(Comparator.comparing(UserAccount::getAccountBalance).reversed())
                .limit(6).collect(Collectors.toList());
        System.out.println("Top 6 accounts with the highest balance:");
        sortedList.forEach(account ->
                System.out.println("Account Holder: " + account.getAccountHolder() +
                        ", Balance: " + account.getAccountBalance()));
    }
    public static void journal(ArrayList<UserAccount> users, String path ){
        double amount;
        boolean c=true;

        String userAccountNumber;
        UserAccount user;
        Scanner sc = new Scanner(System.in);
        while (c) {
            System.out.println("Enter operation no!");
            System.out.println("1-credit ");
            System.out.println("2-debit");
            System.out.println("3-exit");

            byte transactionType = sc.nextByte();
            switch (transactionType) {
                case 1:
                    System.out.println("Enter amount: ");
                    amount = sc.nextDouble();

                    if (amount < 0) {
                        try {
                            throw new UserAccountException(UserErrorCode.NegativeBalance);
                        } catch (UserAccountException e) {
                            failedTransaction++;
                            System.out.println(e.getMessage());
                            journal(users, path);
                        }
                    }

                    System.out.println("Enter account number: ");
                    userAccountNumber = sc.next();
                    user = new UserAccount();
                    user = user.findUserByAccountNumber(userAccountNumber, users);
                    System.out.println("Holder: " + user.getAccountHolder());
                    if (!user.getAccountNumber().equals(userAccountNumber)) {
                        System.out.println("Account Number not found");
                        journal(users, path);
                    }
                    users = updateCredit(amount, userAccountNumber, path);
                    readListAndWriteToFile(users, path);
                    transactionsCount++;
                    BankTps(users, path);

                    break;
                case 2:
                    System.out.println("Enter amount: ");
                    amount = sc.nextDouble();

                    if (amount < 0) {
                        try {
                            throw new UserAccountException(UserErrorCode.NegativeBalance);
                        } catch (UserAccountException e) {
                            failedTransaction++;
                            System.out.println(e.getMessage());
                            journal(users, path);
                        }
                    }

                    System.out.println("Enter account number: ");
                    userAccountNumber = sc.next();
                    user = new UserAccount();
                    user = user.findUserByAccountNumber(userAccountNumber, users);
                    System.out.println("Holder: " + user.getAccountHolder());

                    if (!user.getAccountNumber().equals(userAccountNumber)) {
                        System.out.println("Account Number not found");
                        journal(users, path);
                    }
                    try {
                        users = updateDebit(amount, userAccountNumber, path);
                    } catch (UserAccountException e) {
                        System.out.println(e.getMessage());
                    }
                    readListAndWriteToFile(users, path);
                    transactionsCount++;
                    BankTps(users, path);

                    break;
                case 3:
                    c = false;
                    BankTps(users, path);
                    break;
                default:
                    System.out.println("Invalid Operation");
            }
        }
    }

    public static void failedTransaction(){
        System.out.println("Number of failed Transactions: "+failedTransaction);
    }
    public static void successTransaction(){
        System.out.println("Number of failed Transactions: "+transactionsCount);
    }


    public static ArrayList<UserAccount> updateCredit(double amount, String userAccountNumber, String filePath) {
        ArrayList<UserAccount> accountList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming each line in the file corresponds to an Account
                String[] parts = line.split(", ");

                // Extracting data and creating an Account object
                String accountNumber = parts[0].split("=")[1];
                String holder = parts[1].split("=")[1];
                double balance = Double.parseDouble(parts[2].split("=")[1]);

                if (userAccountNumber.equals(holder)) {
                    balance+=amount;
                    System.out.println("you deposit " + amount + " successfully");
                }
                UserAccount account = new UserAccount(accountNumber, holder, balance);
                accountList.add(account);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accountList;
    }

    public  static ArrayList<UserAccount> updateDebit(double amount, String userAccountNumber, String filePath) throws UserAccountException {

        ArrayList<UserAccount> accountList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming each line in the file corresponds to an Account
                String[] parts = line.split(", ");

                // Extracting data and creating an Account object
                String accountNumber = parts[0].split("=")[1];
                String holder = parts[1].split("=")[1];
                double balance = Double.parseDouble(parts[2].split("=")[1]);

                if (amount>balance)
                    throw new UserAccountException(UserErrorCode.AmountNotAllowed);

                if (userAccountNumber.equals(holder)) {
                    balance-=amount;
                    System.out.println("you debit " + amount + " successfully");
                }
                UserAccount account = new UserAccount(accountNumber, holder, balance);
                accountList.add(account);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accountList;
    }
}