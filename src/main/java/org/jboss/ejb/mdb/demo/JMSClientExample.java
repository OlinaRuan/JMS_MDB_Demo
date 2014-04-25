/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package org.jboss.ejb.mdb.demo;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Client demo.
 *
 * @author OlinaRuan, TCSASSEMBLER
 * @version 1.0
 * @since 1.0
 */
public final class JMSClientExample {

    /**
     * Queue JNDI.
     */
    private static String queueDestination = "queue/testQueue";

    /**
     * Topic JNDI.
     */
    private static String topicDestination = "topic/testTopic";

    /**
     * Private constructor to stop instantiation.
     */
    private JMSClientExample() {
    }

    /**
     * Main method.
     * @param args arguments.
     * @throws Exception If there is an error.
     */
    public static void main(String[] args) throws Exception {
        //startConsumer();
        JMSClientExample.runQueueExample();
        JMSClientExample.runTopicExample();

    }

    /**
     * Publish a message to queue.
     * @throws Exception If there is an error.
     */
    public static void runQueueExample() throws Exception {
        Context context = null;
        ConnectionFactory connectionFactory = null;
        Connection connection = null;

        try {
            context = getInitialContext();
            connectionFactory = (ConnectionFactory) context.lookup("/ConnectionFactory");
            Queue queue = (Queue) context.lookup(queueDestination);
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer publisher = session.createProducer(queue);
            MessageConsumer subscriber = session.createConsumer(queue);
            connection.start();

            TextMessage textMessage = session.createTextMessage("Hello Queue JMS!");
            publisher.send(textMessage);
            System.out.println("Queue Message Sent!");
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

    /**
     * Publish a message to topic.
     * @throws Exception If there is an error.
     */
    public static void runTopicExample() throws Exception {
        Context context = null;
        ConnectionFactory connectionFactory = null;
        Connection connection = null;

        try {
            context = getInitialContext();
            connectionFactory = (ConnectionFactory) context.lookup("/ConnectionFactory");
            Topic topic = (Topic) context.lookup(topicDestination);
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer publisher = session.createProducer(topic);
            connection.start();

            TextMessage textMessage = session.createTextMessage("Hello Topic JMS!");
            publisher.send(textMessage);
            System.out.println("Topic Message Sent!");
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


    /**
     * Construct a {@link Context} instance.
     * @return Instance of {@link Context}.
     * @throws Exception If there is an error.
     */
    private static Context getInitialContext() throws Exception {
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        properties.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
        properties.put(Context.PROVIDER_URL, "jnp://localhost:1099");
        return new InitialContext(properties);
    }

}
