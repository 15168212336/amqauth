/**
 * @name: AuthBroker.java
 * @copyright: soyea©2018
 * @description:
 * @author: ywm
 * @createtime: 2018/6/22 11:25
 */

package cn.com.soyea.amq.auth.broker;

import cn.com.soyea.amq.auth.context.AuthSecurityContext;
import cn.com.soyea.amq.auth.utils.AuthUtil;
import cn.com.soyea.duhp.entity.User;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.broker.ConsumerBrokerExchange;
import org.apache.activemq.broker.ProducerBrokerExchange;
import org.apache.activemq.broker.region.Destination;
import org.apache.activemq.broker.region.Subscription;
import org.apache.activemq.command.*;
import org.apache.activemq.jaas.GroupPrincipal;
import org.apache.activemq.security.AbstractAuthenticationBroker;
import org.apache.activemq.security.AuthorizationMap;
import org.apache.activemq.security.SecurityContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.redisson.api.RedissonClient;
import org.springframework.jdbc.core.JdbcTemplate;

import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * @name AuthBroker
 * @description
 * @auther ywm
 * @see
 * @since
 */
public class AuthBroker extends AbstractAuthenticationBroker{

    private static Log log = LogFactory.getLog(AuthBroker.class);

//    private static final String ACTIVEMQ_ADVISORY_PRODUCER_TOPIC = "ActiveMQ.Advisory.Producer.Topic.";//主题消息生产者前缀
//    private static final String ACTIVEMQ_ADVISORY_CONSUMER_TOPIC = "ActiveMQ.Advisory.Consumer.Topic.";//主题消息消费者前缀
//    private static final String ACTIVEMQ_ADVISORY_PRODUCER_QUEUE="ActiveMQ.Advisory.Producer.Queue.";//队列消息生产者前缀
//    private static final String ACTIVEMQ_ADVISORY_CONSUMER_QUEUE="ActiveMQ.Advisory.Consumer.Queue.";//队列消息消费者前缀
    private JdbcTemplate jdbcTemplate;
    private RedissonClient redisson;
    private Map groupMap;
    private Map writeMap;
    private Map readMap;

    public AuthBroker(Broker next, JdbcTemplate jdbcTemplate, RedissonClient redisson) {
        super(next);
        this.redisson = redisson;
        this.jdbcTemplate = jdbcTemplate;
        groupMap = new HashMap();
        writeMap = new HashMap();
        readMap = new HashMap();

        groupMap.put("admins", "admin");
        groupMap.put("users", "user");

        writeMap.put("admin", "test_topic");
        writeMap.put("user", "test_queue");

        readMap.put("admin", "test_topic");
        readMap.put("user", "test_queue");

    }

    /**
     * <p>
     * 创建连接的时候拦截
     * </p>
     */
    @Override
    public void addConnection(ConnectionContext context, ConnectionInfo info) throws Exception {
        System.out.println("addConnection");
        SecurityContext securityContext = context.getSecurityContext();
        System.out.println("userName---------------"+info.getUserName());
        System.out.println("password---------------"+info.getPassword());
        if (securityContext == null) {
            securityContext = authenticate(info.getUserName(), info.getPassword(), null);
            context.setSecurityContext(securityContext);
            securityContexts.add(securityContext);
        }
        try {
            super.addConnection(context, info);
        } catch (Exception e) {
            securityContexts.remove(securityContext);
            context.setSecurityContext(null);
            throw e;
        }
    }

