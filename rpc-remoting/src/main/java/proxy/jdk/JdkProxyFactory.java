package proxy.jdk;

import netty.NettyRpcClient;
import proxy.AbstractProxyFactory;

import java.lang.reflect.Proxy;

public class JdkProxyFactory extends AbstractProxyFactory {
    private RpcClientJdkProxy clientJdkProxy;
    @Override
    public <T> T getProxy(Class<T> clazz) {
        if (clientJdkProxy == null) {
            clientJdkProxy = new RpcClientJdkProxy(new NettyRpcClient());
        }
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                clientJdkProxy);
    }
}
