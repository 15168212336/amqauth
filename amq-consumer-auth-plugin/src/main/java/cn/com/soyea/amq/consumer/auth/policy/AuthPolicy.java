/**
 * @name: AuthPolicy.java
 * @copyright: soyea©2018
 * @description:
 * @author: ywm
 * @createtime: 2018/6/26 9:55
 */

package cn.com.soyea.amq.consumer.auth.policy;
import cn.com.soyea.amq.consumer.auth.entity.User;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.Message;
import org.apache.activemq.security.MessageAuthorizationPolicy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @name AuthPolicy
 * @description
 * @auther ywm
 * @see
 * @since
 */
public class AuthPolicy implements MessageAuthorizationPolicy {

    private static Log log = LogFactory.getLog(AuthPolicy.class);

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User getUser(String name) {
        String sql = "select * from activemq_user where name=? limit 1";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{name}, new BeanPropertyRowMapper<User>(User.class));
        return user;
    }

    public boolean isAllowedToConsume(ConnectionContext context, Message message) {
        String userName = context.getSecurityContext().getUserName();
        log.info(userName);
        ActiveMQDestination destination = message.getDestination();
        String consumerDest = destination.getPhysicalName();
        log.info(consumerDest);
        if (!hasReadPrivilege(userName, consumerDest)) {
            log.info("Permission to consume denied");
            return false;
        }

        log.info("Permission to consume granted");
        return true;
    }

    private boolean hasReadPrivilege(String userName, String destinationName) {
        if (null == userName || userName.isEmpty() || null == destinationName || destinationName.isEmpty()) {
            return false;
        }

        User user = getUser(userName);
        if (null == user) {
            return false;
        }

        // 获取用户具有写权限的主题
        String[] readTopicArray = user.getReadTopics().split("\\|");
        if (null != readTopicArray && readTopicArray.length > 0) {
            for (int i = 0; i < readTopicArray.length; i++) {
                if (readTopicArray[i].trim().equals(destinationName)) {
                    return true;
                }
            }
        }

        return false;
    }
}