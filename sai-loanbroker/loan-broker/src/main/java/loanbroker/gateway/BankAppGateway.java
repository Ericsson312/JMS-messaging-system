package loanbroker.gateway;

import bank.model.BankInterestReply;
import bank.model.BankInterestRequest;
import jmsmessaging.MessageReceiverGateway;
import jmsmessaging.MessageSenderGateway;
import loanbroker.model.BankReplyAggregator;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import javax.jms.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static jmsmessaging.Constants.*;

/*
 * receive BankInterestRequest and send BankInterestReply
 */
public class BankAppGateway {
    private BankSerializer bankSerializer;
    private BankReplyListener bankReplyListener;
    private MessageReceiverGateway messageReceiverGateway;
    private MessageSenderGateway messageSenderGatewayING;
    private MessageSenderGateway messageSenderGatewayABN;
    private MessageSenderGateway messageSenderGatewayRABO;
    private List<BankReplyAggregator> bankReplyAggregators;
    private int aggregationId;
    private int recipientCounter;

    // IGN, bankInterestReplyQueue
    public BankAppGateway(String receiverQueueName, String senderQueueName) {
        bankSerializer = new BankSerializer();
        messageReceiverGateway = new MessageReceiverGateway(receiverQueueName);
        messageSenderGatewayING = new MessageSenderGateway(REQUEST_BANK_QUEUE_IGN);
        messageSenderGatewayABN = new MessageSenderGateway(REQUEST_BANK_QUEUE_ABN_AMRO);
        messageSenderGatewayRABO = new MessageSenderGateway(REQUEST_BANK_QUEUE_RABO_BANK);
        messageReceiverGateway.openConnection();
        messageSenderGatewayING.openConnection();
        messageSenderGatewayABN.openConnection();
        messageSenderGatewayRABO.openConnection();
        bankReplyAggregators = new ArrayList<>();
        aggregationId = 0;
        recipientCounter = 0;

        // receive response message from bank
        messageReceiverGateway.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    // get aggregation id
                    int bankAggregationId = message.getIntProperty("aggregationId");
                    // deserialize bank reply
                    BankInterestReply interestReply = bankSerializer.deserializeBankInterestReply(((TextMessage)message).getText());

                    for (BankReplyAggregator aggregator: bankReplyAggregators) {
                        if (aggregator.getAggregationId() == bankAggregationId) {
                            aggregator.setRecipientNumber(aggregator.getRecipientNumber() - 1);
                            if (aggregator.getBankReply() == null) {
                                aggregator.setBankReply(interestReply);
                            } else {

                                if (aggregator.getBankReply().getInterest() == -1) {
                                    aggregator.setBankReply(interestReply);
                                } else {
                                    if (aggregator.getBankReply().getInterest() >= interestReply.getInterest() && interestReply.getInterest() != -1) {
                                        aggregator.setBankReply(interestReply);
                                    }
                                }
                            }

                            if (aggregator.getRecipientNumber() == 0) {
                                // send bank reply to broker controller listener
                                if (aggregator.getBankReply().getInterest() == -1) {
                                    aggregator.getBankReply().setBankId("refused");
                                }
                                bankReplyListener.onReplyReceived(aggregator.getBankReply());
                            }
                        }
                    }
                } catch (JMSException exc) {
                    exc.printStackTrace();
                }
            }
        });
    }

    // send bank interest request to bank
    public void sendBankInterestRequest(BankInterestRequest bankInterestRequest) throws JMSException {
        String serializedMessage = bankSerializer.serializeBankInterestRequest(bankInterestRequest);

        Argument amount = new Argument("amount = " + bankInterestRequest.getAmount());
        Argument time = new Argument("time = " + bankInterestRequest.getTime());

        Expression ingExpression = new Expression(ING, amount, time);
        Expression abnExpression = new Expression(ABN_AMRO, amount, time);
        Expression raboExpression = new Expression(RABO_BANK, amount, time);

        aggregationId++;

        // send request to IGN bank
        if (ingExpression.calculate() == 1.0) {
            recipientCounter++;
            Message message = messageSenderGatewayING.createTextMessage(serializedMessage);
            message.setIntProperty("aggregationId", aggregationId);
            messageSenderGatewayING.send(message);
        }

        // send request to ABN ARMO bank
        if (abnExpression.calculate() == 1.0) {
            recipientCounter++;
            Message message = messageSenderGatewayABN.createTextMessage(serializedMessage);
            message.setIntProperty("aggregationId", aggregationId);
            messageSenderGatewayABN.send(message);
        }

        // send request to RABO bank
        if (raboExpression.calculate() == 1.0) {
            recipientCounter++;
            Message message = messageSenderGatewayRABO.createTextMessage(serializedMessage);
            message.setIntProperty("aggregationId", aggregationId);
            messageSenderGatewayRABO.send(message);
        }

        if (recipientCounter == 0) {
            bankReplyListener.onReplyReceived(new BankInterestReply(bankInterestRequest.getId(), -1, "Refused"));
        } else {
            bankReplyAggregators.add(new BankReplyAggregator(aggregationId, recipientCounter));
        }

        recipientCounter = 0;
    }

    public void setReplyListener(BankReplyListener bankReplyListener) {
        this.bankReplyListener = bankReplyListener;
    }
}
