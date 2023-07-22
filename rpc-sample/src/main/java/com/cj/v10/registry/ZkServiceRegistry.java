package com.cj.v10.registry;

import com.cj.v10.dto.RPCRequest;
import com.cj.v10.loadbalance.LoadBalance;
import com.cj.v10.util.ServiceUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;


public class ZkServiceRegistry implements ServiceRegistry {
    // Zookeeper 的客户端 curator
    private final CuratorFramework curator;
    // Zookeeper 的根目录 "quick-rpc"
    private static final String ROOT_PATH = "quick-rpc";
    // Zookeeper 的集群地址（目前只搭建一个）
    private static final String CLUSTER_ADDRESS = "127.0.0.1:2181";

    // 负载均衡算法
    private final LoadBalance loadBalance;
    private static final Logger logger = LoggerFactory.getLogger(ZkServiceRegistry.class);

    // Zookeeper 的配置
    public ZkServiceRegistry(LoadBalance loadBalance) {
        this.curator = CuratorFrameworkFactory.builder()
                .connectString(CLUSTER_ADDRESS)
                .sessionTimeoutMs(40000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace(ROOT_PATH)
                .build();
        this.curator.start();
        this.loadBalance = loadBalance;
        logger.info("Zookeeper 连接成功: {}", curator);
    }

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

    @Override
    public InetSocketAddress discoverService(RPCRequest request) {
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
