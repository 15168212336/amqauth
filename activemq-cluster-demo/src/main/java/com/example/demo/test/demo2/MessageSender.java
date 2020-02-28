package com.example.demo.test.demo2;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class MessageSender {
    private Destination destination;
    private JmsTemplate jmsTemplate;
    MessageSender(Destination destination,JmsTemplate jmsTemplate) {
        this.destination = destination;
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(final String txt)
    {

        jmsTemplate.send(destination,new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                Message message = session.createTextMessage(txt);
                return message;
            }
        });
    }
    public void sendMessageByTxt(String tx)
    {
        jmsTemplate.setDefaultDestination(destination);
        jmsTemplate.convertAndSend(tx);

    }
}
