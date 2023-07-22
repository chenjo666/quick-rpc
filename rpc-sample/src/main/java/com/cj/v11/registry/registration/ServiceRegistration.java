package com.cj.v11.registry.registration;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 */
public interface ServiceRegistration {

    void registerService(String serviceName, InetSocketAddress serverAddress);
}
