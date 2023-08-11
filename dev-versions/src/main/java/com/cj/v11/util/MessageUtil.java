package com.cj.v11.util;

import com.cj.v11.dto.RpcResponse;
import com.cj.v11.dto.RpcRequest;

public class MessageUtil {

    public static Class getMessage(byte messageType) {
        switch (messageType) {
            case 0:
                return RpcRequest.class;
            case 1:
                return RpcResponse.class;
            default:
                return null;
        }
    }
}
