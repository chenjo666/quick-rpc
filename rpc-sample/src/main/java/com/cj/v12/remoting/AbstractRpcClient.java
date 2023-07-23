package com.cj.v12.remoting;

import com.cj.v12.remoting.dto.RpcRequest;
import com.cj.v12.remoting.dto.RpcResponse;
import com.cj.v12.registry.discovery.ServiceDiscovery;
import com.cj.v12.registry.discovery.ZkServiceDiscovery;
import com.cj.v12.registry.loadbalance.RandomLoadBalance;
import com.cj.v12.util.ZookeeperUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class AbstractRpcClient {

    private ServiceDiscovery serviceDiscovery;
    public AbstractRpcClient() {
        this.serviceDiscovery = ZkServiceDiscovery.builder()
                .curator(ZookeeperUtil.createCurator())
                .loadBalance(new RandomLoadBalance())
                .build();
    }
    public abstract RpcResponse sendRPCRequest(RpcRequest request);
}
