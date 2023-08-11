package com.cj.v11.registry.loadbalance;

import com.cj.v11.dto.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询法
 */
public class RoundLoadBalance implements LoadBalance {
    // 使用原子类保证并发安全
    private final AtomicInteger index = new AtomicInteger(-1);
    private static final Logger logger = LoggerFactory.getLogger(RoundLoadBalance.class);

    @Override
    public String getServicePath(List<String> servicePaths, RpcRequest request) {
        String servicePath = servicePaths.get(index.incrementAndGet() % servicePaths.size());
        logger.info("轮询法结果: {}", servicePath);
        return servicePath;
    }
}
