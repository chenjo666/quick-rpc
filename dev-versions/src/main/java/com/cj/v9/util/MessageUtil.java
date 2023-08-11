package com.cj.v9.util;

import com.cj.v9.dto.RPCRequest;
import com.cj.v9.dto.RPCResponse;

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
