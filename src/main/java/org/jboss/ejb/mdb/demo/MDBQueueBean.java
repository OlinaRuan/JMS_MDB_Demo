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
 * MDB for Queue.
 * @author OlinaRuan, TCSASSEMBLER
 * @version 1.0
 * @since 1.0
 */
@MessageDriven(name = "MDBQueueEJB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/testQueue") })
public class MDBQueueBean implements MessageListener {
    /**
     * Default constructor.
     */
    public MDBQueueBean() {
    }

    /**
     * Handling of message.
     *
     * @param message Message to handle.
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
