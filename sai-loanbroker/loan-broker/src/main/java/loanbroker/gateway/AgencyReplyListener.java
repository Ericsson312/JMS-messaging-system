package loanbroker.gateway;

import loanbroker.model.Agency;

public interface AgencyReplyListener {
    void onReplyReceived(Agency agency);
}
