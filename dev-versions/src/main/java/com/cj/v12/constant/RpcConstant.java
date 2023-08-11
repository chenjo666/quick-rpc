package com.cj.v12.constant;

public interface RpcConstant {

    int MESSAGE_MAGIC_NUM = "QRPC".hashCode();
    int MESSAGE_HEADER_LENGTH = 16;
    byte MESSAGE_VERSION = 1;
    byte MESSAGE_RESERVE = 0;

    byte MESSAGE_TYPE_REQUEST = 0;
    byte MESSAGE_TYPE_RESPONSE = 1;
    byte MESSAGE_TYPE_PING = 2;
    byte MESSAGE_TYPE_PONG = 3;
}
