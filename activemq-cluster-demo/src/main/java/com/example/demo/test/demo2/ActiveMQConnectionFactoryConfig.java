package com.example.demo.test.demo2;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;

@Configuration
public class ActiveMQConnectionFactoryConfig {
    /**
     * 创建普通连接工厂
     * @return
     */
    @Bean(name = "jmsConnectionFactory")
    public ActiveMQConnectionFactory jmsConnectionFactory() {
        return new ActiveMQConnectionFactory("admin","123456","failover:(tcp://localhost:61616,tcp://localhost:61617,tcp://localhost:61618)");
    }

    @Bean(name = "jmsConnection")
    public Connection jmsConnection(){
        Connection connection = null;
        try {
           connection = jmsConnectionFactory().createConnection();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Bean(name = "jmsTemplate")
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(jmsConnectionFactory());
        jmsTemplate.setPubSubDomain(false);
        jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);
        return jmsTemplate;
    }
}
