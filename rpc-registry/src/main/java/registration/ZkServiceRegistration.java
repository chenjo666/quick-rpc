package registration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ServiceUtil;

import java.net.InetSocketAddress;

@AllArgsConstructor
@Builder
public class ZkServiceRegistration implements ServiceRegistration {
    private final CuratorFramework curator;
    private static final Logger logger = LoggerFactory.getLogger(ZkServiceRegistration.class);
    @Override
    public void registerService(String serviceName, InetSocketAddress serverAddress) {
        // 服务名称路径
        String serviceNamePath = "/" + serviceName;
        // 服务提供者路径
        String serviceProviderPath = serviceNamePath + "/" + ServiceUtil.getServerAddress(serverAddress);
        try {
            // 1. 为服务创建永久节点，方便后续服务提供者时无需再建服务
            if (curator.checkExists().forPath(serviceNamePath) == null) {
                curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(serviceNamePath);
            }
            // 2. 为服务提供者创建临时节点，方便服务提供者故障时下线处理
            curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(serviceProviderPath);
            logger.info("注册服务: {}:{}", serviceName, serverAddress);
        } catch (Exception e) {
            logger.info("服务已存在: {}", e.getMessage());
        }
    }
}
