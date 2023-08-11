package com.cj.v12.registry.discovery;

import com.cj.v12.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * 服务发现
 */
public interface ServiceDiscovery {

    InetSocketAddress discoverService(RpcRequest request);
}
