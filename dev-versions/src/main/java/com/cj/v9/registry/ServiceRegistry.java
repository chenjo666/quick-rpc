package com.cj.v9.registry;

import java.net.InetSocketAddress;

/**
 * 服务注册中心
 */
public interface ServiceRegistry {
    // 功能一：服务注册
    void registerService(String serviceName, InetSocketAddress serverAddress);
    // 功能二：服务发现
    InetSocketAddress discoverService(String serviceName);
}
