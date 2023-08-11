package utils;

import dtos.RpcPing;
import dtos.RpcPong;
import dtos.RpcRequest;
import dtos.RpcResponse;

public class MessageUtil {

    public static Class getMessage(byte messageType) {
        switch (messageType) {
            case 0:
                return RpcRequest.class;
            case 1:
                return RpcResponse.class;
            case 2:
                return RpcPing.class;
            case 3:
                return RpcPong.class;
            default:
                return null;
        }
    }
}
