package loanclient.gateway;

import jmsmessaging.MessageReceiverGateway;
import jmsmessaging.MessageSenderGateway;
import loanclient.model.LoanReply;
import loanclient.model.LoanRequest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;

public class LoanBrokerAppGateway {
    private LoanSerializer loanSerializer;
    private LoanReplyListener loanReplyListener;
    private MessageReceiverGateway messageReceiverGateway;
    private MessageSenderGateway messageSenderGateway;
    private List<LoanRequest> requests = new ArrayList<>();
    private LoanRequest loanRequest = null;

    public LoanBrokerAppGateway(String receiverQueueName, String senderQueueName) {
        loanSerializer = new LoanSerializer();
        messageReceiverGateway = new MessageReceiverGateway(receiverQueueName);
        messageSenderGateway = new MessageSenderGateway(senderQueueName);
        messageReceiverGateway.openConnection();
        messageSenderGateway.openConnection();

        messageReceiverGateway.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    LoanReply loanReply = loanSerializer.deserializeLoadReply(((TextMessage)message).getText());

                    for (LoanRequest rst : requests) {
                        if (rst.getId().equals(loanReply.getId())) {
                            loanRequest = rst;
                            break;
                        }
                    }

                    if (loanRequest != null) {
                        loanReplyListener.onReplyReceived(loanRequest, loanReply);
                    }
                } catch (JMSException exc) {
                    exc.printStackTrace();
                }
            }
        });
    }

    public void applyForLoan(LoanRequest loanRequest) throws JMSException {
        requests.add(loanRequest);
        String serializedMessage = loanSerializer.serializeLoanRequest(loanRequest);
        Message message = messageSenderGateway.createTextMessage(serializedMessage);
        message.setJMSReplyTo(messageReceiverGateway.getDestination());
        messageSenderGateway.send(message);
    }

    public void setReplyListener(LoanReplyListener loanReplyListener) {
        this.loanReplyListener = loanReplyListener;
    }

}
