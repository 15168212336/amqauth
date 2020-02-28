package com.example.demo.test.demo1;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQXASslConnectionFactory;
import org.apache.activemq.pool.PooledConnection;
import org.apache.activemq.pool.PooledConnectionFactory;

import javax.jms.*;

public class ActivemqReceiver {
    public static void main(String[] args) {
        try {
//            ActiveMQXASslConnectionFactory activeMQXASslConnectionFactory = new ActiveMQXASslConnectionFactory("failover:(ssl://duhp.soyea.com.cn:61616)");
//            activeMQXASslConnectionFactory.setUserName("admin");
//            activeMQXASslConnectionFactory.setPassword("123456");
//            activeMQXASslConnectionFactory.setTrustStore("server.jks");
//            activeMQXASslConnectionFactory.setTrustStorePassword("123456");
            // 第一步：建立ConnectionFactory工厂对象，需要填入用户名、密码、以及要连接的地址，均使用默认即可，默认端口为"tcp://localhost:61616"
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "123456",
                    "failover:(tcp://127.0.0.1:61616,tcp://127.0.0.1:61617,tcp://127.0.0.1:61618)?Randomize=false");
//            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "123456",
//                    "failover:(tcp://192.9.25.197:61616,tcp://192.9.25.197:61617,tcp://192.9.25.197:61618)?Randomize=false");
            // 第二步：通过ConnectionFactory工厂对象我们创建一个Connection连接，并且调用Connection的start方法开启连接，Connection默认是关闭的。
//            Connection connection = activeMQXASslConnectionFactory.createXAConnection();

//            PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(activeMQXASslConnectionFactory);
//            pooledConnectionFactory.setMaximumActiveSessionPerConnection(200);
//            pooledConnectionFactory.setIdleTimeout(120);
//            pooledConnectionFactory.setMaxConnections(5);
//            pooledConnectionFactory.setBlockIfSessionPoolIsFull(true);
//            PooledConnection connection = (PooledConnection) pooledConnectionFactory.createConnection();
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // 第三步：通过Connection对象创建Session会话（上下文环境对象），用于接收消息，参数配置1为是否启用是事务，参数配置2为签收模式，一般我们设置自动签收。
            Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);

            // 第四步：通过Session创建Destination对象，指的是一个客户端用来指定生产消息目标和消费消息来源的对象，在PTP模式中，Destination被称作Queue即队列；在Pub/Sub模式，Destination被称作Topic即主题。在程序中可以使用多个Queue和Topic。
//            Destination destination = session.createQueue("test_2/device_rule_1/thing/service/property/set");
//            Destination destination = session.createTopic("*");
            Destination destination = session.createQueue("test_queue");
//            Destination destination = session.createQueue("shadow/get/test_2/device_rule_1");
            // 第五步：通过Session创建MessageConsumer
            MessageConsumer consumer = session.createConsumer(destination);
            Thread.sleep(30000);
            while (true) {
//                Message msg = consumer.receive();
                TextMessage msg = (TextMessage) consumer.receive();
                if (msg == null)
                    break;
                System.out.println("收到的内容：" + msg.getText());
//                System.out.println("收到的内容：" + msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
