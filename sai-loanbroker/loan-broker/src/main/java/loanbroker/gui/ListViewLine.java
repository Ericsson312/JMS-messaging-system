package loanbroker.gui;

import loanbroker.model.ClientCreditHistory;
import loanclient.model.LoanReply;
import loanclient.model.LoanRequest;

public class ListViewLine {

    private LoanRequest loanRequest;
    private LoanReply loanReply;
    private ClientCreditHistory clientCreditHistory;

    public ListViewLine(LoanRequest loanRequest) {
        setLoanRequest(loanRequest);
        setLoanReply(null);
    }

    public LoanRequest getLoanRequest() {
        return loanRequest;
    }

    private void setLoanRequest(LoanRequest loanRequest) {
        this.loanRequest = loanRequest;
    }


    public void setLoanReply(LoanReply loanReply) {
        this.loanReply = loanReply;
    }

    public void setClientCreditHistory(ClientCreditHistory clientCreditHistory) {
        this.clientCreditHistory = clientCreditHistory;
    }


    /**
     * This method defines how one line is shown in the ListViewLine.
     * @return
     *  a) if BankInterestReply is null, then this item will be shown as "loanRequest.toString ---> waiting for loan reply..."
     *  b) if BankInterestReply is not null, then this item will be shown as "loanRequest.toString ---> loanReply.toString"
     */
    @Override
    public String toString() {
        return loanRequest.toString() + " " + ((clientCreditHistory != null) ? clientCreditHistory.toString() : "") + "  --->  " + ((loanReply != null) ? loanReply.toString() : "waiting for reply...");
    }

}
