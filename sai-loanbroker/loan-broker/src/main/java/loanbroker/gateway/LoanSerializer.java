package loanbroker.gateway;

import com.google.gson.Gson;
import loanclient.model.LoanReply;
import loanclient.model.LoanRequest;

public class LoanSerializer {
    private Gson gson;

    public LoanSerializer() {
        this.gson = new Gson();
    }

    public String serializeLoanRequest(LoanRequest request) {
        return gson.toJson(request);
    }

    public String serializeLoanReply(LoanReply reply) {
        return gson.toJson(reply);
    }

    public LoanRequest deserializeLoadRequest(String request) {
        return gson.fromJson(request, LoanRequest.class);
    }

    public LoanReply deserializeLoadReply(String reply) {
        return gson.fromJson(reply, LoanReply.class);
    }
}
