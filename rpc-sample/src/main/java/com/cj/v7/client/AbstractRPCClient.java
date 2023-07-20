package com.cj.v7.client;

import com.cj.v7.dto.RPCRequest;
import com.cj.v7.dto.RPCResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class AbstractRPCClient {
    private String host;
    private int port;
    public abstract RPCResponse sendRPCRequest(RPCRequest request);
}
