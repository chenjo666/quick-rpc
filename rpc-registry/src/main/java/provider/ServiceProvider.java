package provider;

import lombok.Data;
import registration.ServiceRegistration;
import registration.ZkServiceRegistration;
import utils.ZookeeperUtil;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class ServiceProvider {
    private final Map<String, Object> services;
    private final ServiceRegistration serviceRegistration;
    private final String host;
    private final int port;

    public ServiceProvider(String host, int port) {
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
