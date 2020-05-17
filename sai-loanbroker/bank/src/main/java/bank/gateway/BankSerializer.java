package bank.gateway;

import bank.model.BankInterestReply;
import bank.model.BankInterestRequest;
import com.google.gson.Gson;

public class BankSerializer {
    private Gson gson;

    public BankSerializer() {
        this.gson = new Gson();
    }

    public String serializeBankInterestRequest(BankInterestRequest request) {
        return gson.toJson(request);
    }

    public String serializeBankInterestReply(BankInterestReply reply) {
        return gson.toJson(reply);
    }

    public BankInterestRequest deserializeBankInterestRequest(String request) {
        return gson.fromJson(request, BankInterestRequest.class);
    }

    public BankInterestReply deserializeBankInterestReply(String reply) {
        return gson.fromJson(reply, BankInterestReply.class);
    }
}
