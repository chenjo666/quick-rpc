package com.cj.v11.remoting;

import com.cj.v11.dto.RpcResponse;
import com.cj.v11.dto.RpcRequest;
import com.cj.v11.registry.discovery.ServiceDiscovery;
import com.cj.v11.registry.discovery.ZkServiceDiscovery;
import com.cj.v11.registry.loadbalance.RandomLoadBalance;
import com.cj.v11.util.ZookeeperUtil;
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
