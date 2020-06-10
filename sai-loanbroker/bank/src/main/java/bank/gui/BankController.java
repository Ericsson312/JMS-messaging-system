package bank.gui;

import bank.gateway.BankRequestListener;
import bank.gateway.LoanBrokerAppGateway;
import bank.model.BankInterestReply;
import bank.model.BankInterestRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URL;
import java.util.ResourceBundle;

class BankController implements Initializable {
    private final String bankId;
    private final String queueName;

    final Logger logger = LoggerFactory.getLogger(getClass());
    @FXML
    public ListView<ListViewLine> lvBankRequestReply;
    @FXML
    public TextField tfInterest;

    private LoanBrokerAppGateway appGateway;

    public BankController(String queueName, String bankId){
        this.bankId = bankId;
        this.queueName = queueName;
        LoggerFactory.getLogger(getClass()).info("Created BankController with arguments [queueName="+queueName+"] and [bankId="+bankId+"]");
    }

    @FXML
    public void btnSendBankInterestReplyClicked(){
        double interest = Double.parseDouble(tfInterest.getText());

        ListViewLine listViewLine = lvBankRequestReply.getSelectionModel().getSelectedItem();
        if (listViewLine!= null){
            BankInterestReply bankInterestReply = new BankInterestReply(listViewLine.getBankInterestRequest().getId(), interest, bankId);
            try {
                appGateway.sendBankInterestReply(bankInterestReply);
            } catch (Exception exc) {
                logger.info("Error while sending request:" + exc.getMessage());
            }

            listViewLine.setBankInterestReply(bankInterestReply);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    lvBankRequestReply.refresh();
                }
            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // receive messages from - bankInterestReplyQueue
            // send messages to - queueName
            //appGateway = new LoanBrokerAppGateway("bankInterestReplyQueue", queueName);
            appGateway = new LoanBrokerAppGateway(queueName, "bankInterestReplyQueue");
            appGateway.setRequestListener(new BankRequestListener() {
                @Override
                public void onRequestReceived(BankInterestRequest bankInterestRequest) {
                    try {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                lvBankRequestReply.getItems().add(new ListViewLine(bankInterestRequest));
                                lvBankRequestReply.refresh();
                            }
                        });
                    } catch (Exception e) {
                        logger.info("Error while getting the reply message: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
        }
    }
}
