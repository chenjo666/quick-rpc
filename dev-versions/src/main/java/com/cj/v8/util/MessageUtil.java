package com.cj.v8.util;

import com.cj.v8.dto.RPCRequest;
import com.cj.v8.dto.RPCResponse;

public class MessageUtil {

    public static Class getMessage(byte messageType) {
        switch (messageType) {
            case 0:
                return RPCRequest.class;
            case 1:
                return RPCResponse.class;
            default:
                return null;
        }
    }
}
