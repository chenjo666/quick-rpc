package com.cj.v9.provider.impl;

import com.cj.v9.provider.ServiceProvider;
import com.cj.v9.registry.ServiceRegistry;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class DefaultServiceProvider implements ServiceProvider {
    private final Map<String, Object> services;
    private final ServiceRegistry serviceRegistry;
    private final String host;
    private final int port;
    public DefaultServiceProvider(String host, int port, ServiceRegistry serviceRegistry) {
        this.services = new HashMap<>();
        this.serviceRegistry = serviceRegistry;
        this.host = host;
        this.port = port;
    }

    @Override
    public void addService(String serviceName, Object service) {
        services.put(serviceName, service);
    }

    @Override
    public Object getService(String serviceName) {
        return services.get(serviceName);
    }

    @Override
    public void delService(String serviceName) {
        services.remove(serviceName);
    }

    @Override
    public void pubService(String serviceName) {
        serviceRegistry.registerService(serviceName, new InetSocketAddress(host, port));
    }
}
