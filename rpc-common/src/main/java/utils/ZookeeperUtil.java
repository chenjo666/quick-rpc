package utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZookeeperUtil {
    // Zookeeper 的根目录 "quick-rpc"
    private static final String ROOT_PATH = "quick-rpc";
    // Zookeeper 的集群地址（目前只搭建一个）
    private static final String CLUSTER_ADDRESS = "127.0.0.1:2181";

    public static CuratorFramework createCurator() {
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString(CLUSTER_ADDRESS)
                .sessionTimeoutMs(40000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace(ROOT_PATH)
                .build();
        curator.start();
        return curator;
    }
}
