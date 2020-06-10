package loanbroker.gateway;

import loanbroker.model.ClientCreditHistory;
import loanclient.model.LoanRequest;

public interface LoanRequestListener {
    void onRequestReceived(LoanRequest loanRequest, ClientCreditHistory clientCreditHistory);
}
