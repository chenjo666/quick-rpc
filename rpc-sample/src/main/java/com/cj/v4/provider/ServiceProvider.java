package com.cj.v4.provider;

public interface ServiceProvider {

    void addService(String serviceName, Object service);

    Object getService(String serviceName);

    void delService(String serviceName);
}
