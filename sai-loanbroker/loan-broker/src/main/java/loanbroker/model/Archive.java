package loanbroker.model;

import loanclient.model.LoanReply;

import java.io.Serializable;
import java.util.Objects;

public class Archive implements Serializable {
    private int ssn; // unique client number
    private int amount; // the amount to borrow
    private String bankID; // the unique quote identification of the bank which makes the offer
    private double interest; // the interest that the bank offers for the requested loan
    private int time; // the time-span of the loan in years

    public Archive() {
        super();
        this.ssn = 0;
        this.amount = 0;
        this.bankID = "";
        this.interest = 0;
        this.time = 0;
    }
    public Archive(int ssn, int amount, String bankID, double interest, int time) {
        super();
        this.ssn = ssn;
        this.amount = amount;
        this.bankID = bankID;
        this.interest = interest;
        this.time = time;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getBankID() {
        return bankID;
    }

    public void setBankID(String bankId) {
        this.bankID = bankId;
    }

    public int getSsn() {
        return ssn;
    }

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
