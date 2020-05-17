package loanbroker.gui;

import bank.model.BankInterestReply;
import bank.model.BankInterestRequest;
import loanclient.model.LoanReply;
import loanclient.model.LoanRequest;
import loanbroker.gateway.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URL;
import java.util.*;

public class LoanBrokerController implements Initializable {
    final Logger logger = LoggerFactory.getLogger(getClass());
    @FXML
    private ListView<ListViewLine> lvLoanRequestReply;

    private LoanClientAppGateway loanClientAppGateway;
    private BankAppGateway bankAppGateway;

    public LoanBrokerController(){
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loanClientAppGateway = new LoanClientAppGateway("requestLoanClientQueue", null);
            bankAppGateway = new BankAppGateway("ingRequestQueue", "bankInterestReplyQueue");

            loanClientAppGateway.setRequestListener(new LoanRequestListener() {
                @Override
                public void onRequestReceived(LoanRequest loanRequest) {
                    BankInterestRequest bankInterestRequest = new BankInterestRequest(loanRequest.getId(), loanRequest.getAmount(), loanRequest.getTime());
                    try {
                        bankAppGateway.sendBankInterestRequest(bankInterestRequest);
                    } catch (Exception exc) {
                        logger.info("Error while sending bank interest request: " + exc.getMessage());
                    }
                }
            });

            bankAppGateway.setReplyListener(new BankReplyListener() {
                @Override
                public void onReplyReceived(BankInterestReply bankInterestReply) {
                    LoanReply loanReply = new LoanReply(bankInterestReply.getId(), bankInterestReply.getInterest(), bankInterestReply.getBankId());
                    try {
                        loanClientAppGateway.sendLoanReply(loanReply);
                    } catch (Exception exc) {
                        logger.info("Error while sending bank interest request: " + exc.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
        }
    }
}
