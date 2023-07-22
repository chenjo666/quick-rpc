package com.cj.v9.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 统一化请求
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RPCRequest implements Serializable {
    // 服务接口名
    private String interfaceName;
    // 服务方法名
    private String methodName;
    // 服务方法参数类型
    private Class<?>[] argsTypes;
    // 服务方法参数列表
    private Object[] args;
}
