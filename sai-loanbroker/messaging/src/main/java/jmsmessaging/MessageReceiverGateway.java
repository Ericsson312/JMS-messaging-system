package jmsmessaging;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class MessageReceiverGateway {
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;

    public MessageReceiverGateway(String queueName) {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            connectionFactory.setTrustAllPackages(true);
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(queueName);
            consumer = session.createConsumer(destination);
        } catch (JMSException exc) {
            exc.printStackTrace();
        }
    }

    public void setListener(MessageListener listener) {
        try {
            consumer.setMessageListener(listener);
        } catch (JMSException exc) {
            exc.printStackTrace();
        }
    }

    public Destination getDestination() {
        return destination;
    }

    public void openConnection() {
        try {
            if (connection != null)
                connection.start();
        } catch (JMSException exc) {
            exc.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (consumer != null)
                consumer.close();

            if (session != null)
                session.close();

            if (connection != null)
                connection.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
