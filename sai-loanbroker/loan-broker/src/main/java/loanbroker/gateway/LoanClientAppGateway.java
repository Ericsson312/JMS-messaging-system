package loanbroker.gateway;

import jmsmessaging.MessageReceiverGateway;
import jmsmessaging.MessageSenderGateway;
import loanbroker.http.ApiClient;
import loanbroker.model.Agency;
import loanbroker.model.Archive;
import loanclient.model.LoanReply;
import loanclient.model.LoanRequest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;

/*
* receive LoanRequest and send LoanReply
 */
public class LoanClientAppGateway {
    private LoanSerializer loanSerializer;
    private LoanRequestListener loanRequestListener;
    private MessageReceiverGateway messageReceiverGateway;
    private MessageSenderGateway messageSenderGateway;
    private List<Message> clientMessages = new ArrayList<>();
    private ApiClient apiClient;
    private Archive archiveModel;

    public LoanClientAppGateway(String receiverQueueName, String senderQueueName) {
        loanSerializer = new LoanSerializer();
        messageReceiverGateway = new MessageReceiverGateway(receiverQueueName);
        messageSenderGateway = new MessageSenderGateway(senderQueueName);
        messageReceiverGateway.openConnection();
        messageSenderGateway.openConnection();
        apiClient = new ApiClient();

        messageReceiverGateway.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    clientMessages.add(message);
                    LoanRequest loanRequest = loanSerializer.deserializeLoadRequest(((TextMessage)message).getText());
                    apiClient.sendToAgency(loanRequest.getSsn());
                    apiClient.setAgencyReplyListener(new AgencyReplyListener() {
                        @Override
                        public void onReplyReceived(Agency agency) {
                            loanRequestListener.onRequestReceived(loanRequest, agency);
                        }
                    });
                } catch (JMSException exc) {
                    exc.printStackTrace();
                }
            }
        });
    }

    // send loan reply to client
    public void sendLoanReply(LoanReply loanReply) throws JMSException {
        // loop through all stored jms messages in order to find a proper recipient
        for (Message msg : clientMessages) {
            LoanRequest loanRequest = loanSerializer.deserializeLoadRequest(((TextMessage) msg).getText());
            if (loanRequest.getId().equals(loanReply.getId())) {
                if (loanReply.getInterest() >= 0) {
                    archiveModel = new Archive(loanRequest.getSsn(), loanRequest.getAmount(),
                            loanReply.getBankID(), loanReply.getInterest(), loanRequest.getTime());
                    apiClient.sendToArchive(archiveModel);
                }

                // serialize loan reply and send it back to the client
                String serializedMessage = loanSerializer.serializeLoanReply(loanReply);
                Message message = messageSenderGateway.createTextMessage(serializedMessage);
                message.setJMSReplyTo(msg.getJMSReplyTo());
                message.setJMSCorrelationID(msg.getJMSCorrelationID());
                messageSenderGateway.send(msg.getJMSReplyTo(), message);
                break;
            }
        }
    }

    public void setRequestListener(LoanRequestListener loanRequestListener) {
        this.loanRequestListener = loanRequestListener;
    }
}
