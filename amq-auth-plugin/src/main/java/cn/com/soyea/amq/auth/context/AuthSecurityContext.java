/**
 * @name: AuthSecurityContext.java
 * @copyright: soyea©2018
 * @description:
 * @author: ywm
 * @createtime: 2018/6/24 14:35
 */

package cn.com.soyea.amq.auth.context;

import org.apache.activemq.security.SecurityContext;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

/**
 * @name AuthSecurityContext
 *@description
 *@auther ywm
 *@version
 *@see
 *@since
 */
public class AuthSecurityContext extends SecurityContext {

    private Set<Principal> principals;
    private Set<String> allowedWriteDestinations = new HashSet<>();
    private Set<String> allowedReadDestinations = new HashSet<>();

    public AuthSecurityContext(
            String userName,
            Set<Principal> principals,
            Set<String> allowedWriteTopics, Set<String> allowedReadTopics) {
        super(userName);
        this.principals = principals;
        for (String topic : allowedWriteTopics) {
            topic = topic.trim();
            if (!topic.isEmpty())
                System.out.println("write-topic://" + topic.trim());
                this.allowedWriteDestinations.add("topic://" + topic.trim());
        }
        for (String topic : allowedReadTopics) {
            topic = topic.trim();
            if (!topic.isEmpty())
                System.out.println("read-topic://" + topic.trim());
                this.allowedReadDestinations.add("topic://" + topic.trim());
        }
    }

    @Override
    public Set<Principal> getPrincipals() {
        return this.principals;
    }

    public boolean verifyWriteDestination(String fullQualifiedDestination) {
        return this.allowedWriteDestinations.contains(fullQualifiedDestination);
    }

    public boolean verifyReadDestination(String fullQualifiedDestination) {
        return this.allowedReadDestinations.contains(fullQualifiedDestination);
    }
}