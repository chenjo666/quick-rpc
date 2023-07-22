package com.cj.v10.loadbalance;

import com.cj.v10.dto.RPCRequest;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 负载均衡算法接口
 */
public interface LoadBalance {
    // 得到服务路径
    String getServicePath(List<String> servicePaths, RPCRequest request);
}
