package com.cj.v11.registry.loadbalance;

import com.cj.v11.dto.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * 随机法
 */
public class RandomLoadBalance implements LoadBalance {
    private static final Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(RandomLoadBalance.class);
    @Override
    public String getServicePath(List<String> servicePaths, RpcRequest request) {
        String servicePath = servicePaths.get(random.nextInt(servicePaths.size()));
        logger.info("随机法结果: {}", servicePath);
        return servicePath;
    }
}
