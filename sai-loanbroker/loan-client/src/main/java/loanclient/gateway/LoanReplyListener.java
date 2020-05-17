package loanclient.gateway;

import loanclient.model.LoanReply;
import loanclient.model.LoanRequest;

public interface LoanReplyListener {
    void onReplyReceived(LoanRequest loanRequest, LoanReply loanReply);
}
