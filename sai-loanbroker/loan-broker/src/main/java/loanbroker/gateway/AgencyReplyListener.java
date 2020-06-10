package loanbroker.gateway;

import loanbroker.model.ClientCreditHistory;

public interface AgencyReplyListener {
    void onReplyReceived(ClientCreditHistory clientCreditHistory);
}
