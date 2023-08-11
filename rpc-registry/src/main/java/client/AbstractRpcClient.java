package client;

import discovery.ServiceDiscovery;
import discovery.ZkServiceDiscovery;
import dtos.RpcRequest;
import dtos.RpcResponse;
import loadbalance.RandomLoadBalance;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ZookeeperUtil;

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
