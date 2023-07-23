package com.cj.v12.provider;

import com.cj.v12.registry.registration.ServiceRegistration;
import com.cj.v12.registry.registration.ZkServiceRegistration;
import com.cj.v12.util.ZookeeperUtil;
import lombok.Data;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class AbstractServiceProvider {
    private final Map<String, Object> services;
    private final ServiceRegistration serviceRegistration;
    private final String host;
    private final int port;

    public AbstractServiceProvider(String host, int port) {
        this.services = new HashMap<>();
        this.serviceRegistration = ZkServiceRegistration.builder()
                .curator(ZookeeperUtil.createCurator())
                .build();
        this.host = host;
        this.port = port;
    }
    public void addService(String serviceName, Object service) {
        services.put(serviceName, service);
    }

    public Object getService(String serviceName) {
        return services.get(serviceName);
    }

    public void delService(String serviceName) {
        services.remove(serviceName);
    }

    public void pubService(String serviceName) {
        serviceRegistration.registerService(serviceName, new InetSocketAddress(host, port));
    }
}
