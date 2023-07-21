package com.cj.v8.client;

import com.cj.v8.dto.RPCRequest;
import com.cj.v8.dto.RPCResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class AbstractRPCClient {
    private String host;
    private int port;
    public abstract RPCResponse sendRPCRequest(RPCRequest request);
}
