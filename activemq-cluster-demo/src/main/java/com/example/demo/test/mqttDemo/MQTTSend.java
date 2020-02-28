package com.example.demo.test.mqttDemo;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTSend {


    public static void main(String[] args) throws MqttException {
        String HOST = "ssl://192.9.25.177:1883";
        String MESSAGE;
        String clientid = "server";
        MqttClient client;
        MqttTopic topic = null;
        MqttMessage message;
        //获取前台传过来的两个参数
        MESSAGE="message";
        //new mqttClient
        //MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
        //与activeMQ连接的方法
        // new mqttConnection 用来设置一些连接的属性
        MqttConnectOptions options = new MqttConnectOptions();
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        // 换而言之，设置为false时可以客户端可以接受离线消息
        options.setCleanSession(false);
        // 设置连接的用户名和密码
        options.setUserName("admin");
        options.setPassword("123456".toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            // 设置回调类
//            client.setCallback(new PushCallback());
            // 连接
            client.connect(options);
            // 获取activeMQ上名为TOPIC的topic
            topic = client.getTopic("topic");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //new mqttMessage
        message = new MqttMessage();
        //设置服务质量
        message.setQos(2);
        //设置是否在服务器中保存消息体
        message.setRetained(true);
        //设置消息的内容
        message.setPayload(MESSAGE.getBytes());
        //发布
        publish(topic, message);

        System.out.println("已发送");
//        client.close();
    }

//    private static void connect(MqttClient client) {
//        // new mqttConnection 用来设置一些连接的属性
//        MqttConnectOptions options = new MqttConnectOptions();
//        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
//        // 换而言之，设置为false时可以客户端可以接受离线消息
//        options.setCleanSession(false);
//        // 设置连接的用户名和密码
//        options.setUserName("admin");
//        options.setPassword("123456".toCharArray());
//        // 设置超时时间
//        options.setConnectionTimeout(10);
//        // 设置会话心跳时间
//        options.setKeepAliveInterval(20);
//        try {
//            // 设置回调类
////            client.setCallback(new PushCallback());
//            // 连接
//            client.connect(options);
//            // 获取activeMQ上名为TOPIC的topic
//            topic = client.getTopic("topic");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void publish(MqttTopic topic, MqttMessage message) throws MqttPersistenceException,  MqttException {
        // 发布的方法
        // new mqttDeliveryToken
        MqttDeliveryToken token = topic.publish(message);
        // 发布
        token.waitForCompletion();
        System.out.println("message is published completely! "
                + token.isComplete());
    }


}
