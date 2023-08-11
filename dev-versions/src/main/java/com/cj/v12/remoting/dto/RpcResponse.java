package com.cj.v12.remoting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {
    // 响应状态码
    private Integer code;
    // 响应消息体
    private String message;
    // 响应数据
    private Object data;

    // 成功
    public static RpcResponse ok(Object data) {
        return RpcResponse.builder().code(200).message("success").data(data).build();
    }
    // 失败
    public static RpcResponse error() {
        return RpcResponse.builder().code(500).message("error").build();
    }
}
