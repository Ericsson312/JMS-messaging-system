package bank.gui;

import bank.gateway.BankSerializer;
import bank.model.BankInterestReply;
import bank.model.BankInterestRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import jmsmessaging.MessageReceiverGateway;
import jmsmessaging.MessageSenderGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URL;
import java.util.ResourceBundle;

import javax.jms.*;

class BankController implements Initializable {
    private final String bankId;
    private final String queueName;

    final Logger logger = LoggerFactory.getLogger(getClass());
    @FXML
    public ListView<ListViewLine> lvBankRequestReply;
    @FXML
    public TextField tfInterest;

    private MessageReceiverGateway messageReceiverGateway;
    private MessageSenderGateway messageSenderGateway;
    private BankSerializer bankSerializer;

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
                String serializedMessage = bankSerializer.serializeBankInterestReply(bankInterestReply);
                Message message = messageSenderGateway.createTextMessage(serializedMessage);
                messageSenderGateway.send(message);
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
            bankSerializer = new BankSerializer();
            messageReceiverGateway = new MessageReceiverGateway("bankInterestReplyQueue");
            messageSenderGateway = new MessageSenderGateway(queueName);
            messageReceiverGateway.openConnection();
            messageSenderGateway.openConnection();

            messageReceiverGateway.setListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        BankInterestRequest loanRequest = bankSerializer.deserializeBankInterestRequest(((TextMessage)message).getText());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                lvBankRequestReply.getItems().add(new ListViewLine(loanRequest));
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
