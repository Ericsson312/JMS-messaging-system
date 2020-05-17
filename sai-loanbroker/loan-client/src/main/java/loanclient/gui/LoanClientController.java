package loanclient.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import loanclient.gateway.LoanBrokerAppGateway;
import loanclient.gateway.LoanReplyListener;
import loanclient.model.LoanReply;
import loanclient.model.LoanRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class LoanClientController implements Initializable {
    final Logger logger = LoggerFactory.getLogger(getClass());
    @FXML
    private TextField tfSsn;
    @FXML
    private TextField tfAmount;
    @FXML
    private TextField tfTime;
    @FXML
    private ListView<ListViewLine> lvLoanRequestReply;

    private LoanBrokerAppGateway appGateway;

    public LoanClientController(){
    }

    @FXML
    public void btnSendLoanRequestClicked(){
        // create BankInterestRequest
        int ssn = Integer.parseInt(tfSsn.getText());
        int amount = Integer.parseInt(tfAmount.getText());
        int time = Integer.parseInt(tfTime.getText());
        LoanRequest loanRequest = new LoanRequest(UUID.randomUUID().toString(), ssn, amount, time);

        // create ListViewLine line with the request and add it to lvLoanRequestReply
        ListViewLine listViewLine = new ListViewLine(loanRequest);

        this.lvLoanRequestReply.getItems().add(listViewLine);

        // @TODO: send the loanRequest here...
        try {
            appGateway.applyForLoan(loanRequest);
        } catch (Exception exc) {
            logger.info("Error while sending request: " + loanRequest + "\n" + exc.getMessage());
        }
        logger.info("Sent the loan request: " + loanRequest);
    }


    /**
     * This method returns the line of lvMessages which contains the given loan request.
     * @param request BankInterestRequest for which the line of lvMessages should be found and returned
     * @return The ListViewLine line of lvMessages which contains the given request
     */
    private ListViewLine getRequestReply(LoanRequest request) {
        for (int i = 0; i < lvLoanRequestReply.getItems().size(); i++) {
            ListViewLine rr =  lvLoanRequestReply.getItems().get(i);
            if (rr.getLoanRequest() != null && rr.getLoanRequest() == request) {
                return rr;
            }
        }

        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfSsn.setText("123456");
        tfAmount.setText("80000");
        tfTime.setText("30");

        appGateway = new LoanBrokerAppGateway("replyLoanClientQueue_" + UUID.randomUUID(), "requestLoanClientQueue");
        appGateway.setReplyListener(new LoanReplyListener() {
            @Override
            public void onReplyReceived(LoanRequest loanRequest, LoanReply loanReply) {
                ListViewLine listLine = getRequestReply(loanRequest);
                if (listLine != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                listLine.setLoanReply(loanReply);
                                lvLoanRequestReply.refresh();
                            } catch (Exception e) {
                                logger.info("Error while updating Requester UI: " + e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }
}
