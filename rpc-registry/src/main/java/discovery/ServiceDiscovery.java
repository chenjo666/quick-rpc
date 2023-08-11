package discovery;

import dtos.RpcRequest;

import java.net.InetSocketAddress;

/**
 * 服务发现
 */
public interface ServiceDiscovery {

    InetSocketAddress discoverService(RpcRequest request);
}
