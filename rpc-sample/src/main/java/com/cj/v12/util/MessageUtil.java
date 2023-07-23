package com.cj.v12.util;

import com.cj.v12.remoting.dto.RpcHeartbeat;
import com.cj.v12.remoting.dto.RpcRequest;
import com.cj.v12.remoting.dto.RpcResponse;

public class MessageUtil {

    public static Class getMessage(byte messageType) {
        switch (messageType) {
            case 0:
                return RpcRequest.class;
            case 1:
                return RpcResponse.class;
            case 2:
                return RpcHeartbeat.class;
            default:
                return null;
        }
    }
}
