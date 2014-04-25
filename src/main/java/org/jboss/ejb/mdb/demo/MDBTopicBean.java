/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package org.jboss.ejb.mdb.demo;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * MDB to test JMS.
 *
 * @author OlinaRuan, TCSASSEMBLER
 * @version 1.0
 * @since 1.0
 */
@MessageDriven(name = "MDBTopicEJB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/testTopic")
        })
public class MDBTopicBean implements MessageListener {

    /**
     * Default constructor.
     */
    public MDBTopicBean() {
    }

    /**
     * Handling of message.
     * @param message JMS message.
     */
    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                System.out.println(textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Unknown message: " + message);
        }
    }
}
