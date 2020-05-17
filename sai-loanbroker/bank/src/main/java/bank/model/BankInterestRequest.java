package bank.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * This class stores all information about an request from a bank to offer
 * a loan to a specific client.
 */
@SuppressWarnings("unused")
public class BankInterestRequest implements Serializable {

    private String id;
    private int amount; // the requested loan amount
    private int time; // the requested loan period

    public BankInterestRequest() {
        super();
        this.amount = 0;
        this.time = 0;
        this.id = "";
    }

    public BankInterestRequest(String id, int amount, int time) {
        super();
        this.id = id;
        this.amount = amount;
        this.time = time;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return " amount=" + amount + " time=" + time ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankInterestRequest that = (BankInterestRequest) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
