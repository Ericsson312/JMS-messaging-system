package jmsmessaging;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

import static jmsmessaging.Constants.TCP_CONNECTION;

public class MessageSenderGateway {
    private Connection connection;
    private Session session;
    private Destination destination = null;
    private MessageProducer producer;

    public MessageSenderGateway(String queueName) {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(TCP_CONNECTION);
            connectionFactory.setTrustAllPackages(true);
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            if (queueName != null) {
                destination = session.createQueue(queueName);
            }

            producer = session.createProducer(destination);
        } catch (JMSException exc) {
            exc.printStackTrace();
        }
    }

    public Message createTextMessage(String body) throws JMSException{
        return session.createTextMessage(body);
    }

    public void send(Message msg) throws JMSException{
        producer.send(msg);
    }

    public void send(Destination destination, Message msg) throws JMSException{
        producer.send(destination, msg);
    }

    public Destination getDestination() {
        return destination;
    }

    public Session getSession() {
        return session;
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
            if (producer != null)
                producer.close();

            if (session != null)
                session.close();

            if (connection != null)
                connection.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
