package bank.gateway;

import bank.model.BankInterestRequest;

public interface BankRequestListener {
    void onRequestReceived(BankInterestRequest bankInterestRequest);
}
