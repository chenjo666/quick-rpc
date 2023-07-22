package com.cj.v11.registry.loadbalance;

import com.cj.v11.dto.RpcRequest;

import java.util.List;

/**
 * 负载均衡算法接口
 */
public interface LoadBalance {
    // 得到服务路径
    String getServicePath(List<String> servicePaths, RpcRequest request);
}
