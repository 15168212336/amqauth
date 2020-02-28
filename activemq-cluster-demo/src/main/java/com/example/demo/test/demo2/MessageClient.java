package com.example.demo.test.demo2;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.Connection;
import javax.jms.Destination;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MessageClient {

    @Autowired
    JmsTemplate jmsTemplate;

    @Test
    public void sendTest() {
        //        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//        JmsTemplate jmsTemplate = new JmsTemplate();
        final Destination destination = new ActiveMQQueue("test_2/device_rule_1/thing");
//        final MessageSender messageSender = new MessageSender(destination, jmsTemplate);
        for (int i = 0; i < 10000; i++) {
            // TODO Auto-generated method stub
            try {
                Thread.sleep(1000);
                jmsTemplate.convertAndSend(destination,"ActiveMQ "+i);
//                messageSender.sendMessageByTxt("ActiveMQ "+i);
                System.out.println("发送消息： ActiveMQ "+i);

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
