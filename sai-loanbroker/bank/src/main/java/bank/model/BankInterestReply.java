package bank.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class stores information about the bank reply
 *  to a loan request of the specific client
 * 
 */
public class BankInterestReply implements Serializable {
    private String id;
    private double interest; // the loan interest
    private String bankId; // the unique quote Id
    
    public BankInterestReply() {
        this.id = "";
        this.interest = 0;
        this.bankId = "";
    }
    
    public BankInterestReply(String id, double interest, String quoteId) {
        this.id = id;
        this.interest = interest;
        this.bankId = quoteId;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getBankId() {
        return bankId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String toString() {
        return "quote=" + this.bankId + " interest=" + this.interest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankInterestReply that = (BankInterestReply) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
