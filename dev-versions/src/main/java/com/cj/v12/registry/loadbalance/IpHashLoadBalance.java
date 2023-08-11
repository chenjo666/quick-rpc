package com.cj.v12.registry.loadbalance;

import com.cj.v12.remoting.dto.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class IpHashLoadBalance implements LoadBalance {
    private static final Logger logger = LoggerFactory.getLogger(IpHashLoadBalance.class);
    @Override
    public String getServicePath(List<String> servicePaths, RpcRequest request) {
        String ip = request.getIp();
        String[] ipParts = ip.split("\\.");
        int ipInt =  (131 * (Integer.parseInt(ipParts[0]) & 0xFF)
                + 137 * ((Integer.parseInt(ipParts[1]) >> 8) & 0xFF)
                + 139 * ((Integer.parseInt(ipParts[2]) >> 16) & 0xFF)
                + 149 * ((Integer.parseInt(ipParts[3]) >> 24) & 0xFF));
        String servicePath = servicePaths.get(ipInt % servicePaths.size());
        logger.info("IpHash 法结果: {}", servicePath);
        return servicePath;
    }
}
