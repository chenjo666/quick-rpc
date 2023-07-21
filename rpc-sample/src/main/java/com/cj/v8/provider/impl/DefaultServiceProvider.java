package com.cj.v8.provider.impl;

import com.cj.v8.provider.ServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class DefaultServiceProvider implements ServiceProvider {
    private Map<String, Object> services;

    public DefaultServiceProvider() {
        this.services = new HashMap<>();
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
}
