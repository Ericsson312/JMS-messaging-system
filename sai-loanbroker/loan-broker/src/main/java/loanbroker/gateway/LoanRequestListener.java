package loanbroker.gateway;

import loanbroker.model.Agency;
import loanclient.model.LoanReply;
import loanclient.model.LoanRequest;

public interface LoanRequestListener {
    void onRequestReceived(LoanRequest loanRequest, Agency agencyData);
}
