package security;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.security.AbstractAuthenticationBroker;
import org.apache.activemq.security.AuthorizationMap;
import org.apache.activemq.security.SecurityContext;

import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class MySimpleAuthenticationBroker extends AbstractAuthenticationBroker{
    private volatile Map authorizationMap;
    public MySimpleAuthenticationBroker(Broker next) {
        super(next);
        authorizationMap = new HashMap();
        authorizationMap.put("admin", "");

    }

    public SecurityContext authenticate(String s, String s1, X509Certificate[] x509Certificates) throws SecurityException {
        return null;
    }

}
