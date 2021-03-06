package plugin;

import filter.AuthFilter;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;

public class AuthPlugin implements BrokerPlugin{
    public Broker installPlugin(Broker broker) throws Exception {
        return new AuthFilter(broker);
    }
}
