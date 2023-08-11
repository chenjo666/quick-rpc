package com.cj.v10.client;

import com.cj.v10.dto.RPCRequest;
import com.cj.v10.dto.RPCResponse;
import com.cj.v10.registry.ServiceRegistry;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class AbstractRPCClient {

    private ServiceRegistry serviceRegistry;
    public abstract RPCResponse sendRPCRequest(RPCRequest request);
}
