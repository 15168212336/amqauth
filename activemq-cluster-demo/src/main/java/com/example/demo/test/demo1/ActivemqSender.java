package com.example.demo.test.demo1;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQXASslConnectionFactory;
import org.apache.activemq.pool.PooledConnection;
import org.apache.activemq.pool.PooledConnectionFactory;

import javax.jms.*;
import java.util.Date;

public class ActivemqSender {
    public static void main(String[] args) {
        try {
//            ActiveMQXASslConnectionFactory activeMQXASslConnectionFactory = new ActiveMQXASslConnectionFactory("failover:(ssl://duhp.soyea.com.cn:61616)");
//            activeMQXASslConnectionFactory.setUserName("admin");
//            activeMQXASslConnectionFactory.setPassword("123456");
//            activeMQXASslConnectionFactory.setTrustStore("server.jks");
//            activeMQXASslConnectionFactory.setTrustStorePassword("123456");
//            activeMQXASslConnectionFactory.setKeyStore("server.cer");
//            activeMQXASslConnectionFactory.setKeyStorePassword("123456");

            // 第一步：建立ConnectionFactory工厂对象，需要填入用户名、密码、以及要连接的地址，均使用默认即可，默认端口为"tcp://localhost:61616"
//            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "123456",
//                    "failover:(tcp://192.9.25.197:61616,tcp://192.9.25.197:61617,tcp://192.9.25.197:61618,tcp://192.9.25.197:61619,tcp://192.9.25.197:61620)?Randomize=false");
                        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("zouxiang", "123456",
                    "failover:(tcp://127.0.0.1:61616,tcp://127.0.0.1:61617,tcp://127.0.0.1:61618)?Randomize=false");
//            QueueConnectionFactory queueConnectionFactory = new ActiveMQConnectionFactory("admin", "123456",
//                    "failover:(tcp://localhost:61616,tcp://localhost:61617,tcp://localhost:61618)?Randomize=false");
            // 第二步：通过ConnectionFactory工厂对象我们创建一个Connection连接，并且调用Connection的start方法开启连接，Connection默认是关闭的。

//            Connection connection = activeMQXASslConnectionFactory.createXAConnection();
            Connection connection = connectionFactory.createConnection();

//            PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(activeMQXASslConnectionFactory);
//            pooledConnectionFactory.setMaximumActiveSessionPerConnection(200);
//            pooledConnectionFactory.setIdleTimeout(120);
//            pooledConnectionFactory.setMaxConnections(5);
//            pooledConnectionFactory.setBlockIfSessionPoolIsFull(true);
//            PooledConnection connection = (PooledConnection) pooledConnectionFactory.createConnection();
//            Connection connection = connectionFactory.createConnection();
//            QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();
            connection.start();
//            queueConnection.start();
            // 第三步：通过Connection对象创建Session会话（上下文环境对象），用于接收消息，参数配置1为是否启用是事务，参数配置2为签收模式，一般我们设置自动签收。
            Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
//            QueueSession queueSession = queueConnection.createQueueSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            // 第四步：通过Session创建Destination对象，指的是一个客户端用来指定生产消息目标和消费消息来源的对象，在PTP模式中，Destination被称作Queue即队列；在Pub/Sub模式，Destination被称作Topic即主题。在程序中可以使用多个Queue和Topic。
//            Destination destination = session.createQueue("test_2/device_rule_1/thing/service/property/set");
            Destination destination = session.createQueue("test_queue");
            // 第五步：我们需要通过Session对象创建消息的发送和接收对象（生产者和消费者）MessageProducer/MessageConsumer。
            MessageProducer producer = session.createProducer(null);
//            QueueSender queueSender = queueSession.createSender(null);
            // 第六步：我们可以使用MessageProducer的setDeliveryMode方法为其设置持久化特性和非持久化特性（DeliveryMode），我们稍后详细介绍。
            // producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // 第七步：最后我们使用JMS规范的TextMessage形式创建数据（通过Session对象），并用MessageProducer的send方法发送数据。同理客户端使用receive方法进行接收数据。最后不要忘记关闭Connection连接。
            for (int i = 0; i < 3; i++) {
//                Queue queue = session.createQueue("我是消息内容" + i);
                StringBuilder text = new StringBuilder();
//                    while (text.toString().getBytes().length < 1048576) {
//                        text.append("ttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
//                }
                TextMessage msg = session.createTextMessage("我是Group2消息内容:"+i+"------"+new Date());
                // 第一个参数目标地址
                // 第二个参数 具体的数据信息
                // 第三个参数 传送数据的模式
                // 第四个参数 优先级
                // 第五个参数 消息的过期时间
//                queueSender.send(destination,msg);
                producer.send(destination, msg);
                System.out.println("发送消息：" + msg.getText());
//                Thread.sleep(1000);

            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
