package org.example;
import java.util.*;

public class UserAccountException extends Exception {
    private UserErrorCode error;

    private  static HashMap<UserErrorCode,String> message=new HashMap<>();

    static  {
        message.put(UserErrorCode.UserNameContainsNumbers,"Invalid User Name");
        message.put(UserErrorCode.UserNameTooShort,"User Name should be at least 4 characters");
        message.put(UserErrorCode.UserAccountNumberTooShort,"User Account Number length should be 8");
        message.put(UserErrorCode.InvalidUserAccountNumber,"User Account Number should be positive");
        message.put(UserErrorCode.NegativeBalance, "Balance should be positive");
        message.put(UserErrorCode.AmountNotAllowed, "Amount must be less than balance");
        message.put(UserErrorCode.NegativeCredit, "Credit must be greater than zero");
        message.put(UserErrorCode.Invalidaccno, "account number must contain only numbers");


    }

    public UserAccountException(UserErrorCode error) {
        this.error = error;
    }

    @Override
    public String getMessage() {
        return message.get(error);
    }
}
