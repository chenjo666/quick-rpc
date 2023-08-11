package com.cj.v3;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RPCResponse implements Serializable {
    // 响应状态码
    private Integer code;
    // 响应消息体
    private String message;
    // 响应数据
    private Object data;

    // 成功
    public static RPCResponse ok(Object data) {
        return RPCResponse.builder().code(200).message("success").data(data).build();
    }
    // 失败
    public static RPCResponse error() {
        return RPCResponse.builder().code(500).message("error").build();
    }
}
