/**
 * @name: AuthPlugin.java
 * @copyright: soyea©2018
 * @description:
 * @author: ywm
 * @createtime: 2018/6/22 11:21
 */
package cn.com.soyea.amq.auth.plugin;

import cn.com.soyea.amq.auth.broker.AuthBroker;
import cn.com.soyea.amq.auth.utils.AuthUtil;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;

/**
 * @name AuthPlugin
 *@description
 *@auther ywm
 *@version
 *@see
 *@since
 */
public class AuthPlugin implements BrokerPlugin {

    private JdbcTemplate jdbcTemplate;
    private RedissonClient redisson;

    public AuthPlugin(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate=jdbcTemplate;
    }

    public Broker installPlugin(Broker broker) throws Exception {
        Config config = Config.fromYAML(new File("E:\\MyFile\\demo\\ActiveMQ-cluster\\activemq-cluster-5.15.9\\apache-activemq-5.15.9\\conf\\redisson.yml"));
//        Config config = Config.fromYAML(new File("/usr/local/apache-activemq-5.15.6/conf/redisson.yml"));
        redisson = Redisson.create(config);
        new AuthUtil(redisson,jdbcTemplate);
        return new AuthBroker(broker, jdbcTemplate,redisson);
    }

}
