package com.cj.v12.remoting.dto;

import lombok.Data;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Data
public class RpcPong {
    private final String message;

    public RpcPong() {
        try {
            message = "pong: " + InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
