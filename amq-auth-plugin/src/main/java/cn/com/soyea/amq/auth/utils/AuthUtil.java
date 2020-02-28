package cn.com.soyea.amq.auth.utils;

import cn.com.soyea.duhp.entity.User;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthUtil {
    private static RedissonClient redisson;
    private static JdbcTemplate jdbcTemplate;
    public AuthUtil(RedissonClient redisson,JdbcTemplate jdbcTemplate){
        this.redisson = redisson;
        this.jdbcTemplate = jdbcTemplate;
    }


    /**
     * 得到用户信息
     * <p>Title: getUser</p>
     *
     * @param name
     * @return
     */
    public static User getUser(String name) {
//        RBucket<User> bucket = redisson.getBucket("ActiveMQ_user:" + name);
//        User user = bucket.get();
//        if (user != null) {
//            return user;
//        }
        String sql = "select * from oauth_client_details where client_id=? limit 1";
//        user = jdbcTemplate.queryForObject(sql, new Object[]{name}, new BeanPropertyRowMapper<>(User.class));
        User user = jdbcTemplate.queryForObject(sql, new Object[]{name}, new BeanPropertyRowMapper<>(User.class));
//        bucket.set(user);
        return user;
    }

    /**
     * 是否拥有写权限
     * @param userName
     * @param destinationName
     * @return
     */
    public static boolean hasWritePrivilege(String userName, String destinationName) {
        if (null == userName || userName.isEmpty() || null == destinationName || destinationName.isEmpty()) {
            return false;
        }

        User user = getUser(userName);
        if (null == user) {
            return false;
        }

        // 获取用户具有写权限的主题
        String[] writeTopicArray = user.getWriteTopics().split("\\|");
        if (null != writeTopicArray && writeTopicArray.length > 0) {
            for (int i = 0; i < writeTopicArray.length; i++) {
                if (match(writeTopicArray[i].trim(),destinationName)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 是否拥有读权限
     * @param userName
     * @param destinationName
     * @return
     */
    public static boolean hasReadPrivilege(String userName, String destinationName) {
        if (null == userName || userName.isEmpty() || null == destinationName || destinationName.isEmpty()) {
            return false;
        }

        User user = getUser(userName);
        if (null == user) {
            return false;
        }

        // 获取用户具有读权限的主题
        String[] readTopicArray = user.getReadTopics().split("\\|");
        if (null != readTopicArray && readTopicArray.length > 0) {
            for (int i = 0; i < readTopicArray.length; i++) {
                if (match(readTopicArray[i].trim(),destinationName)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 验证正则表达式
     * @param regex
     * @param str
     * @return
     */
    public static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}
