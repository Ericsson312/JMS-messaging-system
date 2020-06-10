package loanbroker.model;

import bank.model.BankInterestReply;

public class BankReplyAggregator {
    private int aggregationId;
    private int recipientNumber;
    private BankInterestReply bankReply;

    public BankReplyAggregator(int aggregationId, int recipientNumber) {
        this.aggregationId = aggregationId;
        this.recipientNumber = recipientNumber;
        this.bankReply = null;
    }

    public int getAggregationId() {
        return aggregationId;
    }

    public void setAggregationId(int aggregationId) {
        this.aggregationId = aggregationId;
    }

    public int getRecipientNumber() {
        return recipientNumber;
    }

    public void setRecipientNumber(int recipientNumber) {
        this.recipientNumber = recipientNumber;
    }

    public BankInterestReply getBankReply() {
        return bankReply;
    }

    public void setBankReply(BankInterestReply bankReply) {
        this.bankReply = bankReply;
    }
}
