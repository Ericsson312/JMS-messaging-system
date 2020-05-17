package loanbroker.gateway;

import bank.model.BankInterestReply;
import bank.model.BankInterestRequest;
import jmsmessaging.MessageReceiverGateway;
import jmsmessaging.MessageSenderGateway;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/*
 * receive BankInterestRequest and send BankInterestReply
 */
public class BankAppGateway {
    private BankSerializer bankSerializer;
    private BankReplyListener bankReplyListener;
    private MessageReceiverGateway messageReceiverGateway;
    private MessageSenderGateway messageSenderGateway;

    public BankAppGateway(String receiverQueueName, String senderQueueName) {
        bankSerializer = new BankSerializer();
        messageReceiverGateway = new MessageReceiverGateway(receiverQueueName);
        messageSenderGateway = new MessageSenderGateway(senderQueueName);
        messageReceiverGateway.openConnection();
        messageSenderGateway.openConnection();

        // receive response message from bank
        messageReceiverGateway.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    BankInterestReply interestReply = bankSerializer.deserializeBankInterestReply(((TextMessage)message).getText());
                    // send bank reply to broker controller listener
                    bankReplyListener.onReplyReceived(interestReply);
                } catch (JMSException exc) {
                    exc.printStackTrace();
                }
            }
        });
    }

    // send bank interest request to bank
    public void sendBankInterestRequest(BankInterestRequest bankInterestRequest) throws JMSException {
        String serializedMessage = bankSerializer.serializeBankInterestRequest(bankInterestRequest);
        Message message = messageSenderGateway.createTextMessage(serializedMessage);
        messageSenderGateway.send(message);
    }

    public void setReplyListener(BankReplyListener bankReplyListener) {
        this.bankReplyListener = bankReplyListener;
    }
}
