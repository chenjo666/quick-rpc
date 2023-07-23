package com.cj.v12.remoting.dto;

import lombok.Data;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Data
public class RpcHeartbeat {
    private final String message;

    public RpcHeartbeat() {
        try {
            message = "HEARTBEAT:" + InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