    @Override
    public Subscription addConsumer(ConnectionContext context, ConsumerInfo info) throws Exception {
        System.out.println("addConsumer");
        System.out.println(context.getUserName());
        System.out.println("getDestination-----"+info.getDestination().getPhysicalName());

        Iterator<Principal> iterator = context.getSecurityContext().getPrincipals().iterator();
        while (iterator.hasNext()) {
            System.out.println("principal-------"+iterator.next());
        }

        if (!AuthUtil.hasReadPrivilege(context.getUserName(), info.getDestination().getPhysicalName())) {
            throw new SecurityException("User " + context.getSecurityContext().getUserName() + " is not authorized to create: " + info.getDestination());
        }
        return super.addConsumer(context, info);
    }

//    @Override
//    public void addProducer(ConnectionContext context, ProducerInfo info) throws Exception {
//        System.out.println("addProducer");
//        System.out.println(context.getUserName());
//        System.out.println("getDestination-----"+info.getDestination());
////        if (!context.getSecurityContext().isBrokerContext() && info.getDestination() != null) {
////            if (!AuthUtil.hasWritePrivilege(context.getUserName(), info.getDestination().getPhysicalName())) {
////                throw new SecurityException("User " + context.getSecurityContext().getUserName() + " is not authorized to create: " + info.getDestination());
////            }
////        }
//        super.addProducer(context, info);
//    }

    /**
     * 认证
     * <p>Title: authenticate</p>
     */
    @Override
    public SecurityContext authenticate(String username, String password, X509Certificate[] peerCertificates) throws SecurityException {
        System.out.println("authenticate");
        User user = AuthUtil.getUser(username);
        //验证用户信息
        if (user != null && user.getClientSecret().equals(password)) {
            // 获取用户所属组
            Set<Principal> groups = new HashSet<Principal>();
            String[] groupArray = user.getGroups().split(",");
            if (null != groupArray && groupArray.length > 0) {
                for (int i = 0; i < groupArray.length; i++) {
                    groups.add(new GroupPrincipal(groupArray[i].trim()));
                    System.out.println("-------group------"+groupArray[i].trim());
                }
            } else {
                return null;
            }

            // 获取用户具有写权限的主题
            Set<String> writeTopics = new HashSet<>();
            String[] writeTopicArray = user.getWriteTopics().split("\\|");
            if (null != writeTopicArray && writeTopicArray.length > 0) {
                for (int i = 0; i < writeTopicArray.length; i++) {
                    writeTopics.add(writeTopicArray[i].trim());
                    System.out.println("-------writeTopic------"+writeTopicArray[i].trim());
                }
            } else {
                return null;
            }

            // 获取用户具有读权限的主题
            Set<String> readTopics = new HashSet<>();
            String[] readTopicArray = user.getReadTopics().split("\\|");
            if (null != readTopicArray && readTopicArray.length > 0) {
                for (int i = 0; i < readTopicArray.length; i++) {
                    readTopics.add(readTopicArray[i].trim());
                    System.out.println("-------readTopic------"+readTopicArray[i].trim());
                }
            } else {
                return null;
            }

            SecurityContext securityContext = new AuthSecurityContext(username, groups, writeTopics, readTopics);
//            System.out.println(securityContext.getAuthorizedReadDests());
//            System.out.println(securityContext.getAuthorizedWriteDests());
            return securityContext;

        } else {
            throw new SecurityException("验证失败");
        }
    }

    /**
     * 监控发送消息
     * @param producerExchange
     * @param messageSend
     * @throws Exception
     */
    @Override
    public void send(ProducerBrokerExchange producerExchange, Message messageSend) throws Exception {
        String userName = producerExchange.getConnectionContext().getUserName();
        if (!AuthUtil.hasWritePrivilege(userName, messageSend.getDestination().getPhysicalName())) {
            throw new SecurityException("User " + userName + " is not authorized to write from: " + messageSend.getDestination().getPhysicalName());
        }
        super.send(producerExchange, messageSend);

    }



    /**
     * 监控接收消息签收
     * @param consumerExchange
     * @param ack
     * @throws Exception
     */
    @Override
    public void acknowledge(ConsumerBrokerExchange consumerExchange, MessageAck ack) throws Exception {
        String userName = consumerExchange.getConnectionContext().getUserName();
        if (AuthUtil.hasReadPrivilege(userName, ack.getDestination().getPhysicalName())) {
            super.acknowledge(consumerExchange, ack);
        }
    }


}