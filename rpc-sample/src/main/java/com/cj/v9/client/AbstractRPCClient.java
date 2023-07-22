package com.cj.v9.client;

import com.cj.v9.dto.RPCRequest;
import com.cj.v9.dto.RPCResponse;
import com.cj.v9.registry.ServiceRegistry;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class AbstractRPCClient {

    private ServiceRegistry serviceRegistry;
    public abstract RPCResponse sendRPCRequest(RPCRequest request);
}
