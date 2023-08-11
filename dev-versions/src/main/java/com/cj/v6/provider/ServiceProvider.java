package com.cj.v6.provider;

public interface ServiceProvider {

    void addService(String serviceName, Object service);

    Object getService(String serviceName);

    void delService(String serviceName);
}
