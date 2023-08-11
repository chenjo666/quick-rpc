package com.cj.v10.registry;

import com.cj.v10.dto.RPCRequest;

import java.net.InetSocketAddress;

/**
 * 服务注册中心
 */
public interface ServiceRegistry {
    // 功能一：服务注册
    void registerService(String serviceName, InetSocketAddress serverAddress);
    // 功能二：服务发现
    InetSocketAddress discoverService(RPCRequest request);
}
