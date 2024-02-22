package org.example;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserAccount {
    private String accountHolder;
    private String accountNumber;
    private double accountBalance;


    public UserAccount(String accountHolder, String accountNumber, double accountBalance) {
        this.accountHolder = accountHolder;
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
    }
    public UserAccount() {

    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "Account=" + accountHolder +
                ", Holder=" + accountNumber +
                ", Balance=" + accountBalance;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount)) return false;
        UserAccount that = (UserAccount) o;
        return Double.compare(that.getAccountBalance(), getAccountBalance()) == 0 && Objects.equals(getAccountHolder(), that.getAccountHolder()) && Objects.equals(getAccountNumber(), that.getAccountNumber());
    }

    public boolean isValidName() throws UserAccountException {
        if (accountHolder.matches(".*\\d.*"))
            throw new UserAccountException(UserErrorCode.UserNameContainsNumbers);
        if (accountHolder.trim().length()<4)
            throw new UserAccountException(UserErrorCode.UserNameTooShort);
        return true;
    }

    public  boolean isValidAccountNumber() throws UserAccountException {
        if (!(accountNumber).matches("\\d+"))
            throw new UserAccountException(UserErrorCode.Invalidaccno);
        if (accountNumber.length()!=8)
            throw new UserAccountException(UserErrorCode.UserAccountNumberTooShort);
        if (accountNumber.startsWith("-"))
            throw new UserAccountException(UserErrorCode.InvalidUserAccountNumber);
        return true;
    }

    public boolean isValidBalance() throws UserAccountException {
        if (accountBalance<0)
            throw new UserAccountException(UserErrorCode.NegativeBalance);
        return true;
    }

    public UserAccount findUserByAccountNumber(String accountNum, List<UserAccount> users){
        UserAccount user = new UserAccount();
        user.setAccountNumber(accountNum);
        try {
            user.isValidAccountNumber();
            for (UserAccount u:
                    users) {
                if (u.getAccountNumber().equals(accountNum)){
                    user = u;
                    break;
                } else {
                    user = null;
                }
            }
        } catch (UserAccountException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

}
