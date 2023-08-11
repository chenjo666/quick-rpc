package com.cj.v8.constant;

public interface RPCConstant {

    int MESSAGE_MAGIC_NUM = "QRPC".hashCode();
    int MESSAGE_HEADER_LENGTH = 16;
    byte MESSAGE_VERSION = 1;
    byte MESSAGE_RESERVE = 0;

    byte MESSAGE_TYPE_REQUEST = 0;
    byte MESSAGE_TYPE_RESPONSE = 1;

    byte MESSAGE_SERIALIZE_JDK = 0;
    byte MESSAGE_SERIALIZE_JSON = 1;
    byte MESSAGE_SERIALIZE_KRYO = 2;

}
