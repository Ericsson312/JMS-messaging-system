package loanbroker.gateway;

import bank.model.BankInterestReply;
import bank.model.BankInterestRequest;

public interface BankReplyListener {
    void onReplyReceived(BankInterestReply bankInterestReply);
}
