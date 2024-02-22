package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)   {
        Scanner scanner=new Scanner(System.in);
        String filePath = "accounts.txt"; // Default file path
        ArrayList<UserAccount> accounts = Operations.readFileAndSaveToList(filePath);
//        Operations.readListAndWriteToFile(accounts, filePath);
        System.out.println("Welcome ");

        int incorrectEntry =0;
        boolean flag=true;
        ArrayList<UserAccount> userAccounts = Operations.readFileAndSaveToList(filePath);

        while (flag){
            System.out.println("1- Bank Entry");
            System.out.println("2- Bank Tps");
            System.out.println("Please choose  your option ");
            String option=scanner.next();
            if (option.equals("1")) {
                Operations.BankEntry(filePath,userAccounts);
            }
            else if (option.equals("2")){
                Operations.BankTps(userAccounts, filePath);
            } else if (option.equalsIgnoreCase("exit")) {
                flag=false;
            } else {
                incorrectEntry++;
                if (incorrectEntry ==3) {
                    System.out.println("you input 3 times incorrect option ");
                    flag=false;
                }
            }
        }
     }
}