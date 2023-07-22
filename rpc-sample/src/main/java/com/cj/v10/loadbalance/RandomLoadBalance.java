package com.cj.v10.loadbalance;

import com.cj.v10.dto.RPCRequest;
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
    public String getServicePath(List<String> servicePaths, RPCRequest request) {
        String servicePath = servicePaths.get(random.nextInt(servicePaths.size()));
        logger.info("随机法结果: {}", servicePath);
        return servicePath;
    }
}
