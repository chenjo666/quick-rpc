package discovery;


import dtos.RpcRequest;
import loadbalance.LoadBalance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ServiceUtil;

import java.net.InetSocketAddress;
import java.util.List;

@AllArgsConstructor
@Builder
public class ZkServiceDiscovery implements ServiceDiscovery {
    private final CuratorFramework curator;
    private final LoadBalance loadBalance;
    private static final Logger logger = LoggerFactory.getLogger(ZkServiceDiscovery.class);
    @Override
    public InetSocketAddress discoverService(RpcRequest request) {
        String serviceName = request.getInterfaceName();
        try {
            List<String> servicePaths = curator.getChildren().forPath("/" + serviceName);
            // 采用负载均衡算法
            String servicePath = loadBalance.getServicePath(servicePaths, request);
            InetSocketAddress serverAddress = ServiceUtil.getServerAddress(servicePath);
            logger.info("发现服务: {}:{}", serviceName, serverAddress);
            return serverAddress;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
