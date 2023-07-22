package com.cj.v11.registry.discovery;

import com.cj.v11.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * 服务发现
 */
public interface ServiceDiscovery {

    InetSocketAddress discoverService(RpcRequest request);
}
