package cn.com.soyea.amq.auth.plugin;

import cn.com.soyea.amq.auth.utils.AuthUtil;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.Message;
import org.apache.activemq.security.MessageAuthorizationPolicy;

/**
 * 消息级别授权
 * 验证是否有读取消息权限
 */
public class AuthorizationPolicy implements MessageAuthorizationPolicy {
    @Override
    public boolean isAllowedToConsume(ConnectionContext connectionContext, Message message) {
        String physicalName = message.getDestination().getPhysicalName();
        String userName = connectionContext.getSecurityContext().getUserName();
        if (AuthUtil.hasReadPrivilege(userName, physicalName)) {
            System.out.println("isAllowedToConsume------------true");
            return true;
        }else{
            System.out.println("isAllowedToConsume------------true");
            return false;
        }
    }
}
