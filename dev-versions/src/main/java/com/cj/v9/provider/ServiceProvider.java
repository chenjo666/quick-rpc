package com.cj.v9.provider;

public interface ServiceProvider {

    /**
     * 添加服务
     * @param serviceName
     * @param service
     */
    void addService(String serviceName, Object service);

    /**
     * 获得服务
     * @param serviceName
     * @return
     */
    Object getService(String serviceName);

    /**
     * 删除服务
     * @param serviceName
     */
    void delService(String serviceName);


    /**
     * 发布服务
     * @param serviceName
     */
    void pubService(String serviceName);
}
