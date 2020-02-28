package com.example.demo.test.demo2;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;

public class JMX {
    public static void main(String[] args) {
        for (int j = 0; j <= 10000; j++) {
            String[] url = {"service:jmx:rmi:///jndi/rmi://192.9.25.225:11097/jmxrmi","service:jmx:rmi:///jndi/rmi://192.9.25.225:11098/jmxrmi","service:jmx:rmi:///jndi/rmi://192.9.25.225:11099/jmxrmi"};
            JMXConnector connector = null;
//这里brokerName的b要小些，大写会报错
            ObjectName name = null;
            try {
                name = new ObjectName("org.apache.activemq:brokerName=localhost,type=Broker");
                for(int i=0;i<url.length;i++){
                    JMXServiceURL urls = new JMXServiceURL(url[i]);
                    connector = tryConnect(urls,name);
                    if (j==10000){
                        System.out.println("10000");
                    }
                    if (connector != null) {
                        i=url.length;
                    }
                }
//            connector.connect();
                MBeanServerConnection conn = connector.getMBeanServerConnection();
                BrokerViewMBean mBean = MBeanServerInvocationHandler.newProxyInstance(conn, name, BrokerViewMBean.class, true);
                for(ObjectName na : mBean.getQueues()){
//                    QueueViewMBean queueBean = MBeanServerInvocationHandler.newProxyInstance
//                            (conn, na, QueueViewMBean.class, true);
//                    System.out.println("******************************");
//                    System.out.println("队列的名称："+queueBean.getName());
//                    System.out.println("队列中剩余的消息数："+queueBean.getQueueSize());
//                    System.out.println("消费者数："+queueBean.getConsumerCount());
//                    System.out.println("出队列的数量："+queueBean.getDequeueCount());
                    System.out.println(j);
                }
                connector.close();
            } catch (MalformedObjectNameException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static JMXConnector tryConnect(JMXServiceURL url,ObjectName name) throws IOException{
        JMXConnector connector = null;
        try {
            connector = JMXConnectorFactory.connect(url);
            connector.connect();
            BrokerViewMBean mBean = MBeanServerInvocationHandler.newProxyInstance(connector.getMBeanServerConnection(), name, BrokerViewMBean.class, true);
            mBean.getQueues();
        } catch (IOException e) {
//            connector.close();
            return null;
        } catch (IllegalStateException i) {
            connector.close();
            return null;
        }
        return connector;
    }
}
