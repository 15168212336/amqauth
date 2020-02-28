package filter;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.ConnectionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthFilter extends BrokerFilter{
    private static Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    public AuthFilter(Broker next) {
        super(next);
    }

    @Override
    public void addConnection(ConnectionContext context, ConnectionInfo info) throws Exception {
        logger.info(info.getUserName());
        logger.info(info.getPassword());
        if(logger.isInfoEnabled()) {
            logger.info("创建连接，ClientId：{},RemoteAddress：{}",context.getClientId(),context.getConnection().getRemoteAddress());
        }
        super.addConnection(context, info);
    }
}
