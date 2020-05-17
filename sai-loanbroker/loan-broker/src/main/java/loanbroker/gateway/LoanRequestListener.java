package loanbroker.gateway;

import loanclient.model.LoanReply;
import loanclient.model.LoanRequest;

public interface LoanRequestListener {
    void onRequestReceived(LoanRequest loanRequest);
}
