package org.jboss.ejb.mdb.demo;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class QueueClientExample {
    public static String destination = "queue/testQueue";

    static boolean end = false;

    public static void main(String[] args) {
            startConsumer();
            QueueClientExample.runExample();
    }

    public static void startConsumer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Context ctx = getInitialContext();
                    ConnectionFactory connectionFactory = (ConnectionFactory) ctx.lookup("/ConnectionFactory");
                    Connection connection = connectionFactory.createConnection();
                    Queue queue = (Queue)ctx.lookup(destination);
                    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                    MessageConsumer messageConsumer = session.createConsumer(queue);
                    connection.start();

                    while (!end) {
                        Message message = messageConsumer.receive();
                        if (message instanceof TextMessage) {
                            System.out.println("Local: " + ((TextMessage) message).getText());
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        System.out.println("Local Consumer Started!");
    }

    public static void runExample() {
        Context context = null;
        ConnectionFactory connectionFactory = null;
        Connection connection = null;

        try {
            context = getInitialContext();
            connectionFactory = (ConnectionFactory) context.lookup("/ConnectionFactory");
            Queue queue = (Queue) context.lookup(destination);
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer publisher = session.createProducer(queue);
            MessageConsumer subscriber = session.createConsumer(queue);
            connection.start();

            TextMessage textMessage = session.createTextMessage("Hello JMS!");
            publisher.send(textMessage);

            for(int i=0; i < 1000;) {
                textMessage = session.createTextMessage("Test " + ++i);
                publisher.send(textMessage);
            }



            System.out.println("Message Sent!");


        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            if (null != context) {
                try {
                    context.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }

            if (null != connection) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private static Context getInitialContext() throws Exception {
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        properties.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
        properties.put(Context.PROVIDER_URL, "jnp://localhost:1099");
        return new InitialContext(properties);
    }

}
